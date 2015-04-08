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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
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
        Toolbar mToolbar = ToolbarBuilder.build(this, true);
        mToolbar.setBackgroundColor(getResources().getColor(R.color.seniors_active_dash_button_color_navy));

        // adicionando edit listeners aos campos de texto
        addTextChangeListeners();

        // recuperando id passada no clique
        long medicationId = getIntent().getLongExtra("_ID_", -1);
        if (medicationId == -1) {
            this.sessionMedication = initMedication();
        } else {
            this.sessionMedication = MedicationDAL.getInstance(this).findOne(medicationId);
        }
        // atulizando a view de medicamento
        updateMedicationView();

        findViewById(R.id.med_form_qty).requestFocus();
    }

    /**
     * Este método adiciona text edit listeners aos campos de texto da página.
     */
    private void addTextChangeListeners() {
        EditText txtName = (EditText)findViewById(R.id.med_form_name);
        txtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                sessionMedication.setName(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        EditText txtObserv = (EditText)findViewById(R.id.med_form_observ);
        txtObserv.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                sessionMedication.setDescription(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private Medication initMedication() {
        Medication oRetVal = new Medication();

        oRetVal.setDosage("1");
        oRetVal.setDosageMeasureType(DosageMeasure.COMPRIMIDO);
        oRetVal.setPeriodicity(Periodicity.DIAx3);
        oRetVal.setDuration(7);
        oRetVal.setDurationType(Duration.DIA);
        oRetVal.setHasAlarm(true);

        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 6);
        c.set(Calendar.MINUTE, 0);
        oRetVal.setStartDate(c.getTime());

        return oRetVal;
    }

    private void updateMedicationView() {
        FormHelper.setTextBoxValue(this, R.id.med_form_name, sessionMedication.getName());

        FormHelper.setTextBoxValue(this, R.id.med_form_qty, sessionMedication.getDosage());
        FormHelper.setTextBoxValue(this, R.id.med_form_measure, sessionMedication.getDosageMeasureType().toString());

        FormHelper.setTextBoxValue(this, R.id.med_form_periodicity, sessionMedication.getPeriodicity().toString());

        if(sessionMedication.isContinuosUse()) {
            FormHelper.setTextBoxValue(this, R.id.med_form_during, "Uso contínuo");
        }
        else {
            String duration = sessionMedication.getDuration() + " " + sessionMedication.getDurationType().toString();
            FormHelper.setTextBoxValue(this, R.id.med_form_during, duration);
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
        FormHelper.setTextBoxValue(this, R.id.med_form_starting, dateFormat.format(sessionMedication.getStartDate()));

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        FormHelper.setTextBoxValue(this, R.id.med_form_time, timeFormat.format(sessionMedication.getStartDate()));

        FormHelper.setTextBoxValue(this, R.id.med_form_observ, sessionMedication.getDescription());

      //  FormHelper.setSwitchValue(this, R.id.med_form_alarm, sessionMedication.isHasAlarm());
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

            if (validateForm())
            {
                // creating medication
                long id = -1;
                if(sessionMedication.getID() > 0 ) { // update
                    id = sessionMedication.getID();
                    db.update(sessionMedication);
                }
                else {
                    id = db.create(sessionMedication);
                }

                if (id > 0 && sessionMedication.isHasAlarm()) {
                    AlertEventDAL dbAlert = AlertEventDAL.getInstance(context);
                    // verificando se ja existe alarme para essa entidade
                    AlertEvent alert = dbAlert.findOneByEntityIdAndType(id, Medication.class.getName());
                    if(alert == null) {
                        alert = new AlertEvent();
                        alert.setEntityId(id);
                        alert.setEntityClass(Medication.class.getName());
                    }

                    alert.setEvent(sessionMedication.getName());
                    // setando numero de alarmes
                    int numAlarms = sessionMedication.getNumOfAlarms() > 0 ? sessionMedication.getNumOfAlarms() : 1;
                    alert.setMaxAlarms(numAlarms);
                    alert.setAlarmsPlayed(0);
                    alert.setNextAlert(sessionMedication.getStartDate());

                    long alertId = dbAlert.create(alert);

                    // setando id para enviar pra servico de alarme
                    alert.setID(alertId);

                    // setando alarme
                    NotificationEventService.setupAlarm(this, alert);
                }
                else {
                    // remova alarme exixstente (se houver)
                    AlertEventDAL alertDb = AlertEventDAL.getInstance(this);
                    AlertEvent alert = alertDb.findOneByEntityIdAndType(id, Medication.class.getName());
                    if(alert != null) {
                        // Cancelando alarme
                        NotificationEventService.cancelAlarm(this, alert.getID());
                        Log.i("Seniors - Medication Form", "Removendo alarme");
                        alertDb.remove(alert);
                    }
                }

                Toast.makeText(this, getString(R.string.success_medication_form_submit), Toast.LENGTH_LONG).show();
                finish();
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
        if (!FormHelper.validateFormTextInput(this, R.id.med_form_name, getString(R.string.validation_error_message)))
            return false;

        MedicationDAL db = MedicationDAL.getInstance(this);
        Medication med = db.findByName(FormHelper.getTextBoxValue(this, R.id.med_form_name));
        if(med != null)
        {
            // TODO realizar equals????
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            // montando dialog
            builder.setTitle("Alerta")
                    .setMessage("Já existe um medicamento cadastrado com esse nome.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    }).create().show();

            return false;
        }

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

                        if(!dec.equals(",0"))
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
        final View dialogView = inflater.inflate(R.layout.dialog_med_duration, null);

        // setando valores iniciais para os pickers
        String[] arrValues = Duration.getStringValues();
        FormHelper.setupPicker(dialogView, R.id.dg_md_duration_type, 0, arrValues.length-1, arrValues, 0);
        FormHelper.setupPicker(dialogView, R.id.dg_md_duration, 1, 30, null, 1);

        // adicionando listener para switcher de uso continuo
        Switch swCont = ((Switch)dialogView.findViewById(R.id.dg_md_sw_cont));
        swCont.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    dialogView.findViewById(R.id.dg_md_layout_duration).setVisibility(View.GONE);
                }
                else {
                    dialogView.findViewById(R.id.dg_md_layout_duration).setVisibility(View.VISIBLE);
                }
            }
        });

        // montando dialog
        builder.setTitle("Escolha o período")
                .setView(dialogView)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        boolean isCont = FormHelper.getSwitchValue(dialogView, R.id.dg_md_sw_cont);
                        if(isCont) {
                            sessionMedication.setDuration(-1);
                            sessionMedication.setDurationType(Duration.NONE);
                            sessionMedication.setContinuosUse(true);
                        }
                        else {
                            int duration = FormHelper.getPickerValue(dialogView, R.id.dg_md_duration);
                            int dType = FormHelper.getPickerValue(dialogView, R.id.dg_md_duration_type);
                            sessionMedication.setDuration(duration);
                            sessionMedication.setDurationType(Duration.fromInt(dType + 1));
                            sessionMedication.setContinuosUse(false);
                        }

                        updateMedicationView();
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
