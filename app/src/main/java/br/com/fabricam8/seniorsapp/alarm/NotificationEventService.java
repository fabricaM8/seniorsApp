package br.com.fabricam8.seniorsapp.alarm;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import br.com.fabricam8.seniorsapp.DashboardActivity;
import br.com.fabricam8.seniorsapp.R;
import br.com.fabricam8.seniorsapp.dal.AlertEventDAL;
import br.com.fabricam8.seniorsapp.domain.AlertEvent;

/**
 * Created by Aercio on 1/27/15.
 */
public class NotificationEventService extends Service {

    private static final String BUNDLE_ALERT_ID = "ALERT_ID";

    private NotificationManager mManager;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @SuppressWarnings("static-access")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int retVal = super.onStartCommand(intent, flags, startId);


        // Retrieving app context
        Context mContext = this.getApplicationContext();
        // Initializing notification manager
        mManager = (NotificationManager) mContext.getSystemService(mContext.NOTIFICATION_SERVICE);

        String notificationMessage = "Você possui um novo evento!";

        // recuperando bundle - id do alert esta aqui
        Bundle extras = intent.getExtras();
        if (extras != null) {
            String data = extras.getString(BUNDLE_ALERT_ID);
            if (data != null) {
                AlertEventDAL db = AlertEventDAL.getInstance(mContext);
                AlertEvent alert = db.findOne(Long.parseLong(data));
                if(alert != null) {
                    SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                    String d = dateFormatter.format(alert.getNextAlert());

                    notificationMessage = d + " - " + alert.getEvent();

                    int iAlarmsPlayed = alert.getAlarmsPlayed();
                    alert.setAlarmsPlayed(++iAlarmsPlayed);
                    // updating database
                    db.update(alert);
                    // todo update next alarm!
                }
            }
        }

        Intent i = new Intent(mContext, DashboardActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pni = PendingIntent
                .getActivity(mContext, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

        // Creating notification
        Notification notification = new Notification.Builder(mContext)
                .setContentTitle("Senior's App")
                .setContentText(notificationMessage)
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
    public void onDestroy() {
        super.onDestroy();
    }

    public static void setupAlarm(Context context, AlertEvent event) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(event.getNextAlert().getTime());

        Intent alarmIntent = new Intent(context, NotificationEventReceiver.class);
        alarmIntent.putExtra(BUNDLE_ALERT_ID, event.getID() + "");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    }
}

