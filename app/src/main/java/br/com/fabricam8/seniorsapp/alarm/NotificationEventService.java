package br.com.fabricam8.seniorsapp.alarm;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.IBinder;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;

import br.com.fabricam8.seniorsapp.DashboardActivity;
import br.com.fabricam8.seniorsapp.R;

/**
 * Created by Aercio on 1/27/15.
 */
public class NotificationEventService extends Service
{

    private NotificationManager mManager;

    @Override
    public IBinder onBind(Intent arg0)
    {
        return null;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
    }

    @SuppressWarnings("static-access")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        int retVal = super.onStartCommand(intent, flags, startId);

        // Retrieving app context
        Context mContext = this.getApplicationContext();
        // Initializing notification manager
        mManager = (NotificationManager) mContext.getSystemService(mContext.NOTIFICATION_SERVICE);

        Intent i = new Intent(mContext, DashboardActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pni = PendingIntent
                .getActivity(mContext, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

        // Creating notification
        Notification notification = new Notification.Builder(mContext)
                .setContentTitle("Senior's App")
                .setContentText("Descrição de Evento")
                .setSmallIcon(R.drawable.ic_launcher)
                .setLights(Color.RED, 3000, 3000)
                .setContentIntent(pni)
                .build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notification.defaults |= Notification.DEFAULT_SOUND;

        // notifying
        mManager.notify(0, notification);

        // ringing!!!
        mContext.startService(new Intent(mContext, AlarmPlayerService.class));

        return retVal;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }

    public static void setupAlarm(Context context){
        Calendar c = Calendar.getInstance();

        c.setTimeInMillis(System.currentTimeMillis());
        c.add(Calendar.SECOND, 15);

        Date d = new Date(c.getTimeInMillis());

        Log.i("\nalarm date: ", d.toString());

        Intent myIntent = new Intent(context, NotificationEventReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, myIntent, 0);

        AlarmManager alarmManager = (AlarmManager)context.getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    }
}

