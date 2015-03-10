package br.com.fabricam8.seniorsapp.domain;

import android.content.ContentValues;

import java.util.Date;

import br.com.fabricam8.seniorsapp.enums.ConsultationType;


/**
 * Created by laecy_000 on 09/03/2015.
 */
public class Consultation extends DbEntity {



    // Entity Columns names
    public static final String KEY_NAME = "name";
    public static final String KEY_DETALIS = "details";
    public static final String KEY_START_DATE = "start_date";
    public static final String KEY_REMEMBER = "remember";
    public static final String KEY_TIME = "timer";



    // Entity attributes
    private ConsultationType type;  // Tipo de atividade
    private Date startDate;     // data inicial
    private String name;  // Tipo de atividade
    private String details;  // Tipo de atividade
    private String remember;  // Tipo de atividade
    private String time;


    @Override
    public ContentValues getContentValues() {

        ContentValues values = new ContentValues();
        values.put(KEY_CLOUD_ID, getCloudId());
        values.put(KEY_NAME, getName());
        values.put(KEY_START_DATE, getKeyStartDate());
        values.put(KEY_DETALIS, getDetails());
        values.put(KEY_REMEMBER, getRemember());
        return values;
    }


    public String getDetails() {
        return details;
    }

    public String getName() {
        return name;
    }

    public static String getKeyStartDate() {
        return KEY_START_DATE;
    }

    public String getRemember() {
        return remember;
    }

    public ConsultationType getType() {
        return type;
    }


    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    @Override
    public long getID() {
        return super.getID();
    }


}



