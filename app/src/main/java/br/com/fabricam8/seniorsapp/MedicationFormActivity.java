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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import br.com.fabricam8.seniorsapp.alarm.NotificationEventService;
import br.com.fabricam8.seniorsapp.dal.AlertEventDAL;
import br.com.fabricam8.seniorsapp.dal.MedicationDAL;
import br.com.fabricam8.seniorsapp.domain.AlertEvent;
import br.com.fabricam8.seniorsapp.domain.Medication;
import br.com.fabricam8.seniorsapp.enums.DosageMeasure;
import br.com.fabricam8.seniorsapp.enums.Duration;
import br.com.fabricam8.seniorsapp.enums.Periodicity;
import br.com.fabricam8.seniorsapp.util.FormHelper;
import br.com.fabricam8.seniorsapp.util.ToolbarBuilder;


public class MedicationFormActivity extends ActionBarActivity
        implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    private static String[] PICKER_QUANTITY_DEC = {
            ",0", ",1", ",2", ",3", ",4", "1/2",
            ",6", ",7", ",9", "1/4", "3/4"
    };

    private Medication sessionMedication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication_form);

        // create toolbar
        ToolbarBuilder.build(this, true);

        // setting up dosage array
//        Spinner spnDosage = (Spinner) findViewById(R.id.spnMedDosageType);
//        spnDosage.setAdapter(new CustomAdapter<>(this, R.layout.custom_spinner, Dosage.values()));

        // recuperando id passada no clique
        long medicationId = getIntent().getLongExtra("_ID_", -1);
        if (medicationId == -1) {
            this.sessionMedication = initMedication();
        } else {
            this.sessionMedication = MedicationDAL.getInstance(this).findOne(medicationId);
        }
        // atulizando a view de medicamento
        updateMedicationView();
    }

    private Medication initMedication() {
        Medication oRetVal = new Medication();

        oRetVal.setDosage("1,0");
        oRetVal.setDosageMeasureType(DosageMeasure.COMPRIMIDO);
        oRetVal.setPeriodicity(Periodicity.DIAx3);
        oRetVal.setDuration(7);
        oRetVal.setDurationType(Duration.DIA);

        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 6);
        c.set(Calendar.MINUTE, 30);
        oRetVal.setStartDate(c.getTime());

        return oRetVal;
    }

    private void updateMedicationView() {
        FormHelper.setTextBoxValue(this, R.id.med_form_qty, sessionMedication.getDosage());
        FormHelper.setTextBoxValue(this, R.id.med_form_measure, sessionMedication.getDosageMeasureType().toString());

        String duration = sessionMedication.getDuration() + " x " + sessionMedication.getDurationType().toString();
        FormHelper.setTextBoxValue(this, R.id.med_form_during, duration);

        FormHelper.setTextBoxValue(this, R.id.med_form_periodicity, sessionMedication.getPeriodicity().toString());

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
        FormHelper.setTextBoxValue(this, R.id.med_form_starting, dateFormat.format(sessionMedication.getStartDate()));
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm");
        FormHelper.setTextBoxValue(this, R.id.med_form_time, timeFormat.format(sessionMedication.getStartDate()));
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
//        boolean fIsContinuous = FormHelper.getCheckBoxValue(this, R.id.cbMed_ContUse);
//        if (fIsContinuous) {
//            findViewById(R.id.txtMed_Duration).setEnabled(false);
//            FormHelper.setTextBoxValue(this, R.id.txtMed_Duration, "");
//        } else {
//            findViewById(R.id.txtMed_Duration).setEnabled(true);
//        }
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

//                med.setDosageType(((Dosage) ((Spinner) findViewById(R.id.spnMedDosageType)).getSelectedItem()));
//                med.setDosage(FormHelper.getTextBoxValueAsInt(this, R.id.txtMed_Dosage));
//                med.setPeriodicity(FormHelper.getTextBoxValueAsInt(this, R.id.txtMed_Repetition));
//                med.setStartDate(selectedDate.getTime());
//
//                med.setContinuosUse(FormHelper.getCheckBoxValue(this, R.id.cbMed_ContUse));
//                if (!med.isContinuosUse()) {
//                    med.setDuration(FormHelper.getTextBoxValueAsInt(this, R.id.txtMed_Duration));
//                } else {
//                    med.setDuration(-1);
//                }

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
//        // validating name
//        if (!FormHelper.validateFormTextInput(this, R.id.txtMed_Name, getString(R.string.validation_error_message)))
//            return false;
//
//        // validating Dosage
//        if (!FormHelper.validateFormTextInput(this, R.id.txtMed_Dosage, getString(R.string.validation_error_message)))
//            return false;
//
//        // validating Repetition
//        if (!FormHelper.validateFormTextInput(this, R.id.txtMed_Repetition, getString(R.string.validation_error_message)))
//            return false;
//
//        // validating spinner
//        if (!validateSpinner())
//            return false;
//
//        // validating duration
//        if (!FormHelper.getCheckBoxValue(this, R.id.cbMed_ContUse) &&
//                !FormHelper.validateFormTextInput(this, R.id.txtMed_Duration, getString(R.string.validation_error_message))) {
//            return false;
//        }
//
//        // validating start date
//        if (!FormHelper.validateFormTextInput(this, R.id.txtMed_StartTime, getString(R.string.validation_error_message)))
//            return false;

        return true;
    }

    /**
     * Efetua a validação do combo box.
     *
     * @return Retorna falso se combo não tiver sido selecionado.
     */
    private boolean validateSpinner() {
//        Spinner spn = (Spinner) findViewById(R.id.spnMedDosageType);
//        if (spn.getSelectedItemId() <= 0) {
//            spn.setFocusableInTouchMode(true);
//            spn.requestFocus();
//
//            CustomAdapter adapter = (CustomAdapter) spn.getAdapter();
//            View view = spn.getSelectedView();
//            adapter.setError(view, getString(R.string.validation_error_message));
//            return false;
//        }
        return true;
    }

    /**
     * Evento chamado para abertura de dialog de quantidade de remedios
     */
    public void openDialogQuantity(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();
        // Pass null as the parent view because its going in the dialog layout
        final View dialogView = inflater.inflate(R.layout.dialog_med_quantity, null);

        FormHelper.setupPicker(dialogView, R.id.dg_md_qty, 0, 100, null, 1);
        FormHelper.setupPicker(dialogView, R.id.dg_md_qty_dec, 0, PICKER_QUANTITY_DEC.length-1,
                PICKER_QUANTITY_DEC, 0);

        // montando dialog
        builder.setTitle("Escolha a quantidade")
                .setView(dialogView)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        int qty = FormHelper.getPickerValue(dialogView, R.id.dg_md_qty);
                        String dec = PICKER_QUANTITY_DEC[FormHelper.getPickerValue(dialogView, R.id.dg_md_qty_dec)];
                        String dosage = "" + qty;
                        if(!dec.startsWith(","))
                            dosage += " e ";

                        dosage += dec;
                        sessionMedication.setDosage(dosage);
                        updateMedicationView();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }).create().show();
    }

    /**
     * Evento chamado para abertura de dialog de tipo de medida (comprimido, dragea, ml, etc)
     */
    public void openDialogMeasure(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();
        // Pass null as the parent view because its going in the dialog layout
        final View dialogView = inflater.inflate(R.layout.dialog_med_measure, null);

        String[] arrValues = DosageMeasure.getStringValues();
        FormHelper.setupPicker(dialogView, R.id.dg_md_measure, 0, arrValues.length-1, arrValues, 0);

        // montando dialog
        builder.setTitle("Escolha a medida")
                .setView(dialogView)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        int measure = FormHelper.getPickerValue(dialogView, R.id.dg_md_measure);
                        sessionMedication.setDosageMeasureType(DosageMeasure.fromInt(measure + 1));
                        updateMedicationView();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }).create().show();
    }

    /**
     * Evento chamado para abertura de dialog de frequencia (1 x ao dia, etc)
     */
    public void openDialogPeriodicity(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();
        // Pass null as the parent view because its going in the dialog layout
        final View dialogView = inflater.inflate(R.layout.dialog_med_periodicity, null);

        String[] arrValues = Periodicity.getStringValues();
        FormHelper.setupPicker(dialogView, R.id.dg_md_periodicity, 0, arrValues.length-1, arrValues, 0);

        // montando dialog
        builder.setTitle("Escolha a frequência")
                .setView(dialogView)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        int freq = FormHelper.getPickerValue(dialogView, R.id.dg_md_periodicity);
                        sessionMedication.setPeriodicity(Periodicity.fromInt(freq + 1));
                        updateMedicationView();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }).create().show();
    }

    /**
     * Evento chamado para abertura de dialog de repeticao (7 dias, uma semana, etc)
     */
    public void openDialogRepetition(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();
        // Pass null as the parent view because its going in the dialog layout
        final View dialogView = inflater.inflate(R.layout.dialog_med_periodicity, null);

        String[] arrValues = Periodicity.getStringValues();
        FormHelper.setupPicker(dialogView, R.id.dg_md_periodicity, 0, arrValues.length-1, arrValues, 0);

        // montando dialog
        builder.setTitle("Escolha o período")
                .setView(dialogView)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
//                        int freq = FormHelper.getPickerValue(dialogView, R.id.dg_md_periodicity);
//                        sessionMedication.setPeriodicity(Periodicity.fromInt(freq + 1));
//                        updateMedicationView();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }).create().show();
    }

    public void openDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "Selecione a data");
    }

    public void openTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "Selecione a hora");
    }

    /**
     * Evento chamado após seleção da data. Esse evento também abre o dialog de seleção da hora.
     */
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.setTime(sessionMedication.getStartDate());
        c.set(year, monthOfYear, dayOfMonth);
        // reajustando dat ade inicio
        sessionMedication.setStartDate(c.getTime());
        updateMedicationView();
    }

    /**
     * Evento chamado após seleção de tempo.
     */
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar c = Calendar.getInstance();
        c.setTime(sessionMedication.getStartDate());
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        // reajustando dat ade inicio
        sessionMedication.setStartDate(c.getTime());
        updateMedicationView();
    }

    // // // // // // // // // // // // // // // // // // // // // // // // //
    // FRAGMENTS - utilizados para mostrar dialógos de data e hora
    // // // // // // // // // // // // // // // // // // // // // // // // //

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

    public static class TimePickerFragment extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), (MedicationFormActivity) getActivity(), 6,
                    30, DateFormat.is24HourFormat(getActivity()));
        }
    }

}
