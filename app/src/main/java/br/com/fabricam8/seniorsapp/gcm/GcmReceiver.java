package br.com.fabricam8.seniorsapp.gcm;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;


public class GcmReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // handle intent



        setResultCode(Activity.RESULT_OK);
    }
}

