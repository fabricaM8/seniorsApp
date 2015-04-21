package br.com.fabricam8.seniorsapp.domain;

import android.content.ContentValues;

/**
 * Created by laecy_000 on 22/02/2015.
 */
public class Contacts extends DbEntity {

    // Entity Columns names
    public static final String KEY_NAME = "name";
    public static final String KEY_FONE1 = "fone1";
    public static final String KEY_FONE2 = "fone2";


    // Entity attributes
    private String name;             // Nome do contato
    private String fone1;             // fone1
    private String fone2;             // fone2

    // constructors
    public Contacts() {
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(KEY_CLOUD_ID, getCloudId());
        values.put(KEY_NAME, getName());
        values.put(KEY_FONE1, getFone1());

        return values;
    }

    public String getFone1() {
        return fone1;
    }

    public String getFone2() {
        return fone2;
    }

    public void setFone1(String fone1) {
        this.fone1 = fone1;
    }

    public void setFone2(String fone2) {
        this.fone2 = fone2;
    }
}

