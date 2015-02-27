package br.com.fabricam8.seniorsapp.domain;

import android.content.ContentValues;

import java.util.Date;

import br.com.fabricam8.seniorsapp.enums.ExerciseType;

/**
 * Created by laecy_000 on 22/02/2015.
 */
public class Exercise extends DbEntity {

    // Entity Columns names
    public static final String KEY_TYPE = "type";
    public static final String KEY_START_DATE = "startDate";
    public static final String KEY_END_DATA = "endDate";
    public static final String KEY_TIME = "time";


     // Entity attributes
    private ExerciseType type;                    // Tipo de atividade
    private Date startDate;                      // data inicial
    private Date endDate;                       // data final
    private String time;                          // TIME

    // constructors
    public Exercise() {
    }

    @Override
    public ContentValues getContentValues() {

        ContentValues values = new ContentValues();
        values.put(KEY_TYPE, getCloudId());
        values.put(KEY_START_DATE, getStartDate().getTime());
        values.put(KEY_END_DATA, getStartDate().getTime());
        return values;
    }

    public ExerciseType getType() {
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

    public  void setTime(String time)
    {
        this.time = time;

    }

    public  String getTime()
    {
       return time;
     }


}


