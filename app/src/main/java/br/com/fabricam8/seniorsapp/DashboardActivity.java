package br.com.fabricam8.seniorsapp;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;

import br.com.fabricam8.seniorsapp.alarm.AlarmPlayerService;
import br.com.fabricam8.seniorsapp.util.FormHelper;
import br.com.fabricam8.seniorsapp.util.GlobalParams;
import br.com.fabricam8.seniorsapp.util.ToolbarBuilder;

public class DashboardActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // create toolbar
        ToolbarBuilder.build(this, false);

        loadInfo();
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
    public void openContacts(View v) {
        Intent i = new Intent(DashboardActivity.this,EmergencyActivity.class);
        startActivity(i);
    }

    public void viewEventsListSaude(View v)
    {
        Intent i = new Intent(DashboardActivity.this, EventsListSaude.class);
        startActivity(i);
    }

    /**
     * Invoked when clicked on the dashboard.
     *
     * @param v The button which invoked the action.
     */
    public void openDialer(View v) {
        Intent dial = new Intent();
        dial.setAction("android.intent.action.DIAL");
        startActivity(dial);
    }

    /**
     * Invoked when clicked on the dashboard.
     *
     * @param v The button which invoked the action.
     */
    public void openSms(View v) {
        String uri= "smsto:";
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse(uri));
        startActivity(intent);
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

    /**
     * Invoked when clicked on the dashboard.
     *
     * @param v The button which invoked the action.
     */
    public void viewProfile(View v) {
        Intent i = new Intent(DashboardActivity.this, ProfileFormActivity.class);
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

    /**
     * Loads the shared prefs info
     */
    private void loadInfo() {
        // inicializando shared prefs
        final SharedPreferences prefs = getSharedPreferences(GlobalParams.SHARED_PREFS_ID, Context.MODE_PRIVATE);
        String userName = prefs.getString(GlobalParams.SHARED_PROPERTY_REG_NAME, "");
        if (!userName.isEmpty()) {
            FormHelper.setTextBoxValue(DashboardActivity.this, R.id.dash_txtUserName, userName);
        }
    }
}
