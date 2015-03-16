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
    public static final String KEY_TYPE = "consultation_type";

    // Entity attributes
    private String name;  // Tipo de atividade
    private String details;  // Tipo de atividade
    private Date startDate;     // data inicial
    private ConsultationType consultation_type;  // Tipo de atividade

    // constructors
    public Consultation() {
    }

    @Override
    public ContentValues getContentValues() {

        ContentValues values = new ContentValues();
        values.put(KEY_CLOUD_ID, getCloudId());
        values.put(KEY_NAME, getName());
        values.put(KEY_DETALIS, getDetails());
        values.put(KEY_START_DATE, getStartDate().getTime());
        values.put(KEY_TYPE, getRememberType().getValue());
        return values;
  }

    public void setConsultation_type(ConsultationType consultation_type) {
        this.consultation_type = consultation_type;
    }

    public String getDetails() {
        return details;
    }

    public String getName() {
        return name;
    }

    public Date getStartDate() {
        return startDate;
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


    @Override
    public long getID() {
        return super.getID();
    }


    public ConsultationType getRememberType() {
        return consultation_type;
    }

    @Override
    public String toString() {
        return String.format("%1$s ",
                getRememberType().toString());
    }

}



