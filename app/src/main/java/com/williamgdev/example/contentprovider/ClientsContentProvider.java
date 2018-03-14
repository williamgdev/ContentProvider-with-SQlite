package com.williamgdev.example.contentprovider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class ClientsContentProvider extends ContentProvider {
    public static final String AUTHORITY = "com.williamgdev.example.contentprovider.contacts";
    public static final Uri BASE_URI = Uri.parse("content://" + AUTHORITY);

    private UsersDataHelper userDataHelper;

    private static final UriMatcher uriMatcher = buildUriMatcher();
    private final static int
            MATCH_USERS = 100,
            MATCH_PHONES = 200;
    private String TAG = "ContentProvider => ";

    public ClientsContentProvider() {
    }

    private static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, UsersDataHelper.TABLE_USERS, MATCH_USERS);
        uriMatcher.addURI(AUTHORITY, UsersDataHelper.TABLE_PHONES, MATCH_PHONES);
        return uriMatcher;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = userDataHelper.getWritableDatabase();
        int result;
        final int match = uriMatcher.match(uri);
        switch (match){
            case MATCH_USERS:
                result = db.delete(UsersDataHelper.TABLE_USERS, selection, selectionArgs);
                break;
            case MATCH_PHONES:
                result = db.delete(UsersDataHelper.TABLE_PHONES, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        return result;
    }

    @Override
    public String getType(Uri uri) {
        final int match = uriMatcher.match(uri);

        switch (match){
            case MATCH_USERS:
                return ContentResolver.CURSOR_DIR_BASE_TYPE +
                        "/" + AUTHORITY +
                        "/" + UsersDataHelper.TABLE_USERS;
            case MATCH_PHONES:
                return ContentResolver.CURSOR_DIR_BASE_TYPE +
                        "/" + AUTHORITY +
                        "/" + UsersDataHelper.TABLE_PHONES;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = userDataHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        long resultId;
        Uri resultUri;

        switch (match){
            case MATCH_USERS:
                resultId = db.insert(UsersDataHelper.TABLE_USERS, null, values);
                if (resultId > 0){
                    resultUri = ContentUris.withAppendedId(UsersDataHelper.URI_PHONES, resultId);
                }
                else
                    throw new SQLException("Error inserting row into " + uri);
                break;
            case MATCH_PHONES:
                resultId = db.insert(UsersDataHelper.TABLE_PHONES, null, values);
                if (resultId > 0){
                    resultUri = ContentUris.withAppendedId(UsersDataHelper.URI_PHONES, resultId);
                }
                else
                    throw new SQLException("Error inserting row into " + uri);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return resultUri;
    }

    @Override
    public boolean onCreate() {
        userDataHelper = new UsersDataHelper(getContext());
        return userDataHelper != null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        final int match = uriMatcher.match(uri);
        String tableName;
        switch (match){
            case MATCH_PHONES:
                tableName = userDataHelper.TABLE_PHONES;
                break;
            case MATCH_USERS:
                tableName = userDataHelper.TABLE_USERS;
                break;
            default:
                throw new UnsupportedOperationException("Uknown uri: " + uri);
        }
        return userDataHelper.getReadableDatabase().query(
                tableName, projection, selection, selectionArgs, null, null, sortOrder
        );
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void populateDatabase() {
        SQLiteDatabase db = userDataHelper.getWritableDatabase();
        db.beginTransaction();
        ContentValues valuesNames = new ContentValues();
        valuesNames.put(userDataHelper.KEY_USER_ID, 1);
        valuesNames.put(userDataHelper.KEY_USER_NAME, "Willy");

        ContentValues valuesPhones = new ContentValues();
        valuesPhones.put(userDataHelper.KEY_USER_ID, 1);
        valuesPhones.put(userDataHelper.KEY_PHONE, 12345);

        try {
            db.insertOrThrow(userDataHelper.TABLE_USERS, null, valuesNames);
            db.insertOrThrow(userDataHelper.TABLE_PHONES, null, valuesPhones);
        } catch (SQLException e) {
            Log.d(TAG, "populateDatabase: Error while trying insert values into the database");
        }finally {
            db.endTransaction();
        }

    }

}
