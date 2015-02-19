package br.com.fabricam8.seniorsapp;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class ExerciseFormActivity extends ActionBarActivity
{
    // criando o Array de String
    private static final String[] opcoes = { "Correr","Andar", "Banhar" };
    ArrayAdapter<String> aOpcoes;
    // Declarando variavel do tipo Spinner
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_form);
        aOpcoes = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, opcoes);
        // capturando o spinner do xml pela id
        spinner = (Spinner) findViewById(R.id.spnOpcoes);
        spinner.setAdapter(aOpcoes);


    }

    public void showTimePickerDialogActivity(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "Selecione a data");
    }
    public static class DatePickerFragment extends DialogFragment {
         /*
           @Override
         public Dialog onCreateDialog(Bundle savedInstanceState) {
                    // Use the current date as the default date in the picker
                    final Calendar c = Calendar.getInstance();
                    int year = c.get(Calendar.YEAR);
                    int month = c.get(Calendar.MONTH);
                    int day = c.get(Calendar.DAY_OF_MONTH);

                    // Create a new instance of DatePickerDialog and return it
                    //return new DatePickerDialog(getActivity(), (activity_form) getActivity(), year,
                      //      month, day);
                }
              */
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
}
