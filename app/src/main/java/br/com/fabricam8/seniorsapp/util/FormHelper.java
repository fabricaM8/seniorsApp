package br.com.fabricam8.seniorsapp.util;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;

import java.lang.reflect.Field;

/**
 * Created by Aercio on 2/2/15.
 */
public class FormHelper {

    public static void setTextBoxValue(Activity ctx, int viewId, String value) {
        View v = ctx.findViewById(viewId);
        if (v != null && v instanceof TextView) {
            ((TextView) v).setText(value);
        }
    }

    public static String getTextBoxValue(Activity ctx, int viewId) {
        String strRetVal = "";

        View v = ctx.findViewById(viewId);
        if (v != null && v instanceof TextView) {
            strRetVal = ((TextView) v).getText().toString().trim();
        }

        return strRetVal;
    }

    public static boolean getCheckBoxValue(Activity ctx, int viewId) {
        boolean fRetVal = false;

        View v = ctx.findViewById(viewId);
        if (v != null && v instanceof CheckBox) {
            fRetVal = ((CheckBox) v).isChecked();
        }

        return fRetVal;
    }

    public static boolean getSwitchValue(View parentiew, int viewId) {
        boolean fRetVal = false;

        View v = parentiew.findViewById(viewId);
        if (v != null && v instanceof Switch) {
            fRetVal = ((Switch) v).isChecked();
        }

        return fRetVal;
    }

    public static boolean getSwitchValue(Activity ctx, int viewId) {
        boolean fRetVal = false;

        View v = ctx.findViewById(viewId);
        if (v != null && v instanceof Switch) {
            fRetVal = ((Switch) v).isChecked();
        }

        return fRetVal;
    }

    public static void setSwitchValue(Activity ctx, int viewId, boolean value) {
        View v = ctx.findViewById(viewId);
        if (v != null && v instanceof Switch) {
            ((Switch) v).setChecked(value);
        }
    }

    public static int getTextBoxValueAsInt(Activity ctx, int viewId) {
        int iRetVal = -1;

        String val = getTextBoxValue(ctx, viewId);
        if (val.length() > 0) {
            try {
                iRetVal = Integer.parseInt(val);
            } catch (NumberFormatException nfex) {
                Log.e("Seniors app", "Error parsing number: " + val + " - " + nfex.getMessage());
                iRetVal = -1;
            }
        }

        return iRetVal;
    }

    public static boolean validateFormTextInput(Activity ctx, int resourceId, String errMessage) {
        if (getTextBoxValue(ctx, resourceId).length() == 0) {
            EditText v = (EditText) ctx.findViewById(resourceId);
            v.setFocusableInTouchMode(true);
            v.requestFocus();
            v.setError(errMessage);
            return false;
        }
        return true;
    }

    public static void setupPicker(View parentView, int resourceId, int minValue, int maxValue,
                                   String[] displayValues, int initialValue) {
        NumberPicker pck =  (NumberPicker) parentView.findViewById(resourceId);
        pck.setMaxValue(maxValue);
        pck.setMinValue(minValue);
        if(displayValues != null)
            pck.setDisplayedValues(displayValues);

        if(initialValue != -1)
            pck.setValue(initialValue);

        // ajustando cor do picker
        setNumberPickerTextColor(pck, Color.argb(255, 22, 22, 22));
    }

    public static boolean setNumberPickerTextColor(NumberPicker numberPicker, int color)
    {
        final int count = numberPicker.getChildCount();
        for(int i = 0; i < count; i++){
            View child = numberPicker.getChildAt(i);
            if(child instanceof EditText){
                try{
                    Field selectorWheelPaintField = numberPicker.getClass()
                            .getDeclaredField("mSelectorWheelPaint");
                    selectorWheelPaintField.setAccessible(true);
                    ((Paint)selectorWheelPaintField.get(numberPicker)).setColor(color);
                    ((EditText)child).setTextColor(color);
                    numberPicker.invalidate();
                    return true;
                }
                catch(NoSuchFieldException e){
                    Log.w("setNumberPickerTextColor", e);
                }
                catch(IllegalAccessException e){
                    Log.w("setNumberPickerTextColor", e);
                }
                catch(IllegalArgumentException e){
                    Log.w("setNumberPickerTextColor", e);
                }
            }
        }
        return false;
    }


    public static int getPickerValue(View parentView, int resourceId) {
        int iRetVal = -1;

        View v = parentView.findViewById(resourceId);
        if (v != null && v instanceof NumberPicker) {
            iRetVal = ((NumberPicker) v).getValue();
        }

        return iRetVal;
    }

}
