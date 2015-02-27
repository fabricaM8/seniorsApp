package br.com.fabricam8.seniorsapp.dal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.fabricam8.seniorsapp.domain.AlertEventReportEntry;
import br.com.fabricam8.seniorsapp.enums.ReportResponseType;

/**
 * Created by Aercio on 1/27/15.
 */
public class AlertEventReportEntryDAL extends DbCRUD<AlertEventReportEntry> {

    public static final String TABLE_NAME = "AlertEventReportEntry";

    private static AlertEventReportEntryDAL _instance;

    private AlertEventReportEntryDAL(Context context) {
        super(context);
    }

    public static synchronized AlertEventReportEntryDAL getInstance(Context context) {
        if (_instance == null) {
            _instance = new AlertEventReportEntryDAL(context);
        }

        return _instance;
    }

    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public long create(AlertEventReportEntry entity) {
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
    public AlertEventReportEntry findOne(long id) {
        return findOne(AlertEventReportEntry.KEY_ID + "=?", new String[]{String.valueOf(id)});
    }

    private AlertEventReportEntry findOne(String selection, String[] selectionArgs) {
        AlertEventReportEntry oRetVal = null;

        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.getReadableDatabase();

            cursor = db.query(getTableName(), new String[]{
                    AlertEventReportEntry.KEY_ID,
                    AlertEventReportEntry.KEY_ENTITY_ID,
                    AlertEventReportEntry.KEY_ENTITY_CLASS,
                    AlertEventReportEntry.KEY_EVENT,
                    AlertEventReportEntry.KEY_EVENT_RESPONSE,
                    AlertEventReportEntry.KEY_REPORT_DATE
            }, selection, selectionArgs, null, null, null, null);

            if (cursor != null)
                cursor.moveToFirst();

            oRetVal = new AlertEventReportEntry();
            oRetVal.setID(cursor.getInt(0));
            oRetVal.setEntityId(cursor.getInt(1));
            oRetVal.setEntityClass(cursor.getString(2));
            oRetVal.setEvent(cursor.getString(3));
            oRetVal.setResponse(ReportResponseType.fromInt(cursor.getInt(4)));
            if (!cursor.isNull(5))
                oRetVal.setReportDate(new Date(cursor.getLong(5)));

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
    public List<AlertEventReportEntry> findAll() {
        List<AlertEventReportEntry> lstRetVal = new ArrayList<>();

        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            // Select All Query
            String selectQuery = "SELECT %1$s, %2$s, %3$s, %4$s, %5$s, %6$s, %7$s FROM "
                    + getTableName();
            selectQuery = String.format(selectQuery, AlertEventReportEntry.KEY_ID, AlertEventReportEntry.KEY_ENTITY_ID,
                    AlertEventReportEntry.KEY_ENTITY_CLASS, AlertEventReportEntry.KEY_EVENT, AlertEventReportEntry.KEY_EVENT_RESPONSE,
                    AlertEventReportEntry.KEY_REPORT_DATE);
            Log.i("Seniors db - query", selectQuery);

            db = this.getWritableDatabase();
            cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    AlertEventReportEntry entity = new AlertEventReportEntry();
                    entity.setID(cursor.getInt(0));
                    entity.setEntityId(cursor.getInt(1));
                    entity.setEntityClass(cursor.getString(2));
                    entity.setEvent(cursor.getString(3));
                    entity.setResponse(ReportResponseType.fromInt(cursor.getInt(4)));
                    if (!cursor.isNull(5))
                        entity.setReportDate(new Date(cursor.getLong(5)));

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
    public int update(AlertEventReportEntry entity) {
        int iRetVal = 0;

        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();

            ContentValues values = entity.getContentValues();
            // updating row
            iRetVal = db.update(getTableName(), values, AlertEventReportEntry.KEY_ID + " = ?",
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
    public int remove(AlertEventReportEntry entity) {
        int iRetVal = 0;

        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();

            iRetVal = db.delete(getTableName(), AlertEventReportEntry.KEY_ID + " = ?",
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
