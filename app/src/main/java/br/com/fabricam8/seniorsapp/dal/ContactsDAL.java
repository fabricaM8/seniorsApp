package br.com.fabricam8.seniorsapp.dal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.com.fabricam8.seniorsapp.domain.Contacts;
import br.com.fabricam8.seniorsapp.domain.Exercise;


/**
 * Created by laecy_000 on 22/02/2015.
 */
public class ContactsDAL extends DbCRUD<Contacts> {

    public static final String TABLE_NAME = "contacts";
    private static ContactsDAL _instance;

    private ContactsDAL(Context context) {
       super(context);
    }

    public static synchronized ContactsDAL getInstance(Context context) {
        if (_instance == null)
        {
            _instance = new ContactsDAL(context);
        }

        return _instance;
    }

    public String getTableName() {
        return TABLE_NAME;
    }


    @Override
    public int remove(Contacts entity) {
        int iRetVal = 0;

        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();

            iRetVal = db.delete(getTableName(), Contacts.KEY_ID + " = ?",
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
    public Contacts findOne(long id) {
        Contacts oRetVal = null;

        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.getReadableDatabase();

            cursor = db.query(getTableName(), new String[]{
                    Contacts.KEY_ID, Contacts.KEY_NAME, Contacts.KEY_FONE1,
                    Contacts.KEY_FONE2
            },Exercise.KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);

            if (cursor != null)
                cursor.moveToFirst();

            oRetVal = new Contacts();
            oRetVal.setID(cursor.getInt(0));
            oRetVal.setCloudId(cursor.getInt(1));
            oRetVal.setName(cursor.getString(2));
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
    public List<Contacts> findAll() {
        List<Contacts> lstRetVal = new ArrayList<>();

        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            // Select All Query
            String selectQuery = "SELECT %1$s, %2$s, %3$s, %4$s FROM "
                    + getTableName();
            selectQuery = String.format(selectQuery, Contacts.KEY_ID, Contacts.KEY_NAME, Contacts.KEY_FONE1,
                    Contacts.KEY_FONE2);
            Log.i("Seniors db - query", selectQuery);

            db = this.getWritableDatabase();
            cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst())
                do
              {
                  Contacts entity = new Contacts();
                  entity.setID(cursor.getInt(0));
                  entity.setCloudId(cursor.getInt(1));
                  entity.setName(cursor.getString(2));
                  entity.setFone1(cursor.getString(3));
                  entity.setFone2(cursor.getString(4));

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
    public int update(Contacts entity) {
        int iRetVal = 0;

        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();

            ContentValues values = entity.getContentValues();
            // updating row
            iRetVal = db.update(getTableName(), values, Contacts.KEY_ID + " = ?",
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
    public long create(Contacts entity) {
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
