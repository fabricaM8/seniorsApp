package br.com.fabricam8.seniorsapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

import br.com.fabricam8.seniorsapp.util.GlobalParams;


public class SplashActivity extends Activity {

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    /**
     * Substitute you own sender ID here. This is the project number you got
     * from the API Console, as described in "Getting Started."
     */
    String SENDER_ID = "986768263859";

    /**
     * Tag used on log messages.
     */
    static final String TAG = "Seniors GCM";

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        context = SplashActivity.this;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // register app or continue
                registerInBackground();
            }
        }, 1500);

    }

    /**
     * Registers the application with GCM servers asynchronously.
     * <p>
     * Stores the registration ID and the app versionCode in the application's
     * shared preferences.
     */
    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if(getRegistrationId().equals("") && checkPlayServices()) {
                        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);
                        String regid = gcm.register(SENDER_ID);
                        // Persist the regID - no need to register again.
                        storeRegistrationId(regid);
                    }
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    Log.e(TAG, ex.getMessage());
                    // If there is an error, don't just keep trying to register.
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = null;
                if(isProfileSet()) {
                    i = new Intent(SplashActivity.this, DashboardActivity.class);
                } else {
                    i = new Intent(SplashActivity.this, ProfileRegistrationActivity.class);
                }

                startActivity(i);

                // close this activity
                finish();
            }
        }.execute(null, null, null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * @return Application's {@code SharedPreferences}.
     */
    private SharedPreferences getSharedPrefs() {
        return getSharedPreferences(GlobalParams.SHARED_PREFS_ID, Context.MODE_PRIVATE);
    }


    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    /**
     * Stores the registration ID and the app versionCode in the application's
     * {@code SharedPreferences}.
     *
     * @param regId registration ID
     */
    private void storeRegistrationId(String regId) {
        final SharedPreferences prefs = getSharedPrefs();
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(GlobalParams.SHARED_PROPERTY_REG_ID, regId);
        editor.commit();
    }

    /**
     * Gets the current registration ID for application on GCM service, if there is one.
     * <p>
     * If result is empty, the app needs to register.
     *
     * @return registration ID, or empty string if there is no existing
     *         registration ID.
     */
    private String getRegistrationId() {
        final SharedPreferences prefs = getSharedPrefs();
        String registrationId = prefs.getString(GlobalParams.SHARED_PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return registrationId;
        }

        return registrationId;
    }

    /**
     * Gets the situation of the profile, if there is one.
     * <p>
     * If result is false, user needs to go to the profile page
     */
    private boolean isProfileSet() {
        final SharedPreferences prefs = getSharedPrefs();
        return prefs.getBoolean(GlobalParams.SHARED_PROPERTY_PROFILE_SET, false);
    }
}
