package br.com.fabricam8.seniorsapp.dal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.com.fabricam8.seniorsapp.domain.Medication;

/**
 * Created by Aercio on 1/27/15.
 */
public class MedicationDAL extends DbCRUD<Medication> {


    private static MedicationDAL _instance;

    public static synchronized MedicationDAL getInstance(Context context) {
        if (_instance == null) {
            _instance = new MedicationDAL(context);
        }

        return _instance;
    }

    private MedicationDAL(Context context) {
        super(context);
    }


    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String tableSchema = "CREATE TABLE " + getTableName() + "("+Medication.KEY_ID+" INTEGER PRIMARY KEY,"
                + Medication.KEY_NAME+" TEXT,"
                + Medication.KEY_DESCRIPTION+" TEXT)";
        db.execSQL(tableSchema);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + getTableName());

        // Create tables again
        onCreate(db);
    }

    public String getTableName() {
        return "Medication";
    }

    @Override
    public void create(Medication entity) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = entity.getContentValues();

        // Inserting Row
        db.insert(getTableName(), null, values);
        db.close(); // Closing database connection
    }

    @Override
    public Medication findOne(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(getTableName(), new String[] { Medication.KEY_ID,
                        Medication.KEY_NAME, Medication.KEY_DESCRIPTION }, Medication.KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Medication entity = new Medication(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2));
        // return entity
        return entity;
    }

    @Override
    public List<Medication> findAll() {
        List<Medication> retList = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + getTableName();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Medication entity = new Medication();
                entity.setID(Integer.parseInt(cursor.getString(0)));
                entity.setName(cursor.getString(1));
                entity.setDescription(cursor.getString(2));
                // Adding entity to list
                retList.add(entity);
            } while (cursor.moveToNext());
        }

        // return entity list
        return retList;
    }

    @Override
    public int update(Medication entity) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = entity.getContentValues();

        // updating row
        return db.update(getTableName(), values, Medication.KEY_ID + " = ?",
                new String[] { String.valueOf(entity.getID()) });
    }

    @Override
    public void remove(Medication entity) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(getTableName(), Medication.KEY_ID + " = ?",
                new String[] { String.valueOf(entity.getID()) });
        db.close();
    }
}
