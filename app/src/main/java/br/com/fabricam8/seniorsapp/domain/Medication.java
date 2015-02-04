package br.com.fabricam8.seniorsapp.domain;

import android.content.ContentValues;

import java.util.Date;

import br.com.fabricam8.seniorsapp.enums.Dosage;

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
    public static final String KEY_CONTINUOUS = "continuous";
    public static final String KEY_START_DATE = "start_date";

    // Entity attributes
    private String description;
    private Dosage dosageType;
    private int dosage;
    private int periodicity;
    private int duration;
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
        values.put(KEY_DOSAGE_TYPE, getDosageType().getValue());
        values.put(KEY_PERIODICITY, getPeriodicity());
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

    public int getDosage() {
        return dosage;
    }

    public void setDosage(int dosage) {
        this.dosage = dosage;
    }

    public Dosage getDosageType() {
        return dosageType;
    }

    public void setDosageType(Dosage dosageType) {
        this.dosageType = dosageType;
    }

    public int getPeriodicity() {
        return periodicity;
    }

    public void setPeriodicity(int periodicity) {
        this.periodicity = periodicity;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
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
            return -1;

        int intakePerDay = 0;
        if (getPeriodicity() <= 24)
            intakePerDay = getDosage() * (24 / getPeriodicity());

        return getDuration() * intakePerDay;
    }

    @Override
    public int hashCode() {
        return (int) getID() * (getDuration() > 0 ? getDuration() : 1) * getName().length();
    }

    @Override
    public String toString() {
        return String.format("Tomar %1$d %2$s de %5$s, a cada %3$d horas, por %4$s dias (%6$s)",
                getDosage(), getDosageType().toString(), getPeriodicity(), getDuration(), getName(),
                getStartDate().toString());
    }
}
