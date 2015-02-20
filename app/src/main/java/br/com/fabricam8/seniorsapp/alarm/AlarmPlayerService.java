package br.com.fabricam8.seniorsapp.alarm;

import android.app.Service;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Aercio on 1/27/15.
 */
public class AlarmPlayerService extends Service {

    private Ringtone ringtone;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.ringtone = RingtoneManager.getRingtone(this, getAvailabeSound());
        ringtone.play();

        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                ringtone.stop();
                timer.cancel();
            }
        }, 10000);
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        ringtone.stop();
    }

    private Uri getAvailabeSound() {
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

        if (soundUri == null) {
            // soundUri is null, using backup
            soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            // I can't see this ever being null (as always have a default notification)
            // but just incase
            if (soundUri == null) {
                // soundUri backup is null, using 2nd backup
                soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            }
        }

        return soundUri;
    }
}
