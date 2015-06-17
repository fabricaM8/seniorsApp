package br.com.fabricam8.seniorsapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import br.com.fabricam8.seniorsapp.alarm.NotificationEventService;
import br.com.fabricam8.seniorsapp.dal.AlertEventDAL;
import br.com.fabricam8.seniorsapp.dal.ExerciseDAL;
import br.com.fabricam8.seniorsapp.domain.AlertEvent;
import br.com.fabricam8.seniorsapp.domain.Exercise;
import br.com.fabricam8.seniorsapp.domain.Medication;
import br.com.fabricam8.seniorsapp.util.FormHelper;
import br.com.fabricam8.seniorsapp.util.ToolbarBuilder;

public class ExerciseFormActivity extends ActionBarActivity
        implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener
{

    private Exercise sessionExercise;
    private int dialogCaller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_form);

        // create toolbar
        Toolbar mToolbar = ToolbarBuilder.build(this, true);
        mToolbar.setBackgroundColor(getResources().getColor(R.color.seniors_active_dash_button_color_navy));

        // adicionando edit listeners aos campos de texto
        addTextChangeListeners();

        // recuperando id passada no clique
        long exerciseId = getIntent().getLongExtra("_ID_", -1);
        if (exerciseId == -1)
        {
            this.sessionExercise = initExercise();
        } else
        {
            this.sessionExercise = ExerciseDAL.getInstance(this).findOne(exerciseId);
        }
        // atulizando a view de atividades
       updateExerciseView(1);
    }


    private Exercise initExercise()
    {
        Exercise eObj = new Exercise();

        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, new Date().getHours() + 1);
        c.set(Calendar.MINUTE, 0);
        eObj.setStartDate(c.getTime());
        eObj.setEndDate(null);

        // setar o resto dos atributos
        return eObj;
    }

    private void addTextChangeListeners()
    {
        EditText txtName = (EditText) findViewById(R.id.exercise_type);
        txtName.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                sessionExercise.setName(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    public void onChecked(View v) {
        switch (v.getId()) {
            case R.id.cb_Sun :
                sessionExercise.setRepeatOnSunday(((CheckBox)v).isChecked());
                break;
            case R.id.cb_Mon :
                sessionExercise.setRepeatOnMonday(((CheckBox) v).isChecked());
                break;
            case R.id.cb_Tue :
                sessionExercise.setRepeatOnTuesday(((CheckBox) v).isChecked());
                break;
            case R.id.cb_Wed :
                sessionExercise.setRepeatOnWednesday(((CheckBox) v).isChecked());
                break;
            case R.id.cb_Thu :
                sessionExercise.setRepeatOnThursday(((CheckBox) v).isChecked());
                break;
            case R.id.cb_Fri :
                sessionExercise.setRepeatOnFriday(((CheckBox) v).isChecked());
                break;
            case R.id.cb_Sat :
                sessionExercise.setRepeatOnSaturday(((CheckBox) v).isChecked());
                break;
            default :
                break;
        }
    }


    /**
     * Evento chamado quando botão cancelar é apertado.
     * <p>
     * Finaliza a Activity e retorna para tela anterior.
     * </p>
     */
    public void cancel(View v) {
        // end activity
        finish();
    }

    private void updateExerciseView(int valor) {
        FormHelper.setTextBoxValue(this, R.id.exercise_type, sessionExercise.getName());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        FormHelper.setTextBoxValue(this, R.id.exc_form_start_date, dateFormat.format(sessionExercise.getStartDate()));
        FormHelper.setTextBoxValue(this, R.id.exc_form_time, timeFormat.format(sessionExercise.getStartDate()));
        if (valor != 1) {
            FormHelper.setTextBoxValue(this, R.id.exc_form_date_end, dateFormat.format(sessionExercise.getEndDate()));
        }

        FormHelper.setCheckboxValue(this, R.id.cb_Sun, sessionExercise.isRepeatOnSunday());
        FormHelper.setCheckboxValue(this, R.id.cb_Mon, sessionExercise.isRepeatOnMonday());
        FormHelper.setCheckboxValue(this, R.id.cb_Tue, sessionExercise.isRepeatOnTuesday());
        FormHelper.setCheckboxValue(this, R.id.cb_Wed, sessionExercise.isRepeatOnWednesday());
        FormHelper.setCheckboxValue(this, R.id.cb_Thu, sessionExercise.isRepeatOnThursday());
        FormHelper.setCheckboxValue(this, R.id.cb_Fri, sessionExercise.isRepeatOnFriday());
        FormHelper.setCheckboxValue(this, R.id.cb_Sat, sessionExercise.isRepeatOnSaturday());
    }

    public void openDatePickerDialogActivity(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "Selecione a data inicial");
        dialogCaller = v.getId();
    }

    private boolean validateForm() {

        Calendar c2 = Calendar.getInstance();
        Date datatual = c2.getInstance().getTime();

        if (!FormHelper.validateFormTextInput(this, R.id.exercise_type, getString(R.string.validation_error_message))) {
            return false;
        }
        else if((diffInDays(sessionExercise.getStartDate(),c2.getTime()))<0)
        {
            showAlert("Alerta", "Data inicial não pode ser menor do que data atual.");
            return false;
        }

        else if((diffInDays(sessionExercise.getEndDate(),sessionExercise.getStartDate()))<0)
        {

            showAlert("Alerta", "Data final não pode ser menor do que data inicial.");
            return false;
        }
        return true;
    }
    private void showAlert(String title, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // montando dialog
        builder.setTitle(title)
                .setMessage(msg)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }).create().show();
    }
    public static int diffInDays(Date d1, Date d2) {
        int MILLIS_IN_DAY = 86400000;

        Calendar c1 = Calendar.getInstance();
        c1.setTime(d1);
        c1.set(Calendar.MILLISECOND, 0);
        c1.set(Calendar.SECOND, 0);
        c1.set(Calendar.MINUTE, 0);
        c1.set(Calendar.HOUR_OF_DAY, 0);

        Calendar c2 = Calendar.getInstance();
        c2.setTime(d2);
        c2.set(Calendar.MILLISECOND, 0);
        c2.set(Calendar.SECOND, 0);
        c2.set(Calendar.MINUTE, 0);
        c2.set(Calendar.HOUR_OF_DAY, 0);
        return (int) ((c1.getTimeInMillis() - c2.getTimeInMillis()) / MILLIS_IN_DAY);
    }
    public void openTimePickerDialogActivity(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "Selecione a hora");
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar c = Calendar.getInstance();
        c.setTime(sessionExercise.getStartDate());
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);

        sessionExercise.setStartDate(c.getTime());
        // reajustando hora ade inicio
        updateExerciseView(1);
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
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, monthOfYear);
        c.set(Calendar.DATE, dayOfMonth);
        if(dialogCaller == R.id.exc_form_start_date)
        {
            sessionExercise.setStartDate(c.getTime());
            updateExerciseView(1);
        }
        else if(dialogCaller == R.id.exc_form_date_end)
        {
            sessionExercise.setEndDate(c.getTime());
            updateExerciseView(0);
        }

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
        switch(item.getItemId()) {
            case android.R.id.home:
            case R.id.action_cancel_medication:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.action_save_medication:
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return super.onOptionsItemSelected(item);
    }

    public void saveExercise(View v) {
        try {
            Context context = this;
            // TODO verificar salvamento na activity de Activity
            ExerciseDAL dbExc = ExerciseDAL.getInstance(this);
            if (validateForm())
            {

                long id = -1;
                if (sessionExercise.getID() > 0)
                {
                    id = sessionExercise.getID();
                    // atualizacao de dados
                    dbExc.update(sessionExercise);
                } else {
                    id = dbExc.create(sessionExercise);
                }

                if (id > 0) {
                    AlertEventDAL dbAlert = AlertEventDAL.getInstance(context);
                    // verificando se ja existe alarme para essa entidade
                    AlertEvent alert = dbAlert.findOneByEntityIdAndType(id, Exercise.class.getName());
                    if(alert == null) {
                        alert = new AlertEvent();
                        alert.setEntityId(id);
                        alert.setEntityClass(Exercise.class.getName());
                    }

                    alert.setEvent(sessionExercise.getName());
                    // setando numero de alarmes
                    int numAlarms = sessionExercise.getNumOfAlarms();
                    alert.setMaxAlarms(numAlarms);
                    alert.setAlarmsPlayed(0);

                    Calendar c = Calendar.getInstance();
                    c.setTime(sessionExercise.getStartDate());
                    c.add(Calendar.HOUR_OF_DAY, -1);
                    c.set(Calendar.MINUTE, 0);

                    alert.setNextAlert(c.getTime());

                    long alertId = dbAlert.create(alert);

                    // setando id para enviar pra servico de alarme
                    alert.setID(alertId);

                    // setando alarme
                    NotificationEventService.setupAlarm(this, alert);

                    Toast.makeText(this, "A atividade foi cadastrada com sucesso.", Toast.LENGTH_LONG).show();
                    finish(); // finalizando activty e retornando para tela anterior
                }
                else {
                    Toast.makeText(this, "Ocorreu uma falha e a atividade não pode ser cadastrada.", Toast.LENGTH_LONG).show();
                }
            }
        } catch (Exception ex)
        {
            Log.e("SeniorsApp - Atividades", ex.getMessage());
            Toast.makeText(this, "Ocorreu um erro e a atividade não pode ser cadastrada.", Toast.LENGTH_LONG).show();
        }
    }

}
