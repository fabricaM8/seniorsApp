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

import br.com.fabricam8.seniorsapp.alarm.NotificationEventService;
import br.com.fabricam8.seniorsapp.dal.AlertEventDAL;
import br.com.fabricam8.seniorsapp.dal.MedicationDAL;
import br.com.fabricam8.seniorsapp.domain.AlertEvent;
import br.com.fabricam8.seniorsapp.domain.Medication;
import br.com.fabricam8.seniorsapp.util.FormHelper;
import br.com.fabricam8.seniorsapp.util.ToolbarBuilder;


public class MedicationInfoActivity extends ActionBarActivity {

    public static final String BUNDLE_ID = "_ID_";
    private long medicationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication_info);

        // create toolbar
        Toolbar toolbar = ToolbarBuilder.build(this, true);
        toolbar.setTitle("Informações");
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

        long alertId = getIntent().getLongExtra(NotificationEventService.BUNDLE_ALERT_ID, -1);
        if(alertId != -1) {
            // mostrar botoes de pular e salvar
            Toast.makeText(this, "BOTOES DE PULAR / TOMAR / CANCELAR SERAO MOSTRADOS AQUI", Toast.LENGTH_LONG);
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
                .setMessage("Deseja realmente deletar medicação?")
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
        Medication mObj = db.findOne(id);
        if (mObj != null) {
            // setando valores
            FormHelper.setTextBoxValue(this, R.id.med_info_name, mObj.getName());
            FormHelper.setTextBoxValue(this, R.id.med_info_dosage, "Tomar " + mObj.getDosage() + " " + mObj.getDosageMeasureType().toString());
            FormHelper.setTextBoxValue(this, R.id.med_info_periodicity, mObj.getPeriodicity().toString());

            if (mObj.isContinuosUse())
                FormHelper.setTextBoxValue(this, R.id.med_info_duration, "Uso contínuo");
            else
                FormHelper.setTextBoxValue(this, R.id.med_info_duration, "Por " + mObj.getDuration() + " " + mObj.getDurationType().toString());

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
            FormHelper.setTextBoxValue(this, R.id.med_info_starting, "A partir de " + dateFormat.format(mObj.getStartDate()));
            FormHelper.setTextBoxValue(this, R.id.med_info_observations, mObj.getDescription());

            // com ou sem alarme
            FormHelper.setTextBoxValue(this, R.id.med_info_alarm, mObj.isHasAlarm() ? "Com alarme" : "Sem alarme");

            // horarios
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            String hours = timeFormat.format(mObj.getStartDate());
            // calculando resto das horas
            Calendar c = Calendar.getInstance();
            c.setTime(mObj.getStartDate());

            switch (mObj.getPeriodicity()) {
                case DIAx2:
                    c.add(Calendar.HOUR, 12);
                    mObj.setStartDate(c.getTime());
                    hours += ", " + timeFormat.format(mObj.getStartDate());
                    break;
                case DIAx3:
                    c.add(Calendar.HOUR, 8);
                    mObj.setStartDate(c.getTime());
                    hours += ", " + timeFormat.format(mObj.getStartDate());
                    c.add(Calendar.HOUR, 8);
                    mObj.setStartDate(c.getTime());
                    hours += ", " + timeFormat.format(mObj.getStartDate());
                    break;
                case DIAx4:
                    c.add(Calendar.HOUR, 6);
                    mObj.setStartDate(c.getTime());
                    hours += ", " + timeFormat.format(mObj.getStartDate());
                    c.add(Calendar.HOUR, 6);
                    mObj.setStartDate(c.getTime());
                    hours += ", " + timeFormat.format(mObj.getStartDate());
                    c.add(Calendar.HOUR, 6);
                    mObj.setStartDate(c.getTime());
                    hours += ", " + timeFormat.format(mObj.getStartDate());
                    break;
            }

            FormHelper.setTextBoxValue(this, R.id.med_info_timing, hours);
        }
    }
}
