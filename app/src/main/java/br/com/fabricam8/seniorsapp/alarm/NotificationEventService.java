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
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import br.com.fabricam8.seniorsapp.DashboardActivity;
import br.com.fabricam8.seniorsapp.MedicationInfoActivity;
import br.com.fabricam8.seniorsapp.R;
import br.com.fabricam8.seniorsapp.dal.AlertEventDAL;
import br.com.fabricam8.seniorsapp.dal.MedicationDAL;
import br.com.fabricam8.seniorsapp.domain.AlertEvent;
import br.com.fabricam8.seniorsapp.domain.Medication;

/**
 * Created by Aercio on 1/27/15.
 */
public class NotificationEventService extends Service {

    public static final String BUNDLE_ALERT_ID = "ALERT_ID";

    private NotificationManager mManager;

    public static void setupAlarm(Context context, AlertEvent event) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(event.getNextAlert().getTime());
        c.set(Calendar.SECOND, 0);

        Intent alarmIntent = new Intent(context, NotificationEventReceiver.class);
        alarmIntent.putExtra(BUNDLE_ALERT_ID, event.getID());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int)event.getID(), alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    }

    public static void cancelAlarm(Context context, long alertId) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        Intent alarmIntent = new Intent(context, NotificationEventReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int) alertId, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);
    }

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

        if(intent != null) {
            // recuperando bundle - id do alert esta aqui
            Bundle extras = intent.getExtras();
            if (extras != null) {

                // Retrieving app context
                Context mContext = this.getApplicationContext();
                // Initializing notification manager
                mManager = (NotificationManager) mContext.getSystemService(mContext.NOTIFICATION_SERVICE);

                String notificationMessage = "Você possui um novo evento!";

                long alertId = extras.getLong(BUNDLE_ALERT_ID, -1);
                if (alertId != -1) {
                    MedicationDAL medDb = MedicationDAL.getInstance(mContext);
                    Medication oMed = null;

                    AlertEventDAL db = AlertEventDAL.getInstance(mContext);
                    AlertEvent alert = db.findOne(alertId);
                    if (alert != null) {
                        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                        String d = dateFormatter.format(alert.getNextAlert());

                        notificationMessage = d + " - " + alert.getEvent();

                        int iAlarmsPlayed = alert.getAlarmsPlayed();
                        alert.setAlarmsPlayed(++iAlarmsPlayed);
                        // se ainda puder tocar...
                        if (iAlarmsPlayed < alert.getMaxAlarms() || alert.getMaxAlarms() == AlertEvent.FOREVER) {
                            // Se for medicaçao
                            if (alert.getEntityClass().equals(Medication.class.getName())) {
                                oMed = medDb.findOne(alert.getEntityId());
                                if (oMed != null) {
                                    Calendar nextEvt = getNextServiceTime(alert, oMed);
                                    // set next alert
                                    alert.setNextAlert(nextEvt.getTime());
                                }
                            }
                            // outros tipos de entidade aqui... else if (bla bla) {}

                            // resetting alarm
                            setupAlarm(mContext, alert);
                        }

                        // updating database
                        db.update(alert);

                        // creating intent to open alarm
                        Intent i = new Intent(mContext, DashboardActivity.class);
                        Log.i("Alarme Service - Seniors", "entityClass = " + alert.getEntityClass());
                        if (alert.getEntityClass().equals(Medication.class.getName())) {
                            i = new Intent(mContext, MedicationInfoActivity.class);
                            if (oMed != null) {
                                Log.i("Alarme Service - Seniors", "Setando bunde id para abrir alarme medicacao");
                                i.putExtra(MedicationInfoActivity.BUNDLE_ID, oMed.getID());
                            }
                        }
                        // else outras classes

                        Log.i("Alarme Service - Seniors", "Setando bunde alarme id: " + alert.getID());
                        i.putExtra(BUNDLE_ALERT_ID, alert.getID());
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

                        PendingIntent pni = PendingIntent.getActivity(mContext, (int) alert.getID(), i, PendingIntent.FLAG_UPDATE_CURRENT);

                        // Creating notification
                        Notification notification = new Notification.Builder(mContext)
                                .setContentTitle("Senior's App")
                                .setContentText(notificationMessage)
                                .setSmallIcon(R.drawable.ic_toolbar_seniors)
                                .setLights(Color.RED, 3000, 3000)
                                .setContentIntent(pni)
                                .getNotification();
                        notification.flags |= Notification.FLAG_AUTO_CANCEL;
                        notification.defaults |= Notification.DEFAULT_VIBRATE;
                        notification.defaults |= Notification.DEFAULT_SOUND;

                        // notifying
                        mManager.notify(0, notification);
                        // ringing!!!
                        mContext.startService(new Intent(mContext, AlarmPlayerService.class));
                    }
                }
            }
        }

        return retVal;
    }

    private Calendar getNextServiceTime(AlertEvent event, Medication mObj) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(event.getNextAlert().getTime());

        switch (mObj.getPeriodicity()) {
            case DIA_SIM_DIA_NAO:
                c.add(Calendar.HOUR, 48);
                break;
            case DIAx1:
                c.add(Calendar.HOUR, 24);
                break;
            case DIAx2:
                c.add(Calendar.HOUR, 12);
                break;
            case DIAx3:
                c.add(Calendar.HOUR, 8);
                break;
            case DIAx4:
                c.add(Calendar.HOUR, 6);
                break;
            case SEMANAx1:
                c.add(Calendar.HOUR, 24 * 7);
                break;
            case MESx1:
                c.add(Calendar.MONTH, 1);
                break;
        }

        return c;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

