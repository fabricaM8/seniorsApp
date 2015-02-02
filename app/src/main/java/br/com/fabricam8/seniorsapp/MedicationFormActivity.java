package br.com.fabricam8.seniorsapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;

import br.com.fabricam8.seniorsapp.dal.MedicationDAL;
import br.com.fabricam8.seniorsapp.domain.Medication;
import br.com.fabricam8.seniorsapp.enums.Dosage;
import br.com.fabricam8.seniorsapp.util.FormHelper;


public class MedicationFormActivity extends ActionBarActivity
        implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    private Calendar selectedDate;
    private boolean mFirstCalendarCal = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication_form);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_medication_form, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save_medication) {
            return true;
        } else if (id == R.id.action_cancel_medication) {
            return true;
        }


        return super.onOptionsItemSelected(item);
    }



    public void cancel(View v) {
        // end activity
        finish();
    }

    public void saveMedication(View v) {
        Context context = this;

        MedicationDAL db = MedicationDAL.getInstance(context);

        Medication med = new Medication();
        med.setName(FormHelper.getTextBoxValue(this, R.id.txtMed_Name));
        med.setDosageType(Dosage.fromInt(1)); // TODO change values
        med.setDosage(FormHelper.getTextBoxValueAsInt(this, R.id.txtMed_Dosage));
        med.setPeriodicity(FormHelper.getTextBoxValueAsInt(this, R.id.txtMed_Repetition));
        med.setDuration(FormHelper.getTextBoxValueAsInt(this, R.id.txtMed_Repetition)); // TODO adicionar campo
        med.setNextAlert(selectedDate.getTime());
        // creating
        long id = db.create(med);

        Log.i("Seniors - med", med.toString());

        Medication dbMed = db.findOne(id);
        Log.i("Seniors - db med", dbMed.toString());
        //NotificationEventService.setupAlarm(this);
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "Selecione a data");
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        if(mFirstCalendarCal) {
            mFirstCalendarCal = false;

            this.selectedDate = Calendar.getInstance();
            this.selectedDate.set(year, monthOfYear, dayOfMonth);

            DialogFragment newFragment = new TimePickerFragment();
            newFragment.show(getFragmentManager(), "Selecione a hora");
        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        this.selectedDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
        this.selectedDate.set(Calendar.MINUTE, minute);

        FormHelper.setTextBoxValue(this, R.id.txtMed_StartTime, this.selectedDate.getTime().toString());
    }


    // // // // //
    // FRAGMENTS
    // // // // //

    public static class TimePickerFragment extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), (MedicationFormActivity)getActivity(), hour,
                    minute, DateFormat.is24HourFormat(getActivity()));
        }
    }

    public static class DatePickerFragment extends DialogFragment{

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), (MedicationFormActivity)getActivity(), year,
                    month, day);
        }
    }
}
