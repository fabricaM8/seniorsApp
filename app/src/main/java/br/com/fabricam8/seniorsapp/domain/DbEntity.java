package br.com.fabricam8.seniorsapp.domain;

import android.content.ContentValues;

/**
 * Created by Aercio on 1/27/15.
 */
public abstract class DbEntity {

    // Entity Columns names
    public static final String KEY_ID = "id";
    public static final String KEY_CLOUD_ID = "cloud_id";
    public static final String KEY_NAME = "name";

    private long id;
    private int cloudId;
    private String name;

    public DbEntity() {
    }

    public abstract ContentValues getContentValues();

    public long getID() {
        return this.id;
    }

    public void setID(long id) {
        this.id = id;
    }

    public int getCloudId()
    {
        return cloudId;
    }

    public void setCloudId(int cloudId) {
        this.cloudId = cloudId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
