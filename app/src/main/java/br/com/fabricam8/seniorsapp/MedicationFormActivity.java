package br.com.fabricam8.seniorsapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import br.com.fabricam8.seniorsapp.alarm.NotificationEventService;
import br.com.fabricam8.seniorsapp.dal.AlertEventDAL;
import br.com.fabricam8.seniorsapp.dal.MedicationDAL;
import br.com.fabricam8.seniorsapp.domain.AlertEvent;
import br.com.fabricam8.seniorsapp.domain.Medication;
import br.com.fabricam8.seniorsapp.enums.Dosage;
import br.com.fabricam8.seniorsapp.util.FormHelper;
import br.com.fabricam8.seniorsapp.util.ToolbarBuilder;


public class MedicationFormActivity extends ActionBarActivity
        implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    private Calendar selectedDate;
    private boolean mFirstCalendarCal = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication_form);

        // create toolbar
        ToolbarBuilder.build(this, true);

        // setting up dosage array
        Spinner spnDosage = (Spinner) findViewById(R.id.spnMedDosageType);
        spnDosage.setAdapter(new CustomAdapter<>(this, R.layout.custom_spinner, Dosage.values()));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_medication_form, menu);
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

    /**
     * Event chamado quando checkbox de uso contínuo é clicado.
     * <p>
     * Controla a habilitação ou desabilitação do campo de duração.
     * </p>
     */
    public void toggleIsContinuos(View v) {
        boolean fIsContinuous = FormHelper.getCheckBoxValue(this, R.id.cbMed_ContUse);
        if (fIsContinuous) {
            findViewById(R.id.txtMed_Duration).setEnabled(false);
            FormHelper.setTextBoxValue(this, R.id.txtMed_Duration, "");
        } else {
            findViewById(R.id.txtMed_Duration).setEnabled(true);
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

    public void saveMedication(View v) {
        Context context = this;

        try {
            MedicationDAL db = MedicationDAL.getInstance(context);

            if (validateForm()) {

                Medication med = new Medication();
                med.setName(FormHelper.getTextBoxValue(this, R.id.txtMed_Name));

                med.setDosageType(((Dosage) ((Spinner) findViewById(R.id.spnMedDosageType)).getSelectedItem()));
                med.setDosage(FormHelper.getTextBoxValueAsInt(this, R.id.txtMed_Dosage));
                med.setPeriodicity(FormHelper.getTextBoxValueAsInt(this, R.id.txtMed_Repetition));
                med.setStartDate(selectedDate.getTime());

                med.setContinuosUse(FormHelper.getCheckBoxValue(this, R.id.cbMed_ContUse));
                if (!med.isContinuosUse()) {
                    med.setDuration(FormHelper.getTextBoxValueAsInt(this, R.id.txtMed_Duration));
                } else {
                    med.setDuration(-1);
                }

                // creating medication
                long id = db.create(med);

                if (id > 0) {
                    // criando alarme
                    AlertEvent alert = new AlertEvent();
                    alert.setEntityId(id);
                    alert.setEntityClass(Medication.class.getName());
                    alert.setEvent(med.getName());
                    // setando numero de alarmes
                    int numAlarms = med.getNumOfAlarms() > 0 ? med.getNumOfAlarms() : 1;
                    alert.setMaxAlarms(numAlarms);
                    alert.setAlarmsPlayed(0);
                    alert.setNextAlert(med.getStartDate());

                    AlertEventDAL dbAlert = AlertEventDAL.getInstance(context);
                    long alertId = dbAlert.create(alert);

                    // setando id para enviar pra servico de alarme
                    alert.setID(alertId);

                    // setando alarme
                    NotificationEventService.setupAlarm(this, alert);

                    Toast.makeText(this, getString(R.string.success_form_submit), Toast.LENGTH_LONG).show();
                    Thread.sleep(1500);
                    finish();
                } else {
                    Toast.makeText(this, getString(R.string.error_form_submit), Toast.LENGTH_LONG).show();
                }
            }
        } catch (Exception ex) {
            Toast.makeText(this, getString(R.string.error_form_submit), Toast.LENGTH_LONG).show();
        }

    }

    /**
     * Efetua a validação do formulário.
     *
     * @return Retorna falso se algo está errado com o formulário e indica erro na tela.
     */
    private boolean validateForm() {
        // validating name
        if (!FormHelper.validateFormTextInput(this, R.id.txtMed_Name, getString(R.string.validation_error_message)))
            return false;

        // validating Dosage
        if (!FormHelper.validateFormTextInput(this, R.id.txtMed_Dosage, getString(R.string.validation_error_message)))
            return false;

        // validating Repetition
        if (!FormHelper.validateFormTextInput(this, R.id.txtMed_Repetition, getString(R.string.validation_error_message)))
            return false;

        // validating spinner
        if (!validateSpinner())
            return false;

        // validating duration
        if (!FormHelper.getCheckBoxValue(this, R.id.cbMed_ContUse) &&
                !FormHelper.validateFormTextInput(this, R.id.txtMed_Duration, getString(R.string.validation_error_message))) {
            return false;
        }

        // validating start date
        if (!FormHelper.validateFormTextInput(this, R.id.txtMed_StartTime, getString(R.string.validation_error_message)))
            return false;

        return true;
    }

    /**
     * Efetua a validação do combo box.
     *
     * @return Retorna falso se combo não tiver sido selecionado.
     */
    private boolean validateSpinner() {
        Spinner spn = (Spinner) findViewById(R.id.spnMedDosageType);
        if (spn.getSelectedItemId() <= 0) {
            spn.setFocusableInTouchMode(true);
            spn.requestFocus();

            CustomAdapter adapter = (CustomAdapter) spn.getAdapter();
            View view = spn.getSelectedView();
            adapter.setError(view, getString(R.string.validation_error_message));
            return false;
        }
        return true;
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "Selecione a data");
    }

    /**
     * Evento chamado após seleção da data. Esse evento também abre o dialog de seleção da hora.
     */
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        if (mFirstCalendarCal) {
            mFirstCalendarCal = false;

            this.selectedDate = Calendar.getInstance();
            this.selectedDate.set(year, monthOfYear, dayOfMonth);

            DialogFragment newFragment = new TimePickerFragment();
            newFragment.show(getFragmentManager(), "Selecione a hora");
        }
    }

    /**
     * Evento chamado após seleção de tempo.
     */
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        this.selectedDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
        this.selectedDate.set(Calendar.MINUTE, minute);

        SimpleDateFormat target = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        FormHelper.setTextBoxValue(this, R.id.txtMed_StartTime, target.format(this.selectedDate.getTime()));
        mFirstCalendarCal = true; // rehabilitando campo
    }


    // // // // // // // // // // // // // // // // // // // // // // // // //
    // FRAGMENTS - utilizados para mostrar dialógos de data e hora
    // // // // // // // // // // // // // // // // // // // // // // // // //

    public static class TimePickerFragment extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), (MedicationFormActivity) getActivity(), hour,
                    minute, DateFormat.is24HourFormat(getActivity()));
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
            return new DatePickerDialog(getActivity(), (MedicationFormActivity) getActivity(), year,
                    month, day);
        }
    }

}
