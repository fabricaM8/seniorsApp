package br.com.fabricam8.seniorsapp.dal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.fabricam8.seniorsapp.domain.AlertEvent;

/**
 * Created by Aercio on 1/27/15.
 */
public class AlertEventDAL extends DbCRUD<AlertEvent> {

    public static final String TABLE_NAME = "AlertEvent";

    private static AlertEventDAL _instance;

    public static synchronized AlertEventDAL getInstance(Context context) {
        if (_instance == null) {
            _instance = new AlertEventDAL(context);
        }

        return _instance;
    }

    private AlertEventDAL(Context context) {
        super(context);
    }

    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public long create(AlertEvent entity) {
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
    public AlertEvent findOne(long id) {
        AlertEvent oRetVal = null;

        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.getReadableDatabase();

            cursor = db.query(getTableName(), new String[]{
                    AlertEvent.KEY_ID,
                    AlertEvent.KEY_ENTITY_ID,
                    AlertEvent.KEY_ENTITY_CLASS,
                    AlertEvent.KEY_EVENT,
                    AlertEvent.KEY_MAX_ALARMS,
                    AlertEvent.KEY_ALARMS_PLAYED,
                    AlertEvent.KEY_NEXT_ALERT
            }, AlertEvent.KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);

            if (cursor != null)
                cursor.moveToFirst();

            oRetVal = new AlertEvent();
            oRetVal.setID(cursor.getInt(0));
            oRetVal.setEntityId(cursor.getInt(1));
            oRetVal.setEntityClass(cursor.getString(2));
            oRetVal.setEvent(cursor.getString(3));
            oRetVal.setMaxAlarms(cursor.getInt(4));
            oRetVal.setAlarmsPlayed(cursor.getInt(5));
            if (!cursor.isNull(6))
                oRetVal.setNextAlert(new Date(cursor.getLong(6)));

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
    public List<AlertEvent> findAll() {
        List<AlertEvent> lstRetVal = new ArrayList<>();

        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            // Select All Query
            String selectQuery = "SELECT %1$s, %2$s, %3$s, %4$s, %5$s, %6$s, %7$s FROM "
                    + getTableName();
            selectQuery = String.format(selectQuery, AlertEvent.KEY_ID, AlertEvent.KEY_ENTITY_ID,
                    AlertEvent.KEY_ENTITY_CLASS, AlertEvent.KEY_EVENT, AlertEvent.KEY_MAX_ALARMS,
                    AlertEvent.KEY_ALARMS_PLAYED, AlertEvent.KEY_NEXT_ALERT);
            Log.i("Seniors db - query", selectQuery);

            db = this.getWritableDatabase();
            cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    AlertEvent entity = new AlertEvent();
                    entity.setID(cursor.getInt(0));
                    entity.setEntityId(cursor.getInt(1));
                    entity.setEntityClass(cursor.getString(2));
                    entity.setEvent(cursor.getString(3));
                    entity.setMaxAlarms(cursor.getInt(4));
                    entity.setAlarmsPlayed(cursor.getInt(5));
                    if (!cursor.isNull(6))
                        entity.setNextAlert(new Date(cursor.getLong(6)));

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
    public int update(AlertEvent entity) {
        int iRetVal = 0;

        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();

            ContentValues values = entity.getContentValues();
            // updating row
            iRetVal = db.update(getTableName(), values, AlertEvent.KEY_ID + " = ?",
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
    public int remove(AlertEvent entity) {
        int iRetVal = 0;

        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();

            iRetVal = db.delete(getTableName(), AlertEvent.KEY_ID + " = ?",
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
