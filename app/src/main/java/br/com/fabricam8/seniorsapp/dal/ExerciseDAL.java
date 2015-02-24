package br.com.fabricam8.seniorsapp.dal;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import br.com.fabricam8.seniorsapp.domain.Exercise;


/**
 * Created by laecy_000 on 22/02/2015.
 */
public class ExerciseDAL {

    public static final String TABLE_NAME = "Exercise";
    private static ExerciseDAL _instance;

    private ExerciseDAL(Context context) {
       // super(context);
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

    //@Override
    public long create(Exercise entity) {
        long iRetVal = -1;
/*
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
*/
        return iRetVal;

    }



}
