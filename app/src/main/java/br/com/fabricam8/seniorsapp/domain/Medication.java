package br.com.fabricam8.seniorsapp.domain;

import android.content.ContentValues;

import java.util.Date;

import br.com.fabricam8.seniorsapp.enums.DosageMeasure;
import br.com.fabricam8.seniorsapp.enums.Duration;
import br.com.fabricam8.seniorsapp.enums.Periodicity;

/**
 * Created by Aercio on 1/27/15.
 * <p/>
 * <p/>
 * Tomar 1 Comprimido, a cada 8 horas, durante 7 dias
 * |       |            |                |
 * Dosagem   Tipo Dose   Periodicidade      Duracao
 */
public class Medication extends DbEntity {

    // Entity Columns names
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_DOSAGE = "dosage";
    public static final String KEY_DOSAGE_TYPE = "dosage_type";
    public static final String KEY_PERIODICITY = "periodicity";
    public static final String KEY_DURATION = "duration";
    public static final String KEY_DURATION_TYPE = "duration_type";
    public static final String KEY_CONTINUOUS = "continuous";
    public static final String KEY_ALARM = "alarm";
    public static final String KEY_START_DATE = "start_date";

    // Entity attributes
    private String description;                 // observacoes
    private DosageMeasure dosageMeasureType;    // comprimido, dragea, ml, etc
    private String dosage;                      // 1 e 1/2 ; 1,5 ; 3/4 , etc
    private Periodicity periodicity;            // 1 x ao dia, 3 x dia, etc
    private int duration;                       // 1..99
    private Duration durationType;              // dias, semanas, meses
    private Date startDate;                     // data inicial
    private boolean continuosUse;               // Se uso é continuo, ou nao
    private boolean hasAlarm;                   // Se uso tem alarme

    // constructors
    public Medication() {
        this("", "");
    }

    // constructors
    public Medication(String name, String description) {
        this(0, name, description);
    }

    public Medication(int id, String name, String description) {
        setID(id);
        setName(name);
        setDescription(description);
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();

        values.put(KEY_CLOUD_ID, getCloudId());
        values.put(KEY_NAME, getName());
        values.put(KEY_DESCRIPTION, getDescription());
        values.put(KEY_DOSAGE, getDosage());
        values.put(KEY_DOSAGE_TYPE, getDosageMeasureType().getValue());
        values.put(KEY_PERIODICITY, getPeriodicity().getValue());
        values.put(KEY_DURATION, getDuration());
        values.put(KEY_DURATION_TYPE, getDurationType().getValue());
        values.put(KEY_CONTINUOUS, isContinuosUse() ? 1 : 0);
        values.put(KEY_ALARM, isHasAlarm() ? 1 : 0);
        values.put(KEY_START_DATE, getStartDate().getTime());

        return values;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public DosageMeasure getDosageMeasureType() {
        return dosageMeasureType;
    }

    public void setDosageMeasureType(DosageMeasure dosageMeasureType) {
        this.dosageMeasureType = dosageMeasureType;
    }

    public Periodicity getPeriodicity() {
        return periodicity;
    }

    public void setPeriodicity(Periodicity periodicity) {
        this.periodicity = periodicity;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Duration getDurationType() {
        return durationType;
    }

    public void setDurationType(Duration duration) {
        this.durationType = duration;
    }

    public boolean isContinuosUse() {
        return continuosUse;
    }

    public void setContinuosUse(boolean continuosUse) {
        this.continuosUse = continuosUse;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public boolean isHasAlarm() {
        return hasAlarm;
    }

    public void setHasAlarm(boolean hasAlarm) {
        this.hasAlarm = hasAlarm;
    }

    public int getNumOfAlarms() {
        if(!isHasAlarm())
            return 0;

        if (isContinuosUse())
            return AlertEvent.FOREVER;

        int durationFactor = 0;
        switch (durationType) {
            case DIA:
                durationFactor = 1;
                break;
            case SEMANA:
                durationFactor = 7;
                break;
            case MES:
                durationFactor = 30;
                break;
            case ANO:
                durationFactor = 365;
                break;
        }

        float periodFactor = 0;
        switch (periodicity) {
            case DIA_SIM_DIA_NAO:
                periodFactor = 0.5f;
                break;
            case DIAx1:
                periodFactor = 1f;
                break;
            case DIAx2:
                periodFactor = 2f;
                break;
            case DIAx3:
                periodFactor = 3f;
                break;
            case DIAx4:
                periodFactor = 4f;
                break;
            case SEMANAx1:
                periodFactor = 1.0f/7.0f;
                break;
            case MESx1:
                periodFactor = 1.0f/30.0f;
                break;
        }

        int intakePerDay = (int)Math.ceil(duration * durationFactor * periodFactor);

        return intakePerDay;
    }

    @Override
    public int hashCode() {
        return (int) getID() * (getDuration() > 0 ? getDuration() : 1) * getName().length();
    }

    @Override
    public String toString() {
        return String.format("%1$s %2$s, %3$s",
                getDosage(), getDosageMeasureType().toString(), getPeriodicity().toString());
    }
}
