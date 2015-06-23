package br.com.fabricam8.seniorsapp;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import br.com.fabricam8.seniorsapp.alarm.AlarmPlayerService;
import br.com.fabricam8.seniorsapp.dal.AlertEventDAL;
import br.com.fabricam8.seniorsapp.dal.MedicationDAL;
import br.com.fabricam8.seniorsapp.domain.AlertEvent;
import br.com.fabricam8.seniorsapp.domain.Consultation;
import br.com.fabricam8.seniorsapp.domain.Exercise;
import br.com.fabricam8.seniorsapp.domain.Medication;
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

    }

    @Override
    public void onResume() {
        super.onResume();
        // stopping alarm - if any
        this.stopService(new Intent(this, AlarmPlayerService.class));
        loadInfo();
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

        Context mContext = DashboardActivity.this;

        // inicializando shared prefs
        final SharedPreferences prefs = getSharedPreferences(GlobalParams.SHARED_PREFS_ID, Context.MODE_PRIVATE);
        String userName = prefs.getString(GlobalParams.SHARED_PROPERTY_REG_NAME, "");
        if (!userName.isEmpty()) {
            FormHelper.setTextBoxValue(DashboardActivity.this, R.id.dash_txtUserName, userName);
        }

        String b64Img = prefs.getString(GlobalParams.SHARED_PROPERTY_PROFILE_PHOTO, "");
        if (!b64Img.isEmpty()) {
            ImageView imgPic = (ImageView) findViewById(R.id.imgPic);
            imgPic.setImageBitmap(FormHelper.decodeBase64(b64Img));
        }


        loadEventsList(mContext);

    }

    /**
     * Loads the enext events
     */
    private void loadEventsList(Context mContext) {
        // carregando todos os eventos
        AlertEventDAL db = AlertEventDAL.getInstance(mContext);
        List<AlertEvent> alerts = db.findAll();
        sortAlerts(alerts);
        if(alerts != null) {
            int i = 1;
            for (AlertEvent e : alerts) {
                if(e.getAlarmsPlayed() < e.getMaxAlarms()) {
                    // TODO Codigo seboso!!! melhorar essa porqueira!!!
                    int id = -1;
                    if(i==1) {
                        id = R.id.dash_txtEvent_1;
                    }
                    else if(i==2) {
                        id = R.id.dash_txtEvent_2;
                    }
                    else if(i==3) {
                        id = R.id.dash_txtEvent_3;
                    }
                    else if(i==4) {
                        id = R.id.dash_txtEvent_4;
                    }
                    else if(i==5) {
                        id = R.id.dash_txtEvent_5;
                    }

                    // carregando elemento
                    TextView el = (TextView) findViewById(id);

                    SimpleDateFormat dateFormatter = new SimpleDateFormat("HH:mm");
                    String d = dateFormatter.format(e.getNextAlert());
                    String event = d + " - " + e.getEvent();

                    // setando evento
                    el.setText(event);

                    Drawable ico = getResources().getDrawable(R.drawable.ic_transparent);
                    if (e.getEntityClass().equals(Medication.class.getName())) {
                        ico = getResources().getDrawable(R.drawable.ic_pill_gray);
                    }
                    else if (e.getEntityClass().equals(Consultation.class.getName())) {
                        ico = getResources().getDrawable(R.drawable.ic_doctor_gray);
                    }
                    else if (e.getEntityClass().equals(Exercise.class.getName())) {
                        ico = getResources().getDrawable(R.drawable.ic_exercise_gray);
                    }
                    // setndo icone
                    el.setCompoundDrawablesWithIntrinsicBounds(ico, null, null, null);

                    i++;
                }
                if(i==5) break;
            }
        }
    }

    private void sortAlerts(List<AlertEvent> alerts) {
        Collections.sort(alerts, new Comparator<AlertEvent>() {
            public int compare(AlertEvent o1, AlertEvent o2) {
                return o1.getNextAlert().compareTo(o2.getNextAlert());
            }
        });
    }

}
