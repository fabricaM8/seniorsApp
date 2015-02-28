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


    // Entity attributes
    private ExerciseType type;  // Tipo de atividade
    private Date startDate;     // data inicial
    private Date endDate;       // data final

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
        return values;
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
}


