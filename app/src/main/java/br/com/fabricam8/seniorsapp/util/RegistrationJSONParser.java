package br.com.fabricam8.seniorsapp.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Map;

import br.com.fabricam8.seniorsapp.R;

/**
 * Created by Aercio on 6/15/15.
 */
public class RegistrationJSONParser extends AsyncTask<String, Void, String> {

    private static String url = null;
    private static Map<String,String> jParams = null;
    private static InputStream is = null;
    private static String json = "";
    private static Context mContext = null;

    // constructor
    public RegistrationJSONParser(Context ctx, String _url, Map<String, String> params) {
        url = _url;
        jParams = params;
        mContext = ctx;
    }

    @Override
    protected String doInBackground(String... params) {

        // Making HTTP request
        try {
            HttpResponse getResponse = null;
            try {
                getResponse = makeRequest(url, jParams);
            } catch (Exception e) {
                e.printStackTrace();
            }
            final int statusCode = getResponse.getStatusLine().getStatusCode();

            if (statusCode != HttpStatus.SC_OK) {
                Log.w(getClass().getSimpleName(),
                        "Error " + statusCode + " for URL " + url);
                return null;
            }

            HttpEntity getResponseEntity = getResponse.getEntity();
            is = getResponseEntity.getContent();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            Log.d("IO", e.getMessage().toString());
            e.printStackTrace();
        } catch (Exception e) {
            Log.d("General", e.getMessage().toString());
            e.printStackTrace();
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            json = sb.toString();
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }

        return json;
    }

    protected void onPostExecute(String json)
    {
        if(json != null && !json.trim().isEmpty()) {
            // inicializando shared prefs
            final SharedPreferences prefs = getSharedPrefs();
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(GlobalParams.SHARED_PROPERTY_REG_CLOUD_ID, json.trim().toUpperCase());
            editor.putBoolean(GlobalParams.SHARED_PROPERTY_PROFILE_SYNC, true);
            editor.apply();
        }
    }

    /**
     * Makes a request to the remote endpoint with a json object.
     */
    private HttpResponse makeRequest(String path, Map params) throws Exception
    {
        //instantiates httpclient to make request
        DefaultHttpClient httpclient = new DefaultHttpClient();

        //url with the post data
        HttpPost httpPost = new HttpPost(path);

        //convert parameters into JSON object
        JSONObject holder = getJsonObjectFromMap(params);

        //passes the results to a string builder/entity
        StringEntity se = new StringEntity(holder.toString());

        //sets the post request as the resulting string
        httpPost.setEntity(se);
        //sets a request header so the page receving the request
        //will know what to do with it
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");

        return httpclient.execute(httpPost);//, responseHandler);
    }

    /**
     * Transforms a map into a Json object
     *
     * @param params The json params
     * @return a json object
     */
    private JSONObject getJsonObjectFromMap(Map params) throws JSONException {

        //all the passed parameters from the post request
        //iterator used to loop through all the parameters
        //passed in the post request
        Iterator iter = params.entrySet().iterator();

        //Stores JSON
        JSONObject holder = new JSONObject();

        //While there is another entry
        while (iter.hasNext())
        {
            //gets an entry in the params
            Map.Entry pairs = (Map.Entry)iter.next();

            //creates a key for Map
            String key = (String)pairs.getKey();
            String value = (String)pairs.getValue();

            holder.put(key, value);
        }
        return holder;
    }

    /**
     * @return Application's {@code SharedPreferences}.
     */
    private SharedPreferences getSharedPrefs() {
        return mContext.getSharedPreferences(GlobalParams.SHARED_PREFS_ID, Context.MODE_PRIVATE);
    }
}