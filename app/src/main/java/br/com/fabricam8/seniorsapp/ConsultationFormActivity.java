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

import br.com.fabricam8.seniorsapp.dal.ConsultationDAL;
import br.com.fabricam8.seniorsapp.domain.Consultation;
import br.com.fabricam8.seniorsapp.enums.ConsultationType;
import br.com.fabricam8.seniorsapp.util.FormHelper;
import br.com.fabricam8.seniorsapp.util.ToolbarBuilder;

public class ConsultationFormActivity extends ActionBarActivity
        implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener
{

    private Consultation sessionConsultation;
    private int dialogCaller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultation_form);

        // create toolbar
        ToolbarBuilder.build(this, true);

        // recuperando id passada no clique
        long consultationId = getIntent().getLongExtra("_ID_", -1);
        if (consultationId == -1) {
            this.sessionConsultation = initConsultation();
        } else {
            this.sessionConsultation = ConsultationDAL.getInstance(this).findOne(consultationId);
        }
        // atulizando a view de Consulta
        updateConsultationView();
    }

    private Consultation initConsultation() {
        Consultation eObj = new Consultation();

        Calendar c = Calendar.getInstance();

        eObj.setConsultation_type(ConsultationType.ANDAR);
        eObj.setStartDate(c.getTime());
       // eObj.setEndDate(c.getTime());
        //eObj.setDescription(c.getDescription());

        // setar o resto dos atributos

        return eObj;
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

    private void updateConsultationView() {

        FormHelper.setTextBoxValue(this, R.id.exc_form_type, sessionConsultation.getName().toString());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
        FormHelper.setTextBoxValue(this, R.id.exc_form_startingt, dateFormat.format(sessionConsultation.getStartDate()));
        //FormHelper.setTextBoxValue(this, R.id.exc_form_dateand, dateFormat.format(sessionConsultation.getEndDate()));
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        FormHelper.setTextBoxValue(this, R.id.exc_form_time, timeFormat.format(sessionConsultation.getStartDate()));
    }

    public void openDialogMeasureActivities(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();
        // Pass null as the parent view because its going in the dialog layout
        final View dialogView = inflater.inflate(R.layout.dialog_exerc_measure, null);

        String[] arrValues = ConsultationType.getStringValues();
        FormHelper.setupPicker(dialogView, R.id.dg_exerc_measure, 0, arrValues.length - 1, arrValues, 0);

        // montando dialog
        builder.setTitle("Escolha uma atividade")
                .setView(dialogView)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        int measure = FormHelper.getPickerValue(dialogView, R.id.dg_exerc_measure);
                        sessionConsultation.setConsultation_type(ConsultationType.fromInt(measure + 1));
                        updateConsultationView();
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
        dialogCaller = v.getId();

    }

    public void openTimePickerDialogActivity(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "Selecione a hora");
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar c = Calendar.getInstance();
        c.setTime(sessionConsultation.getStartDate());
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        // reajustando dat ade inicio
        sessionConsultation.setStartDate(c.getTime());
        updateConsultationView();
    }

    public static class TimePickerFragment extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), (ConsultationFormActivity) getActivity(), 6,
                    30, DateFormat.is24HourFormat(getActivity()));
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, monthOfYear);
        c.set(Calendar.DATE, dayOfMonth);
       if(dialogCaller == R.id.exc_form_startingt)
        {
            sessionConsultation.setStartDate(c.getTime());
        }

        else if(dialogCaller == R.id.exc_form_dateand)
        {
            sessionConsultation.setStartDate(c.getTime());
        }
        updateConsultationView();
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
            return new DatePickerDialog(getActivity(), (ConsultationFormActivity) getActivity(), year,
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

    public void saveConsultation(View v) {
        try {
            Context context = this;
            // TODO verificar salvamento na activity de Activity
            ConsultationDAL dbExc = ConsultationDAL.getInstance(this);
            long id = -1;
            if (sessionConsultation.getID() > 0)
            {
                id = sessionConsultation.getID();
                // atualizacao de dados
                dbExc.update(sessionConsultation);
            }
            else
            {
                id = dbExc.create(sessionConsultation);
            }

            if (id > 0)
            {
                // TODO salvar alarme (de acordo) com modelo em MedicationFormActivity

                Toast.makeText(this, "A consulta foi cadastrada com sucesso.", Toast.LENGTH_LONG).show();
                finish(); // finalizando activty e retornando para tela anterior
            } else
            {
                // TODO remover alarme (se existir) ?!!
                Toast.makeText(this, "Ocorreu uma falha e a consulta não pode ser cadastrada.", Toast.LENGTH_LONG).show();
            }
        } catch (Exception ex)
        {
            Log.e("Seniors App - Consultas", ex.getMessage());
            Toast.makeText(this, "Ocorreu um erro e a consulta não pode ser cadastrada.", Toast.LENGTH_LONG).show();
        }
    }

}
