package br.com.fabricam8.seniorsapp.util;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Aercio on 2/2/15.
 */
public class FormHelper {

    public static void setTextBoxValue(Activity ctx, int viewId, String value)
    {
        View v = ctx.findViewById(viewId);
        if(v != null && v instanceof TextView) {
            ((TextView) v).setText(value);
        }
    }

    public static String getTextBoxValue(Activity ctx, int viewId)
    {
        String strRetVal = "";

        View v = ctx.findViewById(viewId);
        if(v != null && v instanceof TextView) {
            strRetVal = ((TextView) v).getText().toString();
        }

        return strRetVal;
    }

    public static int getTextBoxValueAsInt(Activity ctx, int viewId) {
        int iRetVal = -1;

        String val = getTextBoxValue(ctx, viewId);
        if(val.length() > 0) {
            try {
                iRetVal = Integer.parseInt(val);
            }
            catch (NumberFormatException nfex) {
                Log.e("Seniors app", "Error parsing number: " + val + " - " + nfex.getMessage());
                iRetVal = -1;
            }
        }

        return iRetVal;
    }

}
