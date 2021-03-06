package br.com.fabricam8.seniorsapp.dal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.fabricam8.seniorsapp.domain.Medication;
import br.com.fabricam8.seniorsapp.enums.DosageMeasure;
import br.com.fabricam8.seniorsapp.enums.Duration;
import br.com.fabricam8.seniorsapp.enums.Periodicity;

/**
 * getWritableDatabased by Aercio on 1/27/15.
 */
public class MedicationDAL extends DbCRUD<Medication> {

    public static final String TABLE_NAME = "Medication";

    private static MedicationDAL _instance;

    private MedicationDAL(Context context) {
        super(context);
    }

    public static synchronized MedicationDAL getInstance(Context context) {
        if (_instance == null)
        {
            _instance = new MedicationDAL(context);
        }

        return _instance;
    }

    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public long create(Medication entity)
    {
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
    public Medication findOne(long id) {
        return findByKeyAndValue(Medication.KEY_ID, String.valueOf(id));
    }

    @Override
    public List<Medication> findAll() {
        List<Medication> lstRetVal = new ArrayList<>();

        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            // Select All Query
            String selectQuery = "SELECT %1$s, %2$s, %3$s, %4$s, %5$s, %6$s, %7$s, %8$s, %9$s, %10$s, %11$s FROM %12$s ORDER BY %13$s ASC";
            selectQuery = String.format(selectQuery, Medication.KEY_ID, Medication.KEY_NAME,
                    Medication.KEY_DESCRIPTION, Medication.KEY_DOSAGE, Medication.KEY_DOSAGE_TYPE,
                    Medication.KEY_PERIODICITY, Medication.KEY_DURATION, Medication.KEY_DURATION_TYPE,
                    Medication.KEY_START_DATE, Medication.KEY_CONTINUOUS, Medication.KEY_ALARM,
                    getTableName(), Medication.KEY_NAME);
            Log.i("Seniors db - query", selectQuery);

            db = this.getWritableDatabase();
            cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    Medication entity = new Medication();
                    entity.setID(cursor.getInt(0));
                    entity.setName(cursor.getString(1));
                    entity.setDescription(cursor.getString(2));
                    entity.setDosage(cursor.getString(3));
                    entity.setDosageMeasureType(cursor.isNull(4) ? DosageMeasure.NONE : DosageMeasure.fromInt(cursor.getInt(4)));
                    entity.setPeriodicity(cursor.isNull(5) ? Periodicity.NONE : Periodicity.fromInt(cursor.getInt(5)));
                    entity.setDuration(cursor.isNull(6) ? -1 : cursor.getInt(6));
                    entity.setDurationType(cursor.isNull(7) ? Duration.NONE : Duration.fromInt(cursor.getInt(7)));

                    if (!cursor.isNull(8))
                        entity.setStartDate(new Date(cursor.getLong(8)));

                    entity.setContinuosUse(cursor.isNull(9) ? false : (cursor.getInt(9) == 1 ? true : false));
                    entity.setHasAlarm(cursor.isNull(10) ? false : (cursor.getInt(10) == 1 ? true : false));

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
    public int update(Medication entity) {
        int iRetVal = 0;

        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();

            ContentValues values = entity.getContentValues();
            // updating row
            iRetVal = db.update(getTableName(), values, Medication.KEY_ID + " = ?",
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
    public int remove(Medication entity) {
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

    /**
     * Encontra e retorna um medicamento pelo seu nome.
     * @param name O nome do medicamento.
     * @return O medicamento encontrado, nulo caso contrário.
     */
    public Medication findByName(String name) {
       return findByKeyAndValue(Medication.KEY_NAME, name);
    }

    /**
     * Encontra uma medicamento cadastrado pela key e valor.
     * @param key A chave de pesquisa: name, id, etc
     * @param value O valor da chave
     */
    private Medication findByKeyAndValue(String key, String value) {
        Medication oRetVal = null;

        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.getReadableDatabase();

            cursor = db.query(getTableName(), new String[]{
                    Medication.KEY_ID,
                    Medication.KEY_NAME,
                    Medication.KEY_DESCRIPTION,
                    Medication.KEY_DOSAGE,
                    Medication.KEY_DOSAGE_TYPE,
                    Medication.KEY_PERIODICITY,
                    Medication.KEY_DURATION,
                    Medication.KEY_DURATION_TYPE,
                    Medication.KEY_START_DATE,
                    Medication.KEY_CONTINUOUS,
                    Medication.KEY_ALARM
            }, key + "=?", new String[]{value}, null, null, null, null);

            if (cursor != null)
                cursor.moveToFirst();

            oRetVal = new Medication();
            oRetVal.setID(cursor.getInt(0));
            oRetVal.setName(cursor.getString(1));
            oRetVal.setDescription(cursor.getString(2));
            oRetVal.setDosage(cursor.getString(3));
            oRetVal.setDosageMeasureType(cursor.isNull(4) ? DosageMeasure.NONE : DosageMeasure.fromInt(cursor.getInt(4)));
            oRetVal.setPeriodicity(cursor.isNull(5) ? Periodicity.NONE : Periodicity.fromInt(cursor.getInt(5)));
            oRetVal.setDuration(cursor.isNull(6) ? -1 : cursor.getInt(6));
            oRetVal.setDurationType(cursor.isNull(7) ? Duration.NONE : Duration.fromInt(cursor.getInt(7)));
            if (!cursor.isNull(8))
                oRetVal.setStartDate(new Date(cursor.getLong(8)));

            oRetVal.setContinuosUse(cursor.isNull(9) ? false : (cursor.getInt(9) == 1 ? true : false));
            oRetVal.setHasAlarm(cursor.isNull(10) ? false : (cursor.getInt(10) == 1 ? true : false));

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
}
