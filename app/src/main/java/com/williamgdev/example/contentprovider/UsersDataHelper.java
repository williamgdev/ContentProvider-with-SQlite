package com.williamgdev.example.contentprovider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

/**
 * Created by User on 12/22/2016.
 */

public class UsersDataHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "usersDB";

    public static final String TABLE_USERS = "users";
    public static final String TABLE_PHONES = "users_phones";

    public static final String KEY_USER_ID = "id";
    public static final String KEY_USER_NAME = "userName";
    public static final String KEY_PHONE = "userPhones";

    public static final Uri URI_USERS = ClientsContentProvider.BASE_URI
            .buildUpon()
            .appendPath(UsersDataHelper.TABLE_USERS)
            .build();
    public static final Uri URI_PHONES = ClientsContentProvider.BASE_URI
            .buildUpon()
            .appendPath(UsersDataHelper.TABLE_PHONES)
            .build();

    public UsersDataHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS +
                "(" +
                KEY_USER_ID + " INTEGER PRIMARY KEY," +
                KEY_USER_NAME + " TEXT" +
                ")";

        db.execSQL(CREATE_USERS_TABLE);

        String CREATE_PHONES_TABLE = "CREATE TABLE " + TABLE_PHONES +
                "(" +
                KEY_USER_ID + " INTEGER PRIMARY KEY," +
                KEY_PHONE + " INTEGER" +
                ")";

        db.execSQL(CREATE_PHONES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PHONES);
        onCreate(db);
    }

}
