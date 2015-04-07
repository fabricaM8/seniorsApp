package br.com.fabricam8.seniorsapp.domain;

import android.content.ContentValues;

import java.util.Date;

import br.com.fabricam8.seniorsapp.enums.ReminderType;


/**
 * Created by laecy_000 on 09/03/2015.
 */
public class Consultation extends DbEntity {


    // Entity Columns names
    public static final String KEY_NAME = "name";
    public static final String KEY_DETAILS = "details";
    public static final String KEY_START_DATE = "start_date";
    public static final String KEY_TYPE = "consultation_type";

    // Entity attributes
    private String details;  // Tipo de atividade
    private Date startDate;     // data inicial
    private ReminderType reminderType;  // Tipo de atividade

    // constructors
    public Consultation() {
    }

    @Override
    public ContentValues getContentValues() {

        ContentValues values = new ContentValues();
        values.put(KEY_CLOUD_ID, getCloudId());
        values.put(KEY_NAME, getName());
        values.put(KEY_DETAILS, getDetails());
        values.put(KEY_START_DATE, getStartDate().getTime());
        values.put(KEY_TYPE, getReminderType().getValue());
        return values;
  }

    public void setReminderType(ReminderType reminderType) {
        this.reminderType = reminderType;
    }

    public String getDetails() {
        return details;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public ReminderType getReminderType() {
        return reminderType;
    }

    @Override
    public String toString() {
        return String.format("%1$s ",
                getReminderType().toString());
    }

}



