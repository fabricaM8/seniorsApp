package br.com.fabricam8.seniorsapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

public class DashboardActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
    }

    /**
     * Invoked when clicked on the dashboard.
     * @param v The button which invoked the action.
     */
    public void openLocation(View v)
    {
        Intent i = new Intent(DashboardActivity.this, LocationActivity.class);
        startActivity(i);
    }

    /**
     * Invoked when clicked on the dashboard.
     * @param v The button which invoked the action.
     */
    public void openBrowser(View v)
    {
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://google.com/"));
        startActivity(i);
    }
}
