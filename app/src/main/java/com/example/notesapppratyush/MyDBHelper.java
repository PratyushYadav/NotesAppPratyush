package com.example.notesapppratyush;

import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

public class MyDBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "notes_db";
    private static final String TABLE_NAME = "notes";
    private static final String COLUMN_NOTE = "note";
    public MyDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_NOTE + " TEXT" + ")";
        db.execSQL(CREATE_TABLE);
        Log.d("sqlite","table created");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    public List<String> getAllNotes() {
        List<String> datanotesList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null) {
            try {
                int columnIndex = cursor.getColumnIndex(COLUMN_NOTE);
                if (columnIndex >= 0) {
                    while (cursor.moveToNext()) {
                        String note = cursor.getString(columnIndex);
                        datanotesList.add(note);
                    }
                } else {

                }
            } finally {
                 cursor.close();
            }
        }
        return datanotesList;
    }
    public void addNote(String note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOTE, note);
        db.insert(TABLE_NAME, null, values);
        Log.d("sqlite","tttttttttttttttttttable added");
        db.close();
    }
    public void updateNote(int position, String newNote) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOTE, newNote);
        String[] whereArgs = {String.valueOf(position)};
        db.update(TABLE_NAME, values, "ROWID = ?", whereArgs);
        db.close();
    }
    public void deleteNote(String note) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_NOTE + " = ?", new String[]{note});
        db.close();
    }
}
