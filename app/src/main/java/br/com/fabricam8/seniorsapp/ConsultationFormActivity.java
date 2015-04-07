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
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import br.com.fabricam8.seniorsapp.dal.ConsultationDAL;
import br.com.fabricam8.seniorsapp.domain.Consultation;
import br.com.fabricam8.seniorsapp.enums.ReminderType;
import br.com.fabricam8.seniorsapp.util.FormHelper;
import br.com.fabricam8.seniorsapp.util.ToolbarBuilder;

public class ConsultationFormActivity extends ActionBarActivity
        implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private Consultation sessionConsultation;
    private int dialogCaller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultation_form);

        // create toolbar
        Toolbar mToolbar = ToolbarBuilder.build(this, true);
        mToolbar.setBackgroundColor(getResources().getColor(R.color.seniors_active_dash_button_color_navy));

        // adicionando edit listeners aos campos de texto
        addTextChangeListeners();

        // recuperando id passada no clique
        long consultaId = getIntent().getLongExtra("_ID_", -1);
        if (consultaId == -1)
        {
            this.sessionConsultation = initConsultation();
        } else {
            this.sessionConsultation = ConsultationDAL.getInstance(this).findOne(consultaId);
        }
        // atulizando a view de atividades
        updateConsultationView();
    }

    private Consultation initConsultation() {
        Consultation eObj = new Consultation();

        Calendar c = Calendar.getInstance();

        eObj.setReminderType(ReminderType.TRES_DIAS_ANTES);
        eObj.setStartDate(c.getTime());

        return eObj;
    }
    private void addTextChangeListeners() {
        EditText txtName = (EditText)findViewById(R.id.nome_medico);
        txtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                sessionConsultation.setName(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        EditText txtObserv = (EditText)findViewById(R.id.detalhe);
        txtObserv.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                sessionConsultation.setDetails(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }


    private void updateConsultationView() {
        FormHelper.setTextBoxValue(this, R.id.nome_medico, sessionConsultation.getName());
        FormHelper.setTextBoxValue(this, R.id.detalhe, sessionConsultation.getDetails());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
        FormHelper.setTextBoxValue(this, R.id.consultation_date_start, dateFormat.format(sessionConsultation.getStartDate()));
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        FormHelper.setTextBoxValue(this, R.id.consultation_time, timeFormat.format(sessionConsultation.getStartDate()));
        FormHelper.setTextBoxValue(this, R.id.consultation_type, sessionConsultation.getReminderType().toString());
    }


    public void openDialogMeasureActivities(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();
        // Pass null as the parent view because its going in the dialog layout
        final View dialogView = inflater.inflate(R.layout.dialog_consul_measure, null);

        String[] arrValues = ReminderType.getStringValues();
        FormHelper.setupPicker(dialogView, R.id.dg_consul_measure, 0, arrValues.length - 1, arrValues, 0);

        // montando dialog
        builder.setTitle("Escolha uma lembrete")
                .setView(dialogView)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        int measure = FormHelper.getPickerValue(dialogView, R.id.dg_consul_measure);
                        sessionConsultation.setReminderType(ReminderType.fromInt(measure + 1));
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

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, monthOfYear);
        c.set(Calendar.DATE, dayOfMonth);
        sessionConsultation.setStartDate(c.getTime());
        updateConsultationView();
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

    public void openTimePickerDialogActivity(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "Selecione a hora");
    }

    public static class TimePickerFragment extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), (ConsultationFormActivity) getActivity(), 6,
                    30, DateFormat.is24HourFormat(getActivity()));
        }
    }

    public void saveConsultation(View v) {
        try {
            Context context = this;
            // TODO verificar salvamento na activity de Activity
            ConsultationDAL dbCon = ConsultationDAL.getInstance(this);
            long id = -1;
            if (sessionConsultation.getID() > 0)
            {
                id = sessionConsultation.getID();
                //atualizacao de dados
                dbCon.update(sessionConsultation);
            } else
            {
                id = dbCon.create(sessionConsultation);
            }

            if (id > 0) {
                // TODO salvar alarme (de acordo) com modelo em MedicationFormActivity

                Toast.makeText(this, "A consulta foi cadastrada com sucesso.", Toast.LENGTH_LONG).show();
                finish(); // finalizando activty e retornando para tela anterior
            } else {
                // TODO remover alarme (se existir) ?!!
                Toast.makeText(this, "Ocorreu uma falha e a consulta não pode ser cadastrada.", Toast.LENGTH_LONG).show();
            }
        } catch (Exception ex) {
            Log.e("Seniors App - Consulta", ex.getMessage());
            Toast.makeText(this, "Ocorreu um erro e a consulta não pode ser cadastrada.", Toast.LENGTH_LONG).show();
        }
    }

}




