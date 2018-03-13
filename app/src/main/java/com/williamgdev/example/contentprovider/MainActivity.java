package com.williamgdev.example.contentprovider;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private String TAG = "MainActivity => ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*
        Uri usersUri = UsersDataHelper.URI_PHONES;
        ContentValues contentValues = new ContentValues();
        contentValues.put(UsersDataHelper.KEY_USER_ID, 2);
        contentValues.put(UsersDataHelper.KEY_PHONE, "12345");
        contentValues.put(UsersDataHelper.KEY_USER_ID, 1);
        contentValues.put(UsersDataHelper.KEY_PHONE, "54321");

        Uri resultUri = getContentResolver().insert(usersUri, contentValues);
        Log.d(TAG, "onCreate: " + resultUri);
        */

        Uri usersUri = UsersDataHelper.URI_USERS;
        Cursor cursor = getContentResolver().query(usersUri, null, null, null, null, null);
        cursor.moveToFirst();
        do{
            Log.d(TAG, "onCreate: " + cursor.getString(cursor.getColumnIndex(UsersDataHelper.KEY_USER_NAME)));
        }while(cursor.moveToNext());
        cursor.close();
        Log.d(TAG, "onCreate: " + getContentResolver().getType(usersUri));
        Log.d(TAG, "onCreate: " + usersUri.toString());
    }
}
