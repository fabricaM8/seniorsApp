package br.com.fabricam8.seniorsapp.domain;

import android.content.ContentValues;

/**
 * Created by laecy_000 on 22/02/2015.
 */
public class Contacts extends DbEntity {

    // Entity Columns names
    public static final String KEY_NAME1 = "name1";
    public static final String KEY_FONE1 = "fone1";
    public static final String KEY_NAME2 = "name2";
    public static final String KEY_FONE2 = "fone2";

    // Entity attributes
    private String name1;             // Nome do contato
    private String fone1;             // fone1
    private String name2;             // Nome do contato
    private String fone2;             // fone2

    // constructors
    public Contacts() {
    }

    @Override
    public ContentValues getContentValues()
    {
        ContentValues values = new ContentValues();
        values.put(KEY_CLOUD_ID, getCloudId());
        values.put(KEY_NAME1, getName1());
        values.put(KEY_FONE1, getFone1());
        values.put(KEY_NAME2, getName());
        values.put(KEY_FONE2, getFone1());


        return values;
    }

    public String getName1() {
        return name1;
    }

    public String getName2() {
        return name2;
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

    public void setNome1(String nome1) {
        this.fone1 = nome1;
    }
    public void setNome2(String nome2) {
        this.fone1 = nome2;
    }


}

