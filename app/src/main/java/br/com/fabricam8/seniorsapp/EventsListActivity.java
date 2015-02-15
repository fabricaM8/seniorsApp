package br.com.fabricam8.seniorsapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import br.com.fabricam8.seniorsapp.dal.MedicationDAL;
import br.com.fabricam8.seniorsapp.domain.Medication;
import br.com.fabricam8.seniorsapp.util.ToolbarBuilder;


public class EventsListActivity extends ActionBarActivity {


    String[] medicamentos;

    String[] ativadades = {"Correr", "Nadar", "Caminhar"};

    String[] todos = {""};

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_list);

        // create toolbar
        ToolbarBuilder.build(this, true);

    }

    protected void onResume() {
        super.onResume();

        showMedications(null);
    }

    public void showAllEvents(View v) {
        listView = (ListView) findViewById(R.id.listView);
        ArrayAdapter<String> array = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, todos);
        listView.setAdapter(array);
    }

    public void showMedications(View v) {
        MedicationDAL db = MedicationDAL.getInstance(this);
        List<Medication> lstMeds = db.findAll();

        this.medicamentos = new String[lstMeds.size()];
        for (int i = 0; i < lstMeds.size(); i++) {
            medicamentos[i] = lstMeds.get(i).getName();
        }

        listView = (ListView) findViewById(R.id.listView);
        ArrayAdapter<String> array = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, medicamentos);
        listView.setAdapter(array);
    }

    public void showActivities(View v) {
        listView = (ListView) findViewById(R.id.listView);
        ArrayAdapter<String> array2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ativadades);
        listView.setAdapter(array2);
    }


    /**
     * Invoked when clicked on the dashboard.
     *
     * @param v The button which invoked the action.
     */
    public void viewMedicationForm(View v) {
        Intent i = new Intent(EventsListActivity.this, MedicationFormActivity.class);
        startActivity(i);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_events_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.action_help_events_list:
                break;
            case R.id.action_events_new_med:
                viewMedicationForm(null);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return super.onOptionsItemSelected(item);
    }

}
