package br.com.fabricam8.seniorsapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import br.com.fabricam8.seniorsapp.dal.GlucosisDAL;
import br.com.fabricam8.seniorsapp.domain.Glucosis;
import br.com.fabricam8.seniorsapp.util.FormHelper;
import br.com.fabricam8.seniorsapp.util.ToolbarBuilder;

public class GlucosisFormActivity extends ActionBarActivity implements DatePickerDialog.OnDateSetListener {

    private int selectedRate;
    private Date selecteDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glucosis_form);

        // create toolbar
        Toolbar mToolbar = ToolbarBuilder.build(this, true);
        mToolbar.setBackgroundColor(getResources().getColor(R.color.seniors_active_dash_button_color_orange));
        mToolbar.setAlpha(0.7f);

        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        selecteDate = c.getTime();
        updateView();
    }

    private void updateView() {
        if (selectedRate > 30) {
            FormHelper.setTextBoxValue(this, R.id.health_glucosis_form_qty, "" + selectedRate);
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
        FormHelper.setTextBoxValue(this, R.id.health_glucosis_form_date, dateFormat.format(selecteDate));
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

    public void saveGlucosis(View view) {
        Context context = this;

        try {
            GlucosisDAL db = GlucosisDAL.getInstance(context);

            if (selectedRate > 0) {
                Glucosis g = new Glucosis();
                g.setDate(selecteDate);
                g.setRate(selectedRate);
                db.create(g);

                Toast.makeText(this, getString(R.string.success_glucosis_form_submit), Toast.LENGTH_LONG).show();
                finish();
            }
        } catch (Exception ex) {
            Toast.makeText(this, getString(R.string.error_form_submit), Toast.LENGTH_LONG).show();
        }

    }

    public void openDatePickerDialogActivity(View view) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "Selecione a data");
    }

    public void openDialogQuantity(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();
        // Pass null as the parent view because its going in the dialog layout
        final View dialogView = inflater.inflate(R.layout.dialog_general_number, null);
        FormHelper.setupPicker(dialogView, R.id.dg_general_number, 30, 250, null, 100);

        // montando dialog
        builder.setTitle("Selecione o peso")
                .setView(dialogView)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        selectedRate = FormHelper.getPickerValue(dialogView, R.id.dg_general_number);
                        updateView();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }).create().show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(year, monthOfYear, dayOfMonth);
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
            return new DatePickerDialog(getActivity(), (GlucosisFormActivity) getActivity(), year,
                    month, day);
        }
    }

}