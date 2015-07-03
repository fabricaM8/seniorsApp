package br.com.fabricam8.seniorsapp.dal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.fabricam8.seniorsapp.domain.Glucosis;

/**
 * Created by Aercio on 1/27/15.
 */
public class GlucosisDAL extends DbCRUD<Glucosis> {

    public static final String TABLE_NAME = "Glucosis";

    private static GlucosisDAL _instance;

    private GlucosisDAL(Context context) {
        super(context);
    }

    public static synchronized GlucosisDAL getInstance(Context context) {
        if (_instance == null) {
            _instance = new GlucosisDAL(context);
        }

        return _instance;
    }

    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public long create(Glucosis entity) {
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
    public Glucosis findOne(long id) {
        return findOne(Glucosis.KEY_ID + "=?", new String[]{String.valueOf(id)});
    }

    private Glucosis findOne(String selection, String[] selectionArgs) {
        Glucosis oRetVal = null;

        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.getReadableDatabase();

            cursor = db.query(getTableName(), new String[]{
                    Glucosis.KEY_ID,
                    Glucosis.KEY_RATE,
                    Glucosis.KEY_DATE
            }, selection, selectionArgs, null, null, null, null);

            if (cursor != null)
                cursor.moveToFirst();

            oRetVal = new Glucosis();
            oRetVal.setID(cursor.getInt(0));
            oRetVal.setRate(cursor.getInt(1));
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
    public List<Glucosis> findAll() {
        List<Glucosis> lstRetVal = new ArrayList<>();

        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            // Select All Query
            String selectQuery = "SELECT %1$s, %2$s, %3$s FROM "
                    + getTableName();
            selectQuery = String.format(selectQuery, Glucosis.KEY_ID, Glucosis.KEY_RATE, Glucosis.KEY_DATE);
            Log.i("Seniors db - query", selectQuery);

            db = this.getWritableDatabase();
            cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    Glucosis entity = new Glucosis();
                    entity.setID(cursor.getInt(0));
                    entity.setRate(cursor.getInt(1));
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
    public int update(Glucosis entity) {
        int iRetVal = 0;

        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();

            ContentValues values = entity.getContentValues();
            // updating row
            iRetVal = db.update(getTableName(), values, Glucosis.KEY_ID + " = ?",
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
    public int remove(Glucosis entity) {
        int iRetVal = 0;

        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();

            iRetVal = db.delete(getTableName(), Glucosis.KEY_ID + " = ?",
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
