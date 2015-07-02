package com.longpaws.spendless;

import static com.longpaws.spendless.ListViewConstantsDB.FIRST_COLUMN;
import static com.longpaws.spendless.ListViewConstantsDB.SECOND_COLUMN;
import static com.longpaws.spendless.ListViewConstantsDB.THIRD_COLUMN;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

/*
 * Created by Mitch on 6/25/2015.
 */
public class DataBaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "DollarsSpentDB.db";
    public static final String TABLE_NAME = "dollars_spent_transaction";
    public static final String COL_ID = "ID";
    public static final String COL_MONTH = "MONTH";
    public static final String COL_YEAR = "YEAR";
    public static final String COL_EXPENSE_NAME = "EXPENSE_NAME";
    public static final String COL_DOLLARS_SPENT = "DOLLARS_SPENT";

    public DataBaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    // Creates SQLite database with 5 columns defined in the public static final Strings above
    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_DOLLARS_SPENT_TRANSACTION_TABLE = "CREATE TABLE " +
                TABLE_NAME + "(" +
                COL_ID + " INTEGER PRIMARY KEY," +
                COL_MONTH + " TEXT," +
                COL_YEAR + " INTEGER," +
                COL_EXPENSE_NAME + " TEXT," +
                COL_DOLLARS_SPENT + " REAL" + ")";
        db.execSQL(CREATE_DOLLARS_SPENT_TRANSACTION_TABLE);
    }

    // Creates new version of database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Takes an instance of DollarsSpentTransaction and inserts corresponding data
    // into corresponding columns in a new row in the QLite database
    public void addTransaction(DollarsSpentTransaction transaction) {
        ContentValues values = new ContentValues();
        values.put(COL_MONTH, transaction.getMonth() );
        values.put(COL_YEAR, transaction.getYear() );
        values.put(COL_EXPENSE_NAME, transaction.getExpenseName() );
        values.put(COL_DOLLARS_SPENT, transaction.getDollarsSpent() );

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    // Displays entire database
    // Returns ArrayList of HashMap that contain key value pair,
    // Key: name of column in the TextView, Value: data from SQLite database
    // Uses Cursor to put every row in the DB into the ArrayList
    public ArrayList<HashMap<String, String>> displayDataBase() {
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        cursor.moveToFirst();

        while (!cursor.isAfterLast() ) {
            HashMap<String, String> temp = new HashMap<>();

            String MONTH_YEAR_STRING = cursor.getString(1) + ", " + cursor.getString(2);
            String EXPENSE_NAME_STRING = cursor.getString(3);
            String DOLLARS_SPENT_STRING = "$" + cursor.getString(4);

            temp.put(FIRST_COLUMN, MONTH_YEAR_STRING);
            temp.put(SECOND_COLUMN, EXPENSE_NAME_STRING);
            temp.put(THIRD_COLUMN, DOLLARS_SPENT_STRING);

            list.add(temp);
            cursor.moveToNext();
        }
        return list;
    }

}