package br.com.fabricam8.domain;

import android.content.ContentValues;

import br.com.fabricam8.enums.Dosage;

/**
 * Created by Aercio on 1/27/15.
 */
public class Medication extends AlertEvent {

    // Entity Columns names
    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_DESCRIPTION = "description";

    // Entity attributes
    private String description;

    private int dosage;

    private Dosage dosageType;


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
        values.put(KEY_NAME, getName());
        values.put(KEY_DESCRIPTION, getDescription());

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
}
