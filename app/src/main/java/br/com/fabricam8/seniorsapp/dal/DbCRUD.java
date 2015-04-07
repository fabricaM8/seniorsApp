package br.com.fabricam8.seniorsapp.dal;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.List;

import br.com.fabricam8.seniorsapp.domain.DbEntity;

/**
 * Created by Aercio on 1/27/15.
 */
public abstract class DbCRUD<T extends DbEntity> extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 24;

    // Database Name
    private static final String DATABASE_NAME = "seniors_db";

    Context mContext;

    public DbCRUD(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        SchemaHelper.createTable(db);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        SchemaHelper.upgradeSchema(db, oldVersion, newVersion);
    }

    public abstract String getTableName();

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new entity
    public abstract long create(T entity);

    // Getting single entity
    public abstract T findOne(long id);

    // Getting All entities
    public abstract List<T> findAll();

    // Updating single entity
    public abstract int update(T entity);

    // Deleting single entity
    public abstract int remove(T entity);

    // Getting entities Count
    public int count() {
        int iRetVal = 0;

        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            String countQuery = "SELECT * FROM " + getTableName();
            db = this.getReadableDatabase();
            cursor = db.rawQuery(countQuery, null);
            // getting count
            iRetVal = cursor.getCount();
        } catch (Exception ex) {
            Log.e("Seniors DB - count", ex.getMessage());
        } finally {
            if (cursor != null)
                cursor.close();

            if (db != null && db.isOpen())
                db.close();
        }

        return iRetVal;
    }
}