package com.example.jassenmoran.simpletodo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by jassenmoran on 2/9/17.
 */

public class TodoDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "todo.db";
    private static final int DATABASE_VERSION = 11;

    public TodoDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_TODO_TABLE = "CREATE TABLE " +
                TodoContract.TodoEntry.TABLE_NAME + " (" +
                TodoContract.TodoEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TodoContract.TodoEntry.COLUMN_TODO_TITLE + " TEXT NOT NULL, " +
                TodoContract.TodoEntry.COLUMN_TODO_MONTH + " INTEGER, " +
                TodoContract.TodoEntry.COLUMN_TODO_DAY + " INTEGER, " +
                TodoContract.TodoEntry.COLUMN_TODO_YEAR + " INTEGER, " +
                TodoContract.TodoEntry.COLUMN_TODO_PRI + " TEXT NOT NULL" +
                ");";
        Log.i("Me", SQL_CREATE_TODO_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_TODO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TodoContract.TodoEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
