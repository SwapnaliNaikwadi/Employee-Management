package com.example.a.userinfo;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.HashMap;

import static android.os.Build.ID;
import static com.example.a.userinfo.DatabaseHelper.PHOTO;
import static com.example.a.userinfo.DatabaseHelper.STUD_ADDRESS;
import static com.example.a.userinfo.DatabaseHelper.STUD_HOBBIES;
import static com.example.a.userinfo.DatabaseHelper.STUD_ID;
import static com.example.a.userinfo.DatabaseHelper.STUD_NAME;
import static com.example.a.userinfo.DatabaseHelper.STUD_PHONE;
import static com.example.a.userinfo.DatabaseHelper.TABLE_STUDENT;



public class MyContentProvider extends ContentProvider {

    public static final String CONTENT_TYPE_USERS_ALL = "vnd.android.cursor.dir/vnd.coderz.users";

    public static final String CONTENT_TYPE_USERS_ONE = "vnd.android.cursor.item/vnd.coderz.users";

    public static final String AUTHORITY = "com.felixit.employeemanagement.User";

    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
            + "/users");

    DatabaseHelper dbHelper;

    private  static final int USERS_ALL=1;
    private  static final int USERS_ONE=2;

    private static final UriMatcher sUriMatcher;

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(AUTHORITY, "users", USERS_ALL);
        sUriMatcher.addURI(AUTHORITY, "users/#", USERS_ONE);
    }

    // Map table columns
    private static final HashMap<String, String> sUsersColumnProjectionMap;
    static {
        sUsersColumnProjectionMap = new HashMap<String, String>();
        sUsersColumnProjectionMap.put(STUD_ID,STUD_ID);
        sUsersColumnProjectionMap.put(STUD_NAME,
                STUD_NAME);
        sUsersColumnProjectionMap.put(STUD_PHONE,
                STUD_PHONE);
        sUsersColumnProjectionMap.put(STUD_ADDRESS,STUD_ADDRESS);
        sUsersColumnProjectionMap.put(STUD_HOBBIES,
                STUD_HOBBIES);
        sUsersColumnProjectionMap.put(PHOTO,
                PHOTO);
    }


    @Override
    public boolean onCreate() {
        dbHelper = new DatabaseHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        SQLiteQueryBuilder builder=new SQLiteQueryBuilder();
        Cursor cursor=null;
        switch (sUriMatcher.match(uri))
        {
            case USERS_ALL:
                builder.setTables(TABLE_STUDENT);
                builder.setProjectionMap(sUsersColumnProjectionMap);

                break;
            case USERS_ONE:
                builder.setTables(TABLE_STUDENT);
                builder.setProjectionMap(sUsersColumnProjectionMap);
                builder.appendWhere(ID+"="+uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);

        }

        cursor=builder.query(db,projection,selection,selectionArgs,null,null,sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(),uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (sUriMatcher.match(uri)){
            case USERS_ALL:
                return CONTENT_TYPE_USERS_ALL;
            case USERS_ONE:
                return CONTENT_TYPE_USERS_ONE;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        if(sUriMatcher.match(uri)!=USERS_ALL)
        {
            throw new IllegalArgumentException(" Unknown URI: " + uri);

        }

        SQLiteDatabase db=dbHelper.getWritableDatabase();
        long rowId=db.insert(TABLE_STUDENT,null,values);
        Uri userUri;
        if(rowId>0)
        {
             userUri = ContentUris.withAppendedId(CONTENT_URI, rowId);
            getContext().getContentResolver().notifyChange(userUri, null);
        }
        else {
            throw new IllegalArgumentException(" Unknown URI: " + uri);
        }
        return userUri;

    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String where, @Nullable String[] whereArgs) {
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        int count=0;
        switch (sUriMatcher.match(uri)) {
            case USERS_ALL:

                count = db.delete(TABLE_STUDENT, where, whereArgs);

                break;
            case USERS_ONE:
                String rowId = uri.getPathSegments().get(1);
                count = db.delete(
                        TABLE_STUDENT,
                        ID
                                + " = "
                                + rowId
                                + (!TextUtils.isEmpty(where) ? " AND (" + where
                                + ")" : ""), whereArgs);

                break;
        }
        if (count>0)
        {
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String where, @Nullable String[] whereArgs) {
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        int count=0;
        switch (sUriMatcher.match(uri)) {

            case USERS_ALL:
                count=db.update(TABLE_STUDENT,values,where,whereArgs);

                break;
            case USERS_ONE:
                String rowId = uri.getPathSegments().get(1);
                count = db.update(
                        TABLE_STUDENT,
                        values,
                        ID
                                + " = "
                                + rowId
                                + (!TextUtils.isEmpty(where) ? " AND (" + where
                                + ")" : ""), whereArgs);
                break;

        }

        if (count>0)
        {
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return count;
    }
}

