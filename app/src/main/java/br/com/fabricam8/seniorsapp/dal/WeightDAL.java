package br.com.fabricam8.seniorsapp.dal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.fabricam8.seniorsapp.domain.Weight;

/**
 * Created by Aercio on 1/27/15.
 */
public class WeightDAL extends DbCRUD<Weight> {

    public static final String TABLE_NAME = "Weight";

    private static WeightDAL _instance;

    private WeightDAL(Context context) {
        super(context);
    }

    public static synchronized WeightDAL getInstance(Context context) {
        if (_instance == null) {
            _instance = new WeightDAL(context);
        }

        return _instance;
    }

    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public long create(Weight entity) {
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
    public Weight findOne(long id) {
        return findOne(Weight.KEY_ID + "=?", new String[]{String.valueOf(id)});
    }

    private Weight findOne(String selection, String[] selectionArgs) {
        Weight oRetVal = null;

        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.getReadableDatabase();

            cursor = db.query(getTableName(), new String[]{
                    Weight.KEY_ID,
                    Weight.KEY_VALUE,
                    Weight.KEY_DATE
            }, selection, selectionArgs, null, null, null, null);

            if (cursor != null)
                cursor.moveToFirst();

            oRetVal = new Weight();
            oRetVal.setID(cursor.getInt(0));
            oRetVal.setValue(cursor.getFloat(1));
            oRetVal.setDate(new Date(cursor.getLong(2)));

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
    public List<Weight> findAll() {
        List<Weight> lstRetVal = new ArrayList<>();

        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            // Select All Query
            String selectQuery = "SELECT %1$s, %2$s, %3$s FROM "
                    + getTableName() + " ORDER BY " + Weight.KEY_DATE + " DESC";
            selectQuery = String.format(selectQuery, Weight.KEY_ID, Weight.KEY_VALUE, Weight.KEY_DATE);
            Log.i("Seniors db - query", selectQuery);

            db = this.getWritableDatabase();
            cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    Weight entity = new Weight();
                    entity.setID(cursor.getInt(0));
                    entity.setValue(cursor.getFloat(1));
                    entity.setDate(new Date(cursor.getLong(2)));

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
    public int update(Weight entity) {
        int iRetVal = 0;

        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();

            ContentValues values = entity.getContentValues();
            // updating row
            iRetVal = db.update(getTableName(), values, Weight.KEY_ID + " = ?",
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
    public int remove(Weight entity) {
        int iRetVal = 0;

        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();

            iRetVal = db.delete(getTableName(), Weight.KEY_ID + " = ?",
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
