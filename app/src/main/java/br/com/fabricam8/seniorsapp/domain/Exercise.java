package br.com.fabricam8.seniorsapp.domain;

import android.content.ContentValues;

import java.util.Date;

import br.com.fabricam8.seniorsapp.enums.DosageMeasure;
import br.com.fabricam8.seniorsapp.enums.Duration;
import br.com.fabricam8.seniorsapp.enums.Periodicity;

/**
 * Created by laecy_000 on 22/02/2015.
 */
public class Exercise   extends DbEntity{

    // Entity Columns names
    public static final String KEY_TYPE = "type";
    public static final String KEY_START_DATE = "start_date";
    public static final String KEY_END_DATA = "end_date";


    // Entity attributes
    private int type;                            // Tipo de atividade
    private Date start_date;                     // data inicial
    private Date end_date;                       // data final



/*
    // constructors
    public Exercise() {
        this("", "");
    }
*/
    @Override
    public ContentValues getContentValues() {

        ContentValues values = new ContentValues();
        values.put(KEY_TYPE, getCloudId());
        values.put(KEY_START_DATE, getStartDate().getTime());
        values.put(KEY_END_DATA, getStartDate().getTime());

        return values;
    }
    public Date getStartDate() {
        return start_date;
    }
    public Date getEndData() {
        return end_date;
    }


}
