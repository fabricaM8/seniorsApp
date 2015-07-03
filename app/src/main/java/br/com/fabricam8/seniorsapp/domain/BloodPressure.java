package br.com.fabricam8.seniorsapp.domain;

import android.content.ContentValues;

import java.util.Date;

/**
 * Created by Aercio on 7/2/15.
 * <p/>
 */
public class BloodPressure extends DbEntity {

    // Entity Columns names
    public static final String KEY_SYSTOLIC = "systolic";
    public static final String KEY_DIASTOLIC = "diastolic";
    public static final String KEY_DATE = "date";

    // Entity attributes
    private int systolic;
    private int diastolic;
    private Date date;

    // constructors
    public BloodPressure(){

    }

    @Override
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();

        values.put(KEY_CLOUD_ID, getCloudId());
        values.put(KEY_SYSTOLIC, getSystolic());
        values.put(KEY_DIASTOLIC, getDiastolic());
        values.put(KEY_DATE, getDate().getTime());

        return values;
    }


    @Override
    public int hashCode() {
        return (int) (getID() * getSystolic() * getDiastolic() * getDate().getTime());
    }

    public int getSystolic() {
        return systolic;
    }

    public void setSystolic(int systolic) {
        this.systolic = systolic;
    }

    public int getDiastolic() {
        return diastolic;
    }

    public void setDiastolic(int diastolic) {
        this.diastolic = diastolic;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
