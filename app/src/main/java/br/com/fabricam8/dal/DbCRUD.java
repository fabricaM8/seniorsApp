package br.com.fabricam8.dal;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;

import br.com.fabricam8.domain.DbEntity;

/**
 * Created by Aercio on 1/27/15.
 */
public abstract class DbCRUD<T extends DbEntity> extends SQLiteOpenHelper{

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "seniors_db";


    public DbCRUD(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public abstract String getTableName();

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new entity
    public abstract void create(T entity);

    // Getting single entity
    public abstract T findOne(int id);

    // Getting All entities
    public abstract  List<T> findAll();

    // Updating single entity
    public abstract int update(T entity);

    // Deleting single entity
    public  abstract void remove(T entity);

    // Getting entities Count
    public int count() {
        String countQuery = "SELECT  * FROM " + getTableName();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }
}
//
//
//
//
//
//
//package br.com.fabricam8.dal;
//
//        import java.util.ArrayList;
//        import java.util.List;
//
//        import android.content.ContentValues;
//        import android.content.Context;
//        import android.database.Cursor;
//        import android.database.sqlite.SQLiteDatabase;
//        import android.database.sqlite.SQLiteOpenHelper;
//
//        import br.com.fabricam8.domain.DbEntity;
//
///**
// * Created by Aercio on 1/27/15.
// */
//public abstract class DbCRUD<T extends DbEntity> extends SQLiteOpenHelper{
//
//    // All Static variables
//    // Database Version
//    private static final int DATABASE_VERSION = 1;
//
//    // Database Name
//    private static final String DATABASE_NAME = "seniors_db";
//
//    // Entity Known Columns names
//    private static final String KEY_ID = "id";
//
//    public DbCRUD(Context context) {
//
//        super(context, DATABASE_NAME, null, DATABASE_VERSION);
//    }
//
//    /**
//     * Returns the table name for this structure
//     * @return
//     */
//    public abstract String getTableName();
//
//    /**
//     * All CRUD(Create, Read, Update, Delete) Operations
//     */
//
//    // Adding new entity
//    T create(T entity) {
//
//    }
//
//    // Getting single entity
//    T findOne(int id) {
//
//    }
//
//    // Getting All Entities
//    public List<T> findAll() {
//
//    }
//
//    // Updating single entity
//    public int update(T entity) {
//
//    }
//
//    // Deleting single entity
//    public void remove(DbEntity entity) {
//
//    }
//
//
//    // Getting contacts Count
//    public int count() {
//        String countQuery = "SELECT  * FROM " + getTableName();
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(countQuery, null);
//        cursor.close();
//
//        // return count
//        return cursor.getCount();
//    }
//
//}
