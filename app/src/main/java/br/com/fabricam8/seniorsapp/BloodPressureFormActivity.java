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
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import br.com.fabricam8.seniorsapp.dal.BloodPressureDAL;
import br.com.fabricam8.seniorsapp.domain.BloodPressure;
import br.com.fabricam8.seniorsapp.util.FormHelper;
import br.com.fabricam8.seniorsapp.util.ToolbarBuilder;

public class BloodPressureFormActivity extends ActionBarActivity
        implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    private int selectedSystolic;
    private int selectedDiastolic;
    private Date selecteDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pressure_form);

        // create toolbar
        Toolbar mToolbar = ToolbarBuilder.build(this, true);
        mToolbar.setBackgroundColor(getResources().getColor(R.color.seniors_active_dash_button_color_orange));
        mToolbar.setAlpha(0.6f);

        Calendar c = Calendar.getInstance();
        selecteDate = c.getTime();
        updateView();
    }

    private void updateView() {
        if (selectedSystolic > 0) {
            FormHelper.setTextBoxValue(this, R.id.health_pressure_form_systolic, "" + selectedSystolic);
        }
        if (selectedDiastolic > 0) {
            FormHelper.setTextBoxValue(this, R.id.health_pressure_form_diastolic, "" + selectedDiastolic);
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
        FormHelper.setTextBoxValue(this, R.id.health_pressure_form_date, dateFormat.format(selecteDate));

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        FormHelper.setTextBoxValue(this, R.id.health_pressure_form_time, timeFormat.format(selecteDate));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
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

    public void saveBloodPressure(View view) {
        Context context = this;

        try {
            BloodPressureDAL db = BloodPressureDAL.getInstance(context);

            if (selectedSystolic > 0) {
                BloodPressure g = new BloodPressure();
                g.setDate(selecteDate);
                g.setSystolic(selectedSystolic);
                g.setDiastolic(selectedDiastolic);
                db.create(g);

                Toast.makeText(this, getString(R.string.success_pressure_form_submit), Toast.LENGTH_LONG).show();
                finish();
            }
        } catch (Exception ex) {
            Toast.makeText(this, getString(R.string.error_form_submit), Toast.LENGTH_LONG).show();
        }
    }

    public void openDialogSystolicValue(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();
        // Pass null as the parent view because its going in the dialog layout
        final View dialogView = inflater.inflate(R.layout.dialog_general_number, null);
        FormHelper.setupPicker(dialogView, R.id.dg_general_number, 0, 300, null, 120);

        // montando dialog
        builder.setTitle("Selecione o valor sistólico")
                .setView(dialogView)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        selectedSystolic = FormHelper.getPickerValue(dialogView, R.id.dg_general_number);
                        updateView();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }).create().show();
    }
    public void openDialogDiatolicValue(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();
        // Pass null as the parent view because its going in the dialog layout
        final View dialogView = inflater.inflate(R.layout.dialog_general_number, null);
        FormHelper.setupPicker(dialogView, R.id.dg_general_number, 0, 250, null, 80);

        // montando dialog
        builder.setTitle("Selecione o valor diastólico")
                .setView(dialogView)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        selectedDiastolic = FormHelper.getPickerValue(dialogView, R.id.dg_general_number);
                        updateView();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }).create().show();
    }

    public void openDatePickerDialogActivity(View view) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "Selecione a data");
    }

    public void openTimePickerDialog(View view) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "Selecione a hora");
    }

    /**
     * Evento chamado após seleção da data.
     */
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(year, monthOfYear, dayOfMonth);
        // reajustando data de inicio
        selecteDate = c.getTime();
        updateView();
    }

    /**
     * Evento chamado após seleção de tempo.
     */
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar c = Calendar.getInstance();
        c.setTime(selecteDate);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        // reajustando dat ade inicio
        selecteDate = c.getTime();
        updateView();
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
            return new DatePickerDialog(getActivity(), (BloodPressureFormActivity) getActivity(), year,
                    month, day);
        }
    }

    public static class TimePickerFragment extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), (BloodPressureFormActivity) getActivity(), hour,
                    minute, DateFormat.is24HourFormat(getActivity()));
        }
    }
}