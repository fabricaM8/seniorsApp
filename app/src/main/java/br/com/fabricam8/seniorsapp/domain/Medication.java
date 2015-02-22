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
    public static final String KEY_START_DATE = "start_date";

    // Entity attributes
    private String description;
    private DosageMeasure dosageMeasureType;
    private String dosage;
    private Periodicity periodicity;
    private int duration;
    private Duration durationType;
    private Date startDate;
    private boolean continuosUse;

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
        values.put(KEY_START_DATE, getStartDate().getTime());
        values.put(KEY_CONTINUOUS, isContinuosUse() ? 1 : 0);

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

    public int getNumOfAlarms() {
        if (isContinuosUse())
            return AlertEvent.FOREVER;

        int intakePerDay = 0;
//        if (getPeriodicity() <= 24)
//            intakePerDay = getDosage() * (24 / getPeriodicity());

        return getDuration() * intakePerDay;
    }

    @Override
    public int hashCode() {
        return (int) getID() * (getDuration() > 0 ? getDuration() : 1) * getName().length();
    }

    @Override
    public String toString() {
        return "Tomar ... ";
//        return String.format("Tomar %1$s %2$s de %5$s, a cada %3$d horas, por %4$s dias (%6$s)",
//                getDosage(), getDosageMeasureType().toString(), getPeriodicity().toString(), getDuration(), getName(),
//                getStartDate().toString());
    }
}
