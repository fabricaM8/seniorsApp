package br.com.fabricam8.seniorsapp.domain;

import android.content.ContentValues;

/**
 * Created by laecy_000 on 22/02/2015.
 */
public class EmergencyContact extends DbEntity {

    // Entity Columns names
    public static final String KEY_NAME = "name";
    public static final String KEY_PHONE = "phone";

    // Entity attributes
    private String name;             // Nome do contato
    private String phone;            // fone1

    // constructors
    public EmergencyContact() {
    }

    @Override
    public ContentValues getContentValues()
    {
        ContentValues values = new ContentValues();
        values.put(KEY_CLOUD_ID, getCloudId());
        values.put(KEY_NAME, getName());
        values.put(KEY_PHONE, getPhone());
        return values;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}

