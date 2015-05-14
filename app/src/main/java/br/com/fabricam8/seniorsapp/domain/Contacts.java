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
        values.put(KEY_NAME2, getName2());
        values.put(KEY_FONE2, getFone1());


        return values;
    }

    public void setName1(String name1) {
        this.name1 = name1;
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


    public void setName2(String name2) {
        this.name2 = name2;
    }
}

