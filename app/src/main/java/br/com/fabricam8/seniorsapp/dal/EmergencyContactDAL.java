package br.com.fabricam8.seniorsapp.dal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.com.fabricam8.seniorsapp.domain.EmergencyContact;


/**
 * Created by laecyo on 22/02/2015.
 */
public class EmergencyContactDAL extends DbCRUD<EmergencyContact> {

    public static final String TABLE_NAME = "contacts";
    private static EmergencyContactDAL _instance;

    private EmergencyContactDAL(Context context) {
        super(context);
    }

    public static synchronized EmergencyContactDAL getInstance(Context context) {
        if (_instance == null) {
            _instance = new EmergencyContactDAL(context);
        }

        return _instance;
    }

    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public int remove(EmergencyContact entity) {
        int iRetVal = 0;

        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();

            iRetVal = db.delete(getTableName(), EmergencyContact.KEY_ID + " = ?",
                    new String[]{String.valueOf(entity.getID())});
        } catch (Exception ex) {
            Log.e("Seniors DB - delete", ex.getMessage());
        } finally {
            if (db != null && db.isOpen())
                db.close();
        }

        return iRetVal;
    }

    @Override
    public EmergencyContact findOne(long id) {
        EmergencyContact oRetVal = null;

        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {

            db = this.getReadableDatabase();

            cursor = db.query(getTableName(), new String[]{
                    EmergencyContact.KEY_ID, EmergencyContact.KEY_NAME, EmergencyContact.KEY_PHONE
            }, EmergencyContact.KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);

            if (cursor != null)
                cursor.moveToFirst();

            oRetVal = new EmergencyContact();
            oRetVal.setID(cursor.getInt(0));
            oRetVal.setName(cursor.getString(1));
            oRetVal.setPhone(cursor.getString(2));

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
    public List<EmergencyContact> findAll() {
        List<EmergencyContact> lstRetVal = new ArrayList<>();

        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            // Select All Query
            String selectQuery = "SELECT %1$s, %2$s, %3$s FROM "
                    + getTableName();
            selectQuery = String.format(selectQuery, EmergencyContact.KEY_ID, EmergencyContact.KEY_NAME,
                    EmergencyContact.KEY_PHONE);
            Log.i("Seniors db - query", selectQuery);

            db = this.getWritableDatabase();
            cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst())
                do {
                    EmergencyContact entity = new EmergencyContact();
                    entity.setID(cursor.getInt(0));
                    entity.setName(cursor.getString(1));
                    entity.setPhone(cursor.getString(2));

                    // Adding entity to list
                    lstRetVal.add(entity);
                } while (cursor.moveToNext());
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
    public int update(EmergencyContact entity) {
        int iRetVal = 0;

        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();

            ContentValues values = entity.getContentValues();
            // updating row
            iRetVal = db.update(getTableName(), values, EmergencyContact.KEY_ID + " = ?",
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
    public long create(EmergencyContact entity) {
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
}
