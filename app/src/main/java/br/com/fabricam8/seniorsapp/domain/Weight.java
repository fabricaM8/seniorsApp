package br.com.fabricam8.seniorsapp.domain;

import android.content.ContentValues;

import java.util.Date;

/**
 * Created by Aercio on 7/2/15.
 * <p/>
 */
public class Weight extends DbEntity {

    // Entity Columns names
    public static final String KEY_VALUE = "weight";
    public static final String KEY_DATE = "date";

    // Entity attributes
    private float value;
    private Date date;

    // constructors
    public Weight(){

    }

    @Override
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();

        values.put(KEY_CLOUD_ID, getCloudId());
        values.put(KEY_VALUE, getValue());
        values.put(KEY_DATE, getDate().getTime());

        return values;
    }


    @Override
    public int hashCode() {
        return (int) (getID() * getValue() * getDate().getTime());
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
