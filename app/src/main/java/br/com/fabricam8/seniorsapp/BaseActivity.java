package br.com.fabricam8.seniorsapp;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;


public abstract class BaseActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

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
