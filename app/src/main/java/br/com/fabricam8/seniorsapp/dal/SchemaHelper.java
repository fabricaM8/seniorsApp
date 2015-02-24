package br.com.fabricam8.seniorsapp.dal;


import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import br.com.fabricam8.seniorsapp.domain.AlertEvent;
import br.com.fabricam8.seniorsapp.domain.Exercise;
import br.com.fabricam8.seniorsapp.domain.Medication;

/**
 * Created by Aercio on 1/27/15.
 */
public class SchemaHelper {

    // Creating Tables
    public static void createTable(SQLiteDatabase db) {
        Log.i("Senior' app", "Creating table schemas");

        String medTableSchema = "CREATE TABLE " + MedicationDAL.TABLE_NAME + "("
                + Medication.KEY_ID + " INTEGER PRIMARY KEY,"
                + Medication.KEY_CLOUD_ID + " INTEGER,"
                + Medication.KEY_NAME + " TEXT,"
                + Medication.KEY_DESCRIPTION + " TEXT,"
                + Medication.KEY_DOSAGE + " INTEGER,"
                + Medication.KEY_DOSAGE_TYPE + " INTEGER,"
                + Medication.KEY_PERIODICITY + " INTEGER,"
                + Medication.KEY_DURATION + " INTEGER,"
                + Medication.KEY_DURATION_TYPE + " INTEGER,"
                + Medication.KEY_START_DATE + " INTEGER,"
                + Medication.KEY_CONTINUOUS + " BIT,"
                + Medication.KEY_ALARM + " BIT"
                + ")";

        String alertTableSchema = "CREATE TABLE " + AlertEventDAL.TABLE_NAME + "("
                + AlertEvent.KEY_ID + " INTEGER PRIMARY KEY,"
                + AlertEvent.KEY_ENTITY_ID + " INTEGER,"
                + AlertEvent.KEY_ENTITY_CLASS + " TEXT,"
                + AlertEvent.KEY_EVENT + " TEXT,"
                + AlertEvent.KEY_MAX_ALARMS + " INTEGER,"
                + AlertEvent.KEY_ALARMS_PLAYED + " INTEGER,"
                + AlertEvent.KEY_NEXT_ALERT + " INTEGER"
                + ")";

        String excTableSchema = "CREATE TABLE " + ExerciseDAL.TABLE_NAME + "(" +
                Exercise.KEY_ID + " INTEGER PRIMARY KEY";


        db.execSQL(medTableSchema);
        db.execSQL(alertTableSchema);
        db.execSQL(excTableSchema);
    }

    // Upgrading database
    public static void upgradeSchema(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            Log.i("Senior' app", "Upgrading table schemas");

            // Drop older table if existed
            db.execSQL("DROP TABLE IF EXISTS " + MedicationDAL.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + AlertEventDAL.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + ExerciseDAL.TABLE_NAME);

            // Create tables again
            createTable(db);
        }
    }

}