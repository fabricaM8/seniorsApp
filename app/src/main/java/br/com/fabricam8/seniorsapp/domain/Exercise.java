package br.com.fabricam8.seniorsapp.domain;

import android.content.ContentValues;

import java.util.Date;

import br.com.fabricam8.seniorsapp.enums.ExerciseType;

/**
 * Created by laecy_000 on 22/02/2015.
 */
public class Exercise extends DbEntity {

    // Entity Columns names
    public static final String KEY_TYPE = "exercise_type";
    public static final String KEY_START_DATE = "start_date";
    public static final String KEY_END_DATE = "end_date";
    public static final String KEY_MONDAY    = "monday";
    public static final String KEY_SUNDAY    = "sunday";
    public static final String KEY_TUESDAY   = "tuesday";
    public static final String KEY_WEDNESDAY    = "wednesday";
    public static final String KEY_THUSDAY = "thursday";
    public static final String KEY_FRIDAY = "friday";
    public static final String KEY_SATURDAY = "saturday";


    // Entity attributes
    private ExerciseType type;  // Tipo de atividade
    private Date startDate;     // data inicial
    private Date endDate;       // data final
    private String monday;  // Tipo de atividade
    private String tuesday;  // Tipo de atividade
    private String wednesday;  // Tipo de atividade
    private String thursday;  // Tipo de atividade
    private String friday;  // Tipo de atividade
    private String saturday;  // Tipo de atividade
    private String sunday;  // Tipo de atividade




    // constructors
    public Exercise() {
    }

    @Override
    public ContentValues getContentValues() {

        ContentValues values = new ContentValues();
        values.put(KEY_CLOUD_ID, getCloudId());
        values.put(KEY_TYPE, getMeasureType().getValue());
        values.put(KEY_START_DATE, getStartDate().getTime());
        values.put(KEY_END_DATE, getEndDate().getTime());
        values.put(KEY_SUNDAY, getSunday());
        values.put(KEY_MONDAY, getMonday());
        values.put(KEY_TUESDAY,getThusday());
        values.put(KEY_WEDNESDAY, getWednesday());
        values.put(KEY_THUSDAY, getThusday());
        values.put(KEY_FRIDAY, getFriday());
        values.put(KEY_SATURDAY, getSaturday());
        return values;
    }
    public String getThusday() {
        return thursday;
    }
 
    public String getMonday() {
        return monday;
    }

    public String getWednesday() {
        return wednesday;
    }

    public String getFriday() {
        return friday;
    }

    public String getSaturday() {
        return saturday;
    }

    public String getSunday() {
        return sunday;
    }

    public String getThursday() {
        return thursday;
    }

    public ExerciseType getMeasureType() {
        return type;
    }

    public void setType(ExerciseType type) {
        this.type = type;
    }




    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    @Override
    public String toString() {
        return String.format("%1$s ",
                getMeasureType().toString());
    }



 }




