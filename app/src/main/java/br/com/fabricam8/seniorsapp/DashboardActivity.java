package br.com.fabricam8.seniorsapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import br.com.fabricam8.seniorsapp.alarm.AlarmPlayerService;

public class DashboardActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Toolbar toolbar = (Toolbar) findViewById(R.id.seniors_toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public void onResume() {
        super.onResume();
        // stopping alarm - if any
        this.stopService(new Intent(this, AlarmPlayerService.class));
    }

    /**
     * Invoked when clicked on the dashboard.
     *
     * @param v The button which invoked the action.
     */
    public void openLocation(View v) {
        Intent i = new Intent(DashboardActivity.this, LocationActivity.class);
        startActivity(i);
    }

    /**
     * Invoked when clicked on the dashboard.
     *
     * @param v The button which invoked the action.
     */
    public void viewEvents(View v) {
        Intent i = new Intent(DashboardActivity.this, EventsListActivity.class);
        startActivity(i);
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
