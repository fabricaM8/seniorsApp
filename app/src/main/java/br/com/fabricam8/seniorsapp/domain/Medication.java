package br.com.fabricam8.seniorsapp.domain;

import android.content.ContentValues;

import br.com.fabricam8.seniorsapp.enums.Dosage;

/**
 * Created by Aercio on 1/27/15.
 *
 *
 * Tomar 1 Comprimido, a cada 8 horas, durante 7 dias
 *       |       |            |                |
 *   Dosagem   Tipo Dose   Periodicidade      Duracao
 *
 */
public class Medication extends AlertEvent {

    // Entity Columns names
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_DOSAGE = "dosage";
    public static final String KEY_DOSAGE_TYPE = "dosage_type";
    public static final String KEY_PERIODICITY = "periodicity";
    public static final String KEY_DURATION = "duration";

    // Entity attributes
    private String description;

    private Dosage dosageType;

    private int dosage;

    private int periodicity;

    private int duration;

    // constructors
    public Medication()
    {
        this("", "");
    }

    // constructors
    public Medication(String name, String description)
    {
        this(0, name, description);
    }
    public Medication(int id, String name, String description)
    {
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
        values.put(KEY_NEXT_ALERT, getNextAlert().getTime());

        return values;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDosage() { return dosage; }

    public void setDosage(int dosage) { this.dosage = dosage; }

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


    @Override
    public int hashCode() {
        return getID() * (getDuration()>0?getDuration():1) * getName().length() ;
    }

    @Override
    public String toString() {
        return String.format("Tomar %1$d %2$s de %5$s, a cada %3$d horas, por %4$s dias",
                getDosage(), getDosageType().toString(), getPeriodicity(), getDuration(), getName());
    }
}
