package br.com.fabricam8.seniorsapp.dal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.fabricam8.seniorsapp.domain.Consultation;
import br.com.fabricam8.seniorsapp.enums.ReminderType;

/**
 * Created by laecy_000 on 09/03/2015.
 */
public class ConsultationDAL extends DbCRUD<Consultation>{

    public static final String TABLE_NAME = "Consultation";
    private static ConsultationDAL _instance;

    private ConsultationDAL(Context context) {
        super(context);
   }

    public static synchronized ConsultationDAL getInstance(Context context) {
        if (_instance == null)
        {
            _instance = new ConsultationDAL(context);
        }

        return _instance;
    }


    public String getTableName() {
        return TABLE_NAME;
    }
    @Override
    public int remove(Consultation entity) {
        int iRetVal = 0;

        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();

            iRetVal = db.delete(getTableName(),Consultation.KEY_ID + " = ?",
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
    public Consultation findOne(long id) {
        Consultation oRetVal = null;

        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.getReadableDatabase();

            cursor = db.query(getTableName(), new String[]{
                    Consultation.KEY_ID,
                    Consultation.KEY_CLOUD_ID,
                    Consultation.KEY_NAME,
                    Consultation.KEY_DETAILS,
                    Consultation.KEY_TYPE,
                    Consultation.KEY_START_DATE,
            }, Consultation.KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);

            if (cursor != null)
                cursor.moveToFirst();

            oRetVal = new Consultation();
            oRetVal.setID(cursor.getInt(0));
            oRetVal.setCloudId(cursor.getInt(1));
            oRetVal.setName(cursor.getString(2));
            oRetVal.setDetails(cursor.getString(3));
            oRetVal.setReminderType(cursor.isNull(4) ? ReminderType.NONE : ReminderType.fromInt(cursor.getInt(4)));

            if(!cursor.isNull(5))
                oRetVal.setStartDate(new Date(cursor.getLong(5)));

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
    public List<Consultation> findAll() {
        List<Consultation> lstRetVal = new ArrayList<>();

        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            // Select All Query
            String selectQuery = "SELECT %1$s, %2$s, %3$s, %4$s, %5$s, %6$s  FROM "
                    + getTableName();
            selectQuery = String.format(selectQuery,
                    Consultation.KEY_ID,
                    Consultation.KEY_CLOUD_ID,
                    Consultation.KEY_NAME,
                    Consultation.KEY_DETAILS,
                    Consultation.KEY_TYPE,
                    Consultation.KEY_START_DATE);
            Log.i("Seniors db - query", selectQuery);

            db = this.getWritableDatabase();
            cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) do {
                Consultation entity = new Consultation();
                entity.setID(cursor.getInt(0));
                entity.setCloudId(cursor.getInt(1));
                entity.setName(cursor.getString(2));
                entity.setDetails(cursor.getString(3));
                entity.setReminderType(cursor.isNull(4) ? ReminderType.NONE : ReminderType.fromInt(cursor.getInt(4)));

                if(!cursor.isNull(5))
                    entity.setStartDate(new Date(cursor.getLong(5)));

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
    public int update(Consultation entity) {
        int iRetVal = 0;

        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();

            ContentValues values = entity.getContentValues();
            // updating row
            iRetVal = db.update(getTableName(), values, Consultation.KEY_ID + " = ?",
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
    public long create(Consultation entity) {
        long iRetVal = -1;

        SQLiteDatabase db = null;
        try {
            // opening db
            db = this.getWritableDatabase();
            // getting content
            ContentValues values = entity.getContentValues();
            // Inserting Row
            iRetVal = db.insert(getTableName(), null, values);
        } catch (Exception ex)
        {
            Log.e("Seniors DB", ex.getMessage());
            iRetVal = -1;
        } finally {
            if (db != null && db.isOpen())
                db.close();
        }

        return iRetVal;
    }


}
