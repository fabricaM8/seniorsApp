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

import br.com.fabricam8.seniorsapp.dal.AlertEventDAL;
import br.com.fabricam8.seniorsapp.dal.ConsultationDAL;
import br.com.fabricam8.seniorsapp.domain.AlertEvent;
import br.com.fabricam8.seniorsapp.domain.Consultation;
import br.com.fabricam8.seniorsapp.util.FormHelper;
import br.com.fabricam8.seniorsapp.util.ToolbarBuilder;


public class ConsultationInfoActivity extends ActionBarActivity {

    public static final String BUNDLE_ID = "_ID_";
    private long consultationId;
    private long alertId;
    private Consultation sessionConsultation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultation_info);

        // create toolbar
        Toolbar toolbar = ToolbarBuilder.build(this, true);
        toolbar.setTitle("Informações");
    }

    @Override
    protected void onResume() {
        super.onResume();

        // recuperando id passada no clique
        consultationId = getIntent().getLongExtra(BUNDLE_ID, -1);
        if (consultationId == -1) {
            finish(); // se nao enviar bundle, sai do activity
        } else {
            loadConsultation(consultationId);
        }
    }

    public void editConsultation(View view) {
        Intent intent = new Intent(this, ConsultationFormActivity.class);
        intent.putExtra("_ID_", consultationId);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_exercise_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.action_md_info_delete:
                deleteExercise();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void deleteExercise() {
        final Activity _this = this;

        new AlertDialog.Builder(this)
                .setTitle("Confirme")
                .setMessage("Deseja realmente deletar a consulta?")
                .setIcon(R.drawable.ic_action_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (consultationId > 0) {
                            ConsultationDAL db = ConsultationDAL.getInstance(_this);
                            Consultation mObj = db.findOne(consultationId);
                            long objId = mObj.getID();
                            if (db.remove(mObj) > 0) {
                                // agora remove alarme, se tiver algum cadastrado
                                AlertEventDAL alertDb = AlertEventDAL.getInstance(_this);
                                AlertEvent alert = alertDb.findOneByEntityIdAndType(objId, Consultation.class.getName());
                                if (alert != null) {
                                    Log.i("Seniors - Exercise Info", "Removendo consulta");
                                    alertDb.remove(alert);
                                }

                                Toast.makeText(_this, getString(R.string.success_consultation_deletion), Toast.LENGTH_LONG).show();
                                finish();
                            } else {
                                Toast.makeText(_this, getString(R.string.fail_consultation_deletion), Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }


    private void loadConsultation(long id) {
        ConsultationDAL db = ConsultationDAL.getInstance(this);
        sessionConsultation = db.findOne(id);
        if (sessionConsultation != null) {

            // setando valores
            FormHelper.setTextBoxValue(this, R.id.exe_info_name, sessionConsultation.getName());

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
            FormHelper.setTextBoxValue(this, R.id.exe_info_start_date, "Iniciar em: " + dateFormat.format(sessionConsultation.getStartDate()));
            //FormHelper.setTextBoxValue(this, R.id.exe_info_end_date, "Finalizar em: " + dateFormat.format(sessionExercise.getEndDate()));
            // horarios
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            String hours = timeFormat.format(sessionConsultation.getStartDate());
            FormHelper.setTextBoxValue(this, R.id.exe_info_horario, hours);

            //SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            //  FormHelper.setTextBoxValue(this, R.id.exe_info_horario, "Iniciar às " + timeFormat.format(sessionExercise.getStartDate()));
//            FormHelper.setTextBoxValue(this, R.id.exe_info_end_horario, "Finalizar às " + timeFormat.format(sessionExercise.getEndDate()));

        }
    }
}
