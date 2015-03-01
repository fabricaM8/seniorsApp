package br.com.fabricam8.seniorsapp.dal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.fabricam8.seniorsapp.domain.Exercise;
import br.com.fabricam8.seniorsapp.domain.Medication;
import br.com.fabricam8.seniorsapp.enums.ExerciseType;


/**
 * Created by laecy_000 on 22/02/2015.
 */
public class ExerciseDAL extends DbCRUD<Exercise> {

    public static final String TABLE_NAME = "Exercise";
    private static ExerciseDAL _instance;

    private ExerciseDAL(Context context) {
       super(context);
    }

    public static synchronized ExerciseDAL getInstance(Context context) {
        if (_instance == null) {
            _instance = new ExerciseDAL(context);
        }

        return _instance;
    }

    public String getTableName() {
        return TABLE_NAME;
    }


    @Override
    public int remove(Exercise entity) {
        int iRetVal = 0;

        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();

            iRetVal = db.delete(getTableName(), Medication.KEY_ID + " = ?",
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
    public Exercise findOne(long id) {
        Exercise oRetVal = null;

        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.getReadableDatabase();

            cursor = db.query(getTableName(), new String[]{
                    Exercise.KEY_ID,
                    Exercise.KEY_TYPE,
                    Exercise.KEY_START_DATE,
                    Exercise.KEY_END_DATE,
            },Exercise.KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);

            if (cursor != null)
                cursor.moveToFirst();

            oRetVal = new Exercise();
            oRetVal.setID(cursor.getInt(0));
            oRetVal.setType(cursor.isNull(1) ? ExerciseType.NONE : ExerciseType.fromInt(cursor.getInt(1)));

            if(!cursor.isNull(2))
                oRetVal.setStartDate(new Date(cursor.getLong(2)));

            if(!cursor.isNull(3))
                oRetVal.setEndDate(new Date(cursor.getLong(3)));

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
    public List<Exercise> findAll() {
        List<Exercise> lstRetVal = new ArrayList<>();

        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            // Select All Query
            String selectQuery = "SELECT %1$s, %2$s, %3$s, %4$s FROM "
                    + getTableName();
            selectQuery = String.format(selectQuery, Exercise.KEY_ID, Exercise.KEY_TYPE,
                    Exercise.KEY_START_DATE, Exercise.KEY_END_DATE);
            Log.i("Seniors db - query", selectQuery);

            db = this.getWritableDatabase();
            cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) do {
                Exercise entity = new Exercise();
                entity.setID(cursor.getInt(0));
                entity.setType(cursor.isNull(1) ? ExerciseType.NONE : ExerciseType.fromInt(cursor.getInt(1)));

                if(!cursor.isNull(2))
                    entity.setStartDate(new Date(cursor.getLong(2)));
                if(!cursor.isNull(3))
                    entity.setEndDate(new Date(cursor.getLong(3)));

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
    public int update(Exercise entity) {
        int iRetVal = 0;

        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();

            ContentValues values = entity.getContentValues();
            // updating row
            iRetVal = db.update(getTableName(), values, Exercise.KEY_ID + " = ?",
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
    public long create(Exercise entity) {
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
