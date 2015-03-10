package br.com.fabricam8.seniorsapp.dal;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.List;

import br.com.fabricam8.seniorsapp.domain.Consultation;

/**
 * Created by laecy_000 on 09/03/2015.
 */
public abstract class ConsultationDAL extends DbCRUD<Consultation>{

    public static final String TABLE_NAME = "Consultation";
    private static ConsultationDAL _instance;

    public static synchronized ConsultationDAL getInstance(Context context) {
        if (_instance == null) {
            _instance = new ConsultationDAL(context) {
                @Override
                public Consultation findOne(long id) {
                    return null;
                }

                @Override
                public List<Consultation> findAll() {
                    return null;
                }

                @Override
                public int update(Consultation entity) {
                    return 0;
                }
            };
        }

        return _instance;
    }
    private ConsultationDAL(Context context) {
        super(context);
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
