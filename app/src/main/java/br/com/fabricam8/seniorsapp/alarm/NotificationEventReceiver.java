package br.com.fabricam8.seniorsapp.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Aercio on 1/27/15.
 */
public class NotificationEventReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Intent svcIntent = new Intent(context, NotificationEventService.class);
        context.startService(svcIntent);
    }
}