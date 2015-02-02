package br.com.fabricam8.seniorsapp.dal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.fabricam8.seniorsapp.domain.Medication;
import br.com.fabricam8.seniorsapp.enums.Dosage;

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
        String tableSchema = "CREATE TABLE " + getTableName() + "("
                + Medication.KEY_ID + " INTEGER PRIMARY KEY,"
                + Medication.KEY_CLOUD_ID + " INTEGER,"
                + Medication.KEY_NAME + " TEXT,"
                + Medication.KEY_DESCRIPTION + " TEXT,"
                + Medication.KEY_DOSAGE + " INTEGER,"
                + Medication.KEY_DOSAGE_TYPE + " INTEGER,"
                + Medication.KEY_PERIODICITY + " INTEGER,"
                + Medication.KEY_DURATION + " INTEGER,"
                + Medication.KEY_NEXT_ALERT + " INTEGER"
                + ")";
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
    public long create(Medication entity) {
        long iRetVal = -1;

        SQLiteDatabase db = null;
        try {
            // opening db
            db = this.getWritableDatabase();
            // getting content
            ContentValues values = entity.getContentValues();
            // Inserting Row
            iRetVal = db.insert(getTableName(), null, values);
        }
        catch(Exception ex) {
            Log.e("Seniors DB", ex.getMessage());
            iRetVal = -1;
        }
        finally {
            if(db != null && db.isOpen())
                db.close();
        }

        return iRetVal;
    }

    @Override
    public Medication findOne(long id) {
        Medication oRetVal = null;

        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.getReadableDatabase();

            cursor = db.query(getTableName(), new String[] {
                    Medication.KEY_ID,
                    Medication.KEY_NAME,
                    Medication.KEY_DESCRIPTION,
                    Medication.KEY_DOSAGE,
                    Medication.KEY_DOSAGE_TYPE,
                    Medication.KEY_PERIODICITY,
                    Medication.KEY_DURATION,
                    Medication.KEY_NEXT_ALERT
            }, Medication.KEY_ID + "=?", new String[] { String.valueOf(id) }, null, null, null, null);

            if (cursor != null)
                cursor.moveToFirst();

            oRetVal = new Medication();
            oRetVal.setID(Integer.parseInt(cursor.getString(0)));
            oRetVal.setName(cursor.getString(1));
            oRetVal.setDescription(cursor.getString(2));
            oRetVal.setDosage(cursor.isNull(3) ? -1 : Integer.parseInt(cursor.getString(3)));
            oRetVal.setDosageType(cursor.isNull(4) ? Dosage.NONE : Dosage.fromInt(Integer.parseInt(cursor.getString(4))));
            oRetVal.setPeriodicity(cursor.isNull(5) ? -1 : Integer.parseInt(cursor.getString(5)));
            oRetVal.setDuration(cursor.isNull(6) ? -1 : Integer.parseInt(cursor.getString(6)));

            if(!cursor.isNull(7))
                oRetVal.setNextAlert(new Date(Long.parseLong(cursor.getString(7))));
        }
        catch(Exception ex) {
            Log.e("Seniors DB", ex.getMessage());
            oRetVal = null;
        }
        finally {
            if(cursor != null)
                cursor.close();

            if(db != null && db.isOpen())
                db.close();
        }

        return oRetVal;
    }

    @Override
    public List<Medication> findAll() {
        List<Medication> lstRetVal = new ArrayList<>();

        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            // Select All Query
            String selectQuery = "SELECT %1$s, %2$s, %3$s, %4$s, %5$s, %6$s, %7$s, %8$s FROM "
                    + getTableName();
            selectQuery = String.format(selectQuery, Medication.KEY_ID, Medication.KEY_NAME,
                    Medication.KEY_DESCRIPTION, Medication.KEY_DOSAGE, Medication.KEY_DOSAGE_TYPE,
                    Medication.KEY_PERIODICITY, Medication.KEY_DURATION, Medication.KEY_NEXT_ALERT);
            Log.i("Seniors db - query" , selectQuery);

            db = this.getWritableDatabase();
            cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    Medication entity = new Medication();
                    entity.setID(Integer.parseInt(cursor.getString(0)));
                    entity.setName(cursor.getString(1));
                    entity.setDescription(cursor.getString(2));
                    entity.setDosage(cursor.isNull(3) ? -1 : Integer.parseInt(cursor.getString(3)));
                    entity.setDosageType(cursor.isNull(4) ? Dosage.NONE : Dosage.fromInt(Integer.parseInt(cursor.getString(4))));
                    entity.setPeriodicity(cursor.isNull(5) ? -1 : Integer.parseInt(cursor.getString(5)));
                    entity.setDuration(cursor.isNull(6) ? -1 : Integer.parseInt(cursor.getString(6)));

                    if(!cursor.isNull(7))
                        entity.setNextAlert(new Date(Long.parseLong(cursor.getString(7))));

                    // Adding entity to list
                    lstRetVal.add(entity);
                } while (cursor.moveToNext());
            }
        }
        catch(Exception ex) {
            Log.e("Seniors DB - find all", ex.getMessage());
            // reset list
            lstRetVal = null;
        }
        finally {
            if(cursor != null)
                cursor.close();

            if(db != null && db.isOpen())
                db.close();
        }

        // return entity list
        return lstRetVal;
    }

    @Override
    public int update(Medication entity) {
        int iRetVal = 0;

        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();

            ContentValues values = entity.getContentValues();
            // updating row
            iRetVal = db.update(getTableName(), values, Medication.KEY_ID + " = ?",
                    new String[] { String.valueOf(entity.getID()) });
        }
        catch(Exception ex) {
            Log.e("Seniors DB - update", ex.getMessage());
        }
        finally {
            if(db != null && db.isOpen())
                db.close();
        }

        return iRetVal;
    }

    @Override
    public int remove(Medication entity) {
        int iRetVal = 0;

        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();

            iRetVal = db.delete(getTableName(), Medication.KEY_ID + " = ?",
                    new String[] { String.valueOf(entity.getID()) });
        }
        catch(Exception ex) {
            Log.e("Seniors DB - delete", ex.getMessage());
        }
        finally {
            if(db != null && db.isOpen())
                db.close();
        }

        return iRetVal;
    }
}