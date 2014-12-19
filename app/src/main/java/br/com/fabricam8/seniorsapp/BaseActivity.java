package br.com.fabricam8.seniorsapp;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;


public abstract class BaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

}
