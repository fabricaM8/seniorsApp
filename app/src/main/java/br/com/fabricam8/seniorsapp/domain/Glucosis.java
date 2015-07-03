package br.com.fabricam8.seniorsapp.domain;

import android.content.ContentValues;

import java.util.Date;

/**
 * Created by Aercio on 7/2/15.
 * <p/>
 */
public class Glucosis extends DbEntity {

    // Entity Columns names
    public static final String KEY_RATE = "rate";
    public static final String KEY_DATE = "date";

    // Entity attributes
    private int rate;
    private Date date;

    // constructors
    public Glucosis(){

    }

    @Override
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();

        values.put(KEY_CLOUD_ID, getCloudId());
        values.put(KEY_RATE, getRate());
        values.put(KEY_DATE, getDate().getTime());

        return values;
    }


    @Override
    public int hashCode() {
        return (int) (getID() * getRate() * getDate().getTime());
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
