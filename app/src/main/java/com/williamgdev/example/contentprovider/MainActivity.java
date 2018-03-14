package com.williamgdev.example.contentprovider;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.util.Dictionary;

public class MainActivity extends AppCompatActivity {

    private String TAG = "MainActivity => ";
    private TextView txtView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtView = findViewById(R.id.txtView);
//        populate();

        String action = getIntent().getAction();
        Uri data = getIntent().getData();
        if (data!= null && data.toString().equals("example://contentprovider/users")) {
            printUsersName();
        }

    }

    private void printUsersName() {
        txtView.setText(null);
        Uri usersUri = UsersDataHelper.URI_USERS;
        Cursor cursor = getContentResolver().query(usersUri, null, null, null, null, null);
        cursor.moveToFirst();
        if (cursor.getCount() == 0) {
            Log.d(TAG, "onCreate: Cursor Empty");
            return;
        } else {
            Log.d(TAG, "onCreate: Count: " + cursor.getCount());
        }
        do {
            String username = cursor.getString(cursor.getColumnIndex(UsersDataHelper.KEY_USER_NAME));
            txtView.setText(txtView.getText() + "\n" + username);
            Log.d(TAG, "onCreate: " + username);
        } while (cursor.moveToNext());
        cursor.close();
        Log.d(TAG, "onCreate: " + getContentResolver().getType(usersUri));
        Log.d(TAG, "onCreate: " + usersUri.toString());
    }

    private void populate() {
//        Uri usersUri = UsersDataHelper.URI_USERS;
//        ContentValues valuesNamesUser = new ContentValues();
//        valuesNamesUser.put(UsersDataHelper.KEY_USER_ID, 1);
//        valuesNamesUser.put(UsersDataHelper.KEY_USER_NAME, "Willy");

        Uri phoneUri = UsersDataHelper.URI_PHONES;
        ContentValues contentValuesPhone = new ContentValues();
        contentValuesPhone.put(UsersDataHelper.KEY_USER_ID, 2);
        contentValuesPhone.put(UsersDataHelper.KEY_PHONE, "+15432123131");

//        Uri resultUri = getContentResolver().insert(usersUri, valuesNamesUser);
//        Log.d(TAG, "onCreate: " + resultUri);
        Uri resultUri = getContentResolver().insert(phoneUri, contentValuesPhone);
        Log.d(TAG, "onCreate: " + resultUri);
    }
}
