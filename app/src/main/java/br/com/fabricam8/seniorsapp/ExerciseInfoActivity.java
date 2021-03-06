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
import br.com.fabricam8.seniorsapp.dal.ExerciseDAL;
import br.com.fabricam8.seniorsapp.domain.AlertEvent;
import br.com.fabricam8.seniorsapp.domain.Exercise;
import br.com.fabricam8.seniorsapp.util.FormHelper;
import br.com.fabricam8.seniorsapp.util.ToolbarBuilder;


public class ExerciseInfoActivity extends ActionBarActivity {

    public static final String BUNDLE_ID = "_ID_";
    private long exerciseId;
    private long alertId;
    private Exercise sessionExercise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_info);

        // create toolbar
        Toolbar mToolbar = ToolbarBuilder.build(this, true);
        mToolbar.setBackgroundColor(getResources().getColor(R.color.seniors_active_dash_button_color_navy));
        mToolbar.setTitle("Informações");
    }

    @Override
    protected void onResume() {
        super.onResume();

        // recuperando id passada no clique
        exerciseId = getIntent().getLongExtra(BUNDLE_ID, -1);
        if (exerciseId == -1) {
            finish(); // se nao enviar bundle, sai do activity
        } else {
            loadExercise(exerciseId);
        }
    }

    public void editExercise(View view) {
        Intent intent = new Intent(this, ExerciseFormActivity.class);
        intent.putExtra("_ID_", exerciseId);
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
                .setMessage("Deseja realmente excluir a atividade física?")
                .setIcon(R.drawable.ic_action_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (exerciseId > 0) {
                            ExerciseDAL db = ExerciseDAL.getInstance(_this);
                            Exercise mObj = db.findOne(exerciseId);
                            long objId = mObj.getID();
                            if (db.remove(mObj) > 0) {
                                // agora remove alarme, se tiver algum cadastrado
                                AlertEventDAL alertDb = AlertEventDAL.getInstance(_this);
                                AlertEvent alert = alertDb.findOneByEntityIdAndType(objId, Exercise.class.getName());
                                if (alert != null) {
                                    Log.i("Seniors - Exercise Info", "Removendo atividade");
                                    alertDb.remove(alert);
                                }

                                Toast.makeText(_this, getString(R.string.success_exercise_deletion), Toast.LENGTH_LONG).show();
                                finish();
                            } else {
                                Toast.makeText(_this, getString(R.string.fail_exercise_deletion), Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }


    private void loadExercise(long id) {
        ExerciseDAL db = ExerciseDAL.getInstance(this);
        sessionExercise = db.findOne(id);
        if (sessionExercise != null) {
            // setando valores
           FormHelper.setTextBoxValue(this, R.id.exe_info_name, sessionExercise.getName());

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
            FormHelper.setTextBoxValue(this, R.id.exe_info_start_date, "A partir de " + dateFormat.format(sessionExercise.getStartDate()));

            if(sessionExercise.getEndDate() != null &&
                    sessionExercise.getEndDate().compareTo(sessionExercise.getStartDate()) == 1)
                FormHelper.setTextBoxValue(this, R.id.exe_info_end_date, "Finalizar em " + dateFormat.format(sessionExercise.getEndDate()));

            // horarios
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            String hours = timeFormat.format(sessionExercise.getStartDate());
            FormHelper.setTextBoxValue(this, R.id.exe_info_horario, hours);

            String daysOfWeek = "";
            daysOfWeek += sessionExercise.isRepeatOnSunday() ? "Dom, " : "";
            daysOfWeek += sessionExercise.isRepeatOnMonday() ? "Seg, " : "";
            daysOfWeek += sessionExercise.isRepeatOnTuesday() ? "Ter, " : "";
            daysOfWeek += sessionExercise.isRepeatOnWednesday() ? "Qua, " : "";
            daysOfWeek += sessionExercise.isRepeatOnThursday() ? "Qui, " : "";
            daysOfWeek += sessionExercise.isRepeatOnFriday() ? "Sex, " : "";
            daysOfWeek += sessionExercise.isRepeatOnSaturday() ? "Sab," : "";

            // removendo ultima virgula
            if(daysOfWeek.length() > 0)
                daysOfWeek = daysOfWeek.substring(0, daysOfWeek.lastIndexOf(","));

            FormHelper.setTextBoxValue(this, R.id.exe_info_days, daysOfWeek);

        }
    }
}
