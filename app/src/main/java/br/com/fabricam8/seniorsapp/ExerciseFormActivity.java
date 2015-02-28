package br.com.fabricam8.seniorsapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import br.com.fabricam8.seniorsapp.dal.ExerciseDAL;
import br.com.fabricam8.seniorsapp.domain.Exercise;
import br.com.fabricam8.seniorsapp.enums.ExerciseType;
import br.com.fabricam8.seniorsapp.util.FormHelper;

public class ExerciseFormActivity extends ActionBarActivity
        implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener
{

    private Exercise sessionExercise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_form);

        // recuperando id passada no clique
        long exerciseId = getIntent().getLongExtra("_ID_", -1);
        if (exerciseId == -1) {
            this.sessionExercise = initExercise();
        } else {
            //this.sessionExercise = ExerciseDAL.getInstance(this).findOne(exerciseId);
        }
        // atulizando a view de medicamento
        updateExerciseView();
    }

    private Exercise initExercise() {
        Exercise eObj = new Exercise();

        Calendar c = Calendar.getInstance();

        eObj.setType(ExerciseType.ANDAR);
        eObj.setStartDate(c.getTime());
        eObj.setEndDate(c.getTime());
        //eObj.setDescription(c.getDescription());

        // setar o resto dos atributos

        return eObj;
    }

    private void updateExerciseView() {

        FormHelper.setTextBoxValue(this, R.id.exc_form_type, sessionExercise.getMeasureType().toString());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
        FormHelper.setTextBoxValue(this, R.id.exc_form_startingt, dateFormat.format(sessionExercise.getStartDate()));

        /*
        FormHelper.setTextBoxValue(this, R.id.exc_form_dateand, dateFormat.format(sessionExercise.getEndDate()));

        FormHelper.setTextBoxValue(this, R.id.exc_form_type, sessionExercise.getType().toString());
    */
    }

    public void openDialogMeasureActivities(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();
        // Pass null as the parent view because its going in the dialog layout
        final View dialogView = inflater.inflate(R.layout.dialog_exerc_measure, null);

        String[] arrValues = ExerciseType.getStringValues();
        FormHelper.setupPicker(dialogView, R.id.dg_exerc_measure, 0, arrValues.length - 1, arrValues, 0);

        // montando dialog
        builder.setTitle("Escolha uma atividade")
                .setView(dialogView)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        int measure = FormHelper.getPickerValue(dialogView, R.id.dg_exerc_measure);
                        sessionExercise.setType(ExerciseType.fromInt(measure + 1));
                        updateExerciseView();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }).create().show();
    }


    public void openDatePickerDialogActivity(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "Selecione a data Inicial");

    }


    public void openTimePickerDialogActivity(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "Selecione a hora");
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

    }

    public static class TimePickerFragment extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), (ExerciseFormActivity) getActivity(), 6,
                    30, DateFormat.is24HourFormat(getActivity()));
        }
    }


    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        sessionExercise.setStartDate(c.getTime());
        updateExerciseView();
    }

    public static class DatePickerFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), (ExerciseFormActivity) getActivity(), year,
                    month, day);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_form, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void saveExercise(View v) {
        try {
            Context context = this;
            // TODO verificar salvamento na activity de Activity
            ExerciseDAL dbExc = ExerciseDAL.getInstance(this);
            long id = -1;
            if (sessionExercise.getID() > 0)
            {
                // atualizacao de dados
                dbExc.update(sessionExercise);
            }
            else
            {
                id = dbExc.create(sessionExercise);
            }


            if (id > 0)
            {
                // TODO salvar alarme (de acordo) com modelo em MedicationFormActivity

                Toast.makeText(this, "A atividade foi cadastrada com sucesso.", Toast.LENGTH_LONG).show();
                finish(); // finalizando activty e retornando para tela anterior
            } else
            {
                // TODO remover alarme (se existir) ?!!
                Toast.makeText(this, "Ocorreu uma falha e a ativiadade não pode ser cadastrada.", Toast.LENGTH_LONG).show();
            }
        } catch (Exception ex)
        {
            Log.e("Seniors App - Exercicio", ex.getMessage());
            Toast.makeText(this, "Ocorreu um erro e a ativiadade não pode ser cadastrada.", Toast.LENGTH_LONG).show();
        }
    }


}
