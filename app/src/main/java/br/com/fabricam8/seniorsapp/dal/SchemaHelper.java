package br.com.fabricam8.seniorsapp.dal;


import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import br.com.fabricam8.seniorsapp.domain.AlertEvent;
import br.com.fabricam8.seniorsapp.domain.AlertEventReportEntry;
import br.com.fabricam8.seniorsapp.domain.BloodPressure;
import br.com.fabricam8.seniorsapp.domain.Consultation;
import br.com.fabricam8.seniorsapp.domain.EmergencyContact;
import br.com.fabricam8.seniorsapp.domain.Exercise;
import br.com.fabricam8.seniorsapp.domain.Glucosis;
import br.com.fabricam8.seniorsapp.domain.Medication;
import br.com.fabricam8.seniorsapp.domain.Weight;

/**
 * Created by Aercio on 1/27/15.
 */
public class SchemaHelper {

    // Creating Tables
    public static void createTable(SQLiteDatabase db) {
        Log.i("SeniorsApp", "Creating table schemas");

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
                Exercise.KEY_ID + " INTEGER PRIMARY KEY," +
                Exercise.KEY_CLOUD_ID + " INTEGER," +
                Exercise.KEY_NAME + " TEXT," +
                Exercise.KEY_START_DATE + " INTEGER," +
                Exercise.KEY_END_DATE + " INTEGER," +
                Exercise.KEY_SUNDAY + " BIT, " +
                Exercise.KEY_MONDAY + " BIT, " +
                Exercise.KEY_TUESDAY + " BIT, " +
                Exercise.KEY_WEDNESDAY + " BIT, " +
                Exercise.KEY_THURSDAY + " BIT, " +
                Exercise.KEY_FRIDAY + " BIT, " +
                Exercise.KEY_SATURDAY + " BIT " +
                 ")";

        String alertReportTableSchema = "CREATE TABLE " + AlertEventReportEntryDAL.TABLE_NAME + "(" +
                AlertEventReportEntry.KEY_ID + " INTEGER PRIMARY KEY," +
                AlertEventReportEntry.KEY_ENTITY_ID + " INTEGER," +
                AlertEventReportEntry.KEY_ENTITY_CLASS + " TEXT," +
                AlertEventReportEntry.KEY_EVENT + " TEXT," +
                AlertEventReportEntry.KEY_EVENT_RESPONSE + " INTEGER," +
                AlertEventReportEntry.KEY_REPORT_DATE + " INTEGER" +
                ")";


        String consultTableSchema = "CREATE TABLE " + ConsultationDAL.TABLE_NAME + "(" +
                Consultation.KEY_ID + " INTEGER PRIMARY KEY," +
                Consultation.KEY_CLOUD_ID + " INTEGER," +
                Consultation.KEY_NAME + " TEXT," +
                Consultation.KEY_DETAILS + " TEXT," +
                Consultation.KEY_START_DATE + " TEXT," +
                Consultation.KEY_TYPE + " TEXT" +
                ")";


        String contactsTableSchema = "CREATE TABLE " + EmergencyContactDAL.TABLE_NAME + "(" +
                EmergencyContact.KEY_ID + " INTEGER PRIMARY KEY," +
                EmergencyContact.KEY_CLOUD_ID + " INTEGER," +
                EmergencyContact.KEY_NAME + " TEXT," +
                EmergencyContact.KEY_PHONE + " TEXT" +
                ")";

        String weightTableSchema = "CREATE TABLE " + WeightDAL.TABLE_NAME + "(" +
                Weight.KEY_ID + " INTEGER PRIMARY KEY," +
                Weight.KEY_CLOUD_ID + " INTEGER," +
                Weight.KEY_VALUE + " REAL," +
                Weight.KEY_DATE + " INTEGER" +
                ")";

        String glucosisTableSchema = "CREATE TABLE " + GlucosisDAL.TABLE_NAME + "(" +
                Glucosis.KEY_ID + " INTEGER PRIMARY KEY," +
                Glucosis.KEY_CLOUD_ID + " INTEGER," +
                Glucosis.KEY_RATE + " INTEGER," +
                Glucosis.KEY_DATE + " INTEGER" +
                ")";

        String bloodPressureTableSchema = "CREATE TABLE " + BloodPressureDAL.TABLE_NAME + "(" +
                BloodPressure.KEY_ID + " INTEGER PRIMARY KEY," +
                BloodPressure.KEY_CLOUD_ID + " INTEGER," +
                BloodPressure.KEY_SYSTOLIC + " INTEGER," +
                BloodPressure.KEY_DIASTOLIC + " INTEGER," +
                BloodPressure.KEY_DATE + " INTEGER" +
                ")";

        db.execSQL(medTableSchema);
        db.execSQL(alertTableSchema);
        db.execSQL(alertReportTableSchema);
        db.execSQL(excTableSchema);
        db.execSQL(consultTableSchema);
        db.execSQL(contactsTableSchema);
        db.execSQL(weightTableSchema);
        db.execSQL(glucosisTableSchema);
        db.execSQL(bloodPressureTableSchema);

    }

    // Upgrading database
    public static void upgradeSchema(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            Log.i("Senior' app", "Upgrading table schemas");

            // Drop older table if existed
            db.execSQL("DROP TABLE IF EXISTS " + MedicationDAL.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + AlertEventDAL.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + ExerciseDAL.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + AlertEventReportEntryDAL.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + ConsultationDAL.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + EmergencyContactDAL.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + WeightDAL.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + GlucosisDAL.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + BloodPressureDAL.TABLE_NAME);
            // Create tables again
            createTable(db);
        }
    }

}