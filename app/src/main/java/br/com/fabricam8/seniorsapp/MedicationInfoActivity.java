package br.com.fabricam8.seniorsapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import br.com.fabricam8.seniorsapp.alarm.AlarmPlayerService;
import br.com.fabricam8.seniorsapp.alarm.NotificationEventService;
import br.com.fabricam8.seniorsapp.dal.AlertEventDAL;
import br.com.fabricam8.seniorsapp.dal.AlertEventReportEntryDAL;
import br.com.fabricam8.seniorsapp.dal.MedicationDAL;
import br.com.fabricam8.seniorsapp.domain.AlertEvent;
import br.com.fabricam8.seniorsapp.domain.AlertEventReportEntry;
import br.com.fabricam8.seniorsapp.domain.Medication;
import br.com.fabricam8.seniorsapp.enums.ReportResponseType;
import br.com.fabricam8.seniorsapp.util.FormHelper;
import br.com.fabricam8.seniorsapp.util.ToolbarBuilder;


public class MedicationInfoActivity extends ActionBarActivity {

    public static final String BUNDLE_ID = "_ID_";
    private long medicationId;
    private long alertId;
    private Medication sessionMedication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication_info);

        // create toolbar
        Toolbar mToolbar = ToolbarBuilder.build(this, true);
        mToolbar.setBackgroundColor(getResources().getColor(R.color.seniors_active_dash_button_color_navy));
        mToolbar.setTitle("Informações");
    }

    @Override
    protected void onResume() {
        super.onResume();

        // recuperando id passada no clique
        medicationId = getIntent().getLongExtra(BUNDLE_ID, -1);
        if (medicationId == -1) {
            finish(); // se nao enviar bundle, sai do activity
        } else {
            loadMedication(medicationId);
        }

        alertId = getIntent().getLongExtra(NotificationEventService.BUNDLE_ALERT_ID, -1);
        Log.i("Alrme Service (Med Info) - Seniors", "alert id = " + alertId);
        if (alertId != -1) {
            // mostrar botoes de pular e salvar
            showAlarmDialog(alertId);
        }
    }

    public void editMedication(View view) {
        Intent intent = new Intent(this, MedicationFormActivity.class);
        intent.putExtra("_ID_", medicationId);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_medication_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.action_md_info_delete:
                deleteMedication();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void deleteMedication() {
        final Activity _this = this;

        new AlertDialog.Builder(this)
                .setTitle("Confirme")
                .setMessage("Deseja realmente excluir a medicação?")
                .setIcon(R.drawable.ic_action_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (medicationId > 0) {
                            MedicationDAL db = MedicationDAL.getInstance(_this);
                            Medication mObj = db.findOne(medicationId);
                            long objId = mObj.getID();
                            if (db.remove(mObj) > 0) {
                                // agora remove alarme, se tiver algum cadastrado
                                AlertEventDAL alertDb = AlertEventDAL.getInstance(_this);
                                AlertEvent alert = alertDb.findOneByEntityIdAndType(objId, Medication.class.getName());
                                if (alert != null) {
                                    Log.i("Seniors - Medication Info", "Removendo alarme");
                                    alertDb.remove(alert);
                                }

                                Toast.makeText(_this, getString(R.string.success_medication_deletion), Toast.LENGTH_LONG).show();
                                finish();
                            } else {
                                Toast.makeText(_this, getString(R.string.fail_medication_deletion), Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }


    private void loadMedication(long id) {
        MedicationDAL db = MedicationDAL.getInstance(this);
        sessionMedication = db.findOne(id);
        if (sessionMedication != null) {
            // setando valores
            FormHelper.setTextBoxValue(this, R.id.med_info_name, sessionMedication.getName());
            FormHelper.setTextBoxValue(this, R.id.med_info_dosage, "Tomar " + sessionMedication.getDosage() + " " + sessionMedication.getDosageMeasureType().toString());
            FormHelper.setTextBoxValue(this, R.id.med_info_periodicity, sessionMedication.getPeriodicity().toString());

            if (sessionMedication.isContinuosUse())
                FormHelper.setTextBoxValue(this, R.id.med_info_duration, "Uso contínuo");
            else
                FormHelper.setTextBoxValue(this, R.id.med_info_duration, "Por " + sessionMedication.getDuration() + " " + sessionMedication.getDurationType().toString());

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
            FormHelper.setTextBoxValue(this, R.id.med_info_starting, "A partir de " + dateFormat.format(sessionMedication.getStartDate()));
            FormHelper.setTextBoxValue(this, R.id.med_info_observations, sessionMedication.getDescription());

            // com ou sem alarme
            FormHelper.setTextBoxValue(this, R.id.med_info_alarm, sessionMedication.isHasAlarm() ? "Com alarme" : "Sem alarme");

            // horarios
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            String hours = timeFormat.format(sessionMedication.getStartDate());
            // calculando resto das horas
            Calendar c = Calendar.getInstance();
            c.setTime(sessionMedication.getStartDate());

            switch (sessionMedication.getPeriodicity()) {
                case DIAx2:
                    c.add(Calendar.HOUR, 12);
                    sessionMedication.setStartDate(c.getTime());
                    hours += ", " + timeFormat.format(sessionMedication.getStartDate());
                    break;
                case DIAx3:
                    c.add(Calendar.HOUR, 8);
                    sessionMedication.setStartDate(c.getTime());
                    hours += ", " + timeFormat.format(sessionMedication.getStartDate());
                    c.add(Calendar.HOUR, 8);
                    sessionMedication.setStartDate(c.getTime());
                    hours += ", " + timeFormat.format(sessionMedication.getStartDate());
                    break;
                case DIAx4:
                    c.add(Calendar.HOUR, 6);
                    sessionMedication.setStartDate(c.getTime());
                    hours += ", " + timeFormat.format(sessionMedication.getStartDate());
                    c.add(Calendar.HOUR, 6);
                    sessionMedication.setStartDate(c.getTime());
                    hours += ", " + timeFormat.format(sessionMedication.getStartDate());
                    c.add(Calendar.HOUR, 6);
                    sessionMedication.setStartDate(c.getTime());
                    hours += ", " + timeFormat.format(sessionMedication.getStartDate());
                    break;
            }

            FormHelper.setTextBoxValue(this, R.id.med_info_timing, hours);
        }
    }


    private void showAlarmDialog(long alertId) {

        // stopping alarm - if any
        this.stopService(new Intent(this, AlarmPlayerService.class));

        if (sessionMedication != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            // montando dialog
            builder.setTitle("O que você deseja fazer?")
                    .setMessage("Tomar " + sessionMedication.getName())
                    .setPositiveButton("Tomar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            // Logar que tomou... enviar a cloud
                            addReportEntryAndCancelAlarm(ReportResponseType.TOMAR);
                        }
                    })
                    .setNeutralButton("Pular", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            // O usuário explicitamente deseja pular essa medicacao
                            addReportEntryAndCancelAlarm(ReportResponseType.PULAR);
                            dialog.cancel();
                        }
                    })
                    .setNegativeButton("Ignorar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // O usuário deseja ignorar
                            addReportEntryAndCancelAlarm(ReportResponseType.ADIAR);
                        }
                    }).create().show();

            // removing bundle info
            getIntent().removeExtra(NotificationEventService.BUNDLE_ALERT_ID);
        }
    }

    private void addReportEntryAndCancelAlarm(ReportResponseType type) {
        AlertEvent event = AlertEventDAL.getInstance(this).findOne(alertId);
        if(event != null) {
            AlertEventReportEntryDAL db = AlertEventReportEntryDAL.getInstance(this);

            AlertEventReportEntry report = new AlertEventReportEntry();
            report.setEvent(event.getEvent());
            report.setEntityId(event.getEntityId());
            report.setEntityClass(event.getEntityClass());
            report.setResponse(type);
            report.setReportDate(new Date());

            db.create(report);
            Log.i("Med Info", "# alarms = " + db.count());
        }
    }

}
