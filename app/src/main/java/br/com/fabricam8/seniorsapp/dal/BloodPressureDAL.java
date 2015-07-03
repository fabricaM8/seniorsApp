package br.com.fabricam8.seniorsapp.dal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.fabricam8.seniorsapp.domain.BloodPressure;

/**
 * Created by Aercio on 1/27/15.
 */
public class BloodPressureDAL extends DbCRUD<BloodPressure> {

    public static final String TABLE_NAME = "BloodPressure";

    private static BloodPressureDAL _instance;

    private BloodPressureDAL(Context context) {
        super(context);
    }

    public static synchronized BloodPressureDAL getInstance(Context context) {
        if (_instance == null) {
            _instance = new BloodPressureDAL(context);
        }

        return _instance;
    }

    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public long create(BloodPressure entity) {
        long iRetVal = -1;

        SQLiteDatabase db = null;
        try {
            // opening db
            db = this.getWritableDatabase();
            // getting content
            ContentValues values = entity.getContentValues();
            // Inserting Row
            iRetVal = db.insert(getTableName(), null, values);
        } catch (Exception ex) {
            Log.e("Seniors DB", ex.getMessage());
            iRetVal = -1;
        } finally {
            if (db != null && db.isOpen())
                db.close();
        }

        return iRetVal;
    }

    @Override
    public BloodPressure findOne(long id) {
        return findOne(BloodPressure.KEY_ID + "=?", new String[]{String.valueOf(id)});
    }

    private BloodPressure findOne(String selection, String[] selectionArgs) {
        BloodPressure oRetVal = null;

        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.getReadableDatabase();

            cursor = db.query(getTableName(), new String[]{
                    BloodPressure.KEY_ID,
                    BloodPressure.KEY_SYSTOLIC,
                    BloodPressure.KEY_DIASTOLIC,
                    BloodPressure.KEY_DATE
            }, selection, selectionArgs, null, null, null, null);

            if (cursor != null)
                cursor.moveToFirst();

            oRetVal = new BloodPressure();
            oRetVal.setID(cursor.getInt(0));
            oRetVal.setSystolic(cursor.getInt(1));
            oRetVal.setDiastolic(cursor.getInt(2));
            oRetVal.setDate(new Date(cursor.getLong(3)));

        } catch (Exception ex) {
            Log.e("Seniors DB", ex.getMessage());
            oRetVal = null;
        } finally {
            if (cursor != null)
                cursor.close();

            if (db != null && db.isOpen())
                db.close();
        }

        return oRetVal;
    }

    @Override
    public List<BloodPressure> findAll() {
        List<BloodPressure> lstRetVal = new ArrayList<>();

        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            // Select All Query
            String selectQuery = "SELECT %1$s, %2$s, %3$s, %4$s FROM "
                    + getTableName();
            selectQuery = String.format(selectQuery, BloodPressure.KEY_ID, BloodPressure.KEY_SYSTOLIC,
                    BloodPressure.KEY_DIASTOLIC, BloodPressure.KEY_DATE);
            Log.i("Seniors db - query", selectQuery);

            db = this.getWritableDatabase();
            cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    BloodPressure entity = new BloodPressure();
                    entity.setID(cursor.getInt(0));
                    entity.setSystolic(cursor.getInt(1));
                    entity.setDiastolic(cursor.getInt(2));
                    entity.setDate(new Date(cursor.getLong(3)));

                    // Adding entity to list
                    lstRetVal.add(entity);
                } while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            Log.e("Seniors DB - find all", ex.getMessage());
            // reset list
            lstRetVal = null;
        } finally {
            if (cursor != null)
                cursor.close();

            if (db != null && db.isOpen())
                db.close();
        }

        // return entity list
        return lstRetVal;
    }

    @Override
    public int update(BloodPressure entity) {
        int iRetVal = 0;

        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();

            ContentValues values = entity.getContentValues();
            // updating row
            iRetVal = db.update(getTableName(), values, BloodPressure.KEY_ID + " = ?",
                    new String[]{String.valueOf(entity.getID())});
        } catch (Exception ex) {
            Log.e("Seniors DB - update", ex.getMessage());
        } finally {
            if (db != null && db.isOpen())
                db.close();
        }

        return iRetVal;
    }

    @Override
    public int remove(BloodPressure entity) {
        int iRetVal = 0;

        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();

            iRetVal = db.delete(getTableName(), BloodPressure.KEY_ID + " = ?",
                    new String[]{String.valueOf(entity.getID())});
        } catch (Exception ex) {
            Log.e("Seniors DB - delete", ex.getMessage());
        } finally {
            if (db != null && db.isOpen())
                db.close();
        }

        return iRetVal;
    }
}
