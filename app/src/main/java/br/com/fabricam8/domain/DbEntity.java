package br.com.fabricam8.domain;

import android.content.ContentValues;

/**
 * Created by Aercio on 1/27/15.
 */
public abstract class DbEntity {

    private int id;

    public DbEntity() {}

    public abstract ContentValues getContentValues();


    public int getID() {
        return this.id;
    }

    public void setID(int id) {
         this.id = id;
    }
}
