package br.com.fabricam8.seniorsapp;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;


public abstract class BaseActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    /**
     * Sets the header title. When available.
     * @param title The title to be set.
     */
    protected void setHeaderTitle(String title) {
        TextView vTitle = (TextView) findViewById(R.id.header_label_title);
        if(vTitle != null) {
            vTitle.setText(title);
        }
    }

    /**
     * Returns to the previous activity.
     *
     * @param v The view requested the method.
     */
    public void previous(View v) {
        finish();
    }
}
