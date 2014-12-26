package br.com.fabricam8.seniorsapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DashboardActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);


//        Button btGps = (Button) findViewById(R.id.dashButtonGps);
//        btGps.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
    }

    public void openLocation(View v)
    {
        Intent intent = new Intent(DashboardActivity.this, LocationActivity.class);
        startActivity(intent);
    }

    public void openBrowser(View v)
    {
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://google.com/"));
        startActivity(i);
    }
}
