package com.example.android.contactlist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;

/**
 * Created by Android on 5/30/2017.
 */

public class SQLiteHelper extends SQLiteOpenHelper {

    private SQLiteDatabase myDb;

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FeedEntry.TABLE_NAME + " (" +
                    FeedEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    FeedEntry.COLUMN_NAME_FIRST + " TEXT," +
                    FeedEntry.COLUMN_NAME_LAST + " TEXT," +
                    FeedEntry.COLUMN_NAME_PHOTO_PATH + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME;

    public SQLiteHelper(Context context) {
        super(context, FeedEntry.DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }

    public void placeValuesInDb(Contact newContact) {
        myDb = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FeedEntry.COLUMN_NAME_FIRST, newContact.getFirstName());
        values.put(FeedEntry.COLUMN_NAME_LAST, newContact.getLastName());
        values.put(FeedEntry.COLUMN_NAME_PHOTO_PATH, newContact.getPhotoPath());

        myDb.insert(FeedEntry.TABLE_NAME, null, values);
    }

    public ArrayList<Contact> readValuesInDb() {
        ArrayList<Contact> returnList = new ArrayList<>();

        myDb = this.getWritableDatabase();

        String[] allColumns = {FeedEntry.COLUMN_NAME_FIRST, FeedEntry.COLUMN_NAME_LAST, FeedEntry.COLUMN_NAME_PHOTO_PATH};
        Cursor cursor = myDb.query(FeedEntry.TABLE_NAME, allColumns, null, null, null, null, null);

        while(cursor.moveToNext()) {
            String firstName = cursor.getString(cursor.getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_FIRST));
            String lastName = cursor.getString(cursor.getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_LAST));
            String path = cursor.getString(cursor.getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_PHOTO_PATH));
            returnList.add(new Contact(firstName, lastName, path));
        }
        cursor.close();

        return returnList;
    }

    public class FeedEntry implements BaseColumns {
        public static final String DB_NAME = "contact_list_db.db";
        public static final String TABLE_NAME = "CONTACT_TABLE";
        public static final String COLUMN_NAME_FIRST = "FIRST_NAME";
        public static final String COLUMN_NAME_LAST = "LAST_NAME";
        public static final String COLUMN_NAME_PHOTO_PATH = "PHOTO_PATH";
    }
}
