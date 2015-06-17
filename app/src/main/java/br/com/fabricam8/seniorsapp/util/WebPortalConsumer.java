package br.com.fabricam8.seniorsapp.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Aercio on 6/15/15.
 */
public class WebPortalConsumer {

    private final String remoteUrl = "http://www.loja.srv.br:8090/seniors-web/api/";

    private Context mContext;

    private static WebPortalConsumer _instance;

    private WebPortalConsumer(Context context) {
        mContext = context;
    }

    public static synchronized WebPortalConsumer getInstance(Context context) {
        if (_instance == null)
            _instance = new WebPortalConsumer(context);

        return _instance;
    }

    /**
     * Register senior on web portal
     */
    public void registerSenior() {
        if(isProfileSet()) {
            // if registered
            if (!isRegistrationSyncd()) {
                final SharedPreferences prefs = getSharedPrefs();

                String name = prefs.getString(GlobalParams.SHARED_PROPERTY_REG_NAME, "");
                String cloudId = prefs.getString(GlobalParams.SHARED_PROPERTY_GCM_REG_ID, "");
                String phone = prefs.getString(GlobalParams.SHARED_PROPERTY_REG_PHONE, "");

                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("reg", cloudId);
                params.put("phone", phone);

                try {

                    JSONParser jp = new JSONParser(remoteUrl + "mobile/senior", params);
                    jp.execute();
//                    HttpResponse response = makeRequest(remoteUrl + "mobile/senior", params);
//                    if (response.getStatusLine().getStatusCode() == 200) {
//
//                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * @return Application's {@code SharedPreferences}.
     */
    private SharedPreferences getSharedPrefs() {
        return mContext.getSharedPreferences(GlobalParams.SHARED_PREFS_ID, Context.MODE_PRIVATE);
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

    /**
     * Gets the situation of the profile, if there is one online.
     * <p>
     * If result is false, user needs to go to the profile page
     */
    private boolean isRegistrationSyncd() {
        final SharedPreferences prefs = getSharedPrefs();
        return prefs.getBoolean(GlobalParams.SHARED_PROPERTY_PROFILE_SYNC, false);
    }
}
