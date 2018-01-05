package com.example.a.userinfo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import static com.example.a.userinfo.MyContentProvider.CONTENT_URI;

/**
 * Created by A on 20/11/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "studentDb";
    public static final int DB_VERSION = 1;

    public static final String TABLE_STUDENT = "student";

    public static final String STUD_ID = "stud_id";

    public static final String STUD_NAME = "stud_name";

    public static final String STUD_PHONE = "stud_phone";

    public static final String STUD_ADDRESS = "stud_address";

    public static final String STUD_HOBBIES = "stud_hobbies";

    public static final String PHOTO = "photo";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        String createTableQuery = "CREATE TABLE " + TABLE_STUDENT + " ( " + STUD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + STUD_NAME + " TEXT, " + STUD_PHONE + " TEXT, " + STUD_ADDRESS + " TEXT, " + STUD_HOBBIES + " TEXT, "
                + PHOTO + " TEXT )";


        db.execSQL(createTableQuery);


    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new employee
    void addStudent(Student student) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(STUD_NAME, student.getStudName());
        values.put(STUD_PHONE, student.getStudPhone());
        values.put(STUD_ADDRESS, student.getStudAddress());
        values.put(STUD_HOBBIES, student.getStudHobbies());
        values.put(PHOTO, student.getImage());

        // Inserting Row
        db.insert(TABLE_STUDENT, null, values);
        db.close(); // Closing database connection

    }


    // Getting single employee
    Student getStudent(String studName) {
        Student student = null;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_STUDENT, new String[]{STUD_ID,
                        STUD_NAME, STUD_PHONE, STUD_ADDRESS, STUD_HOBBIES}, STUD_NAME + "=?",
                new String[]{studName}, null, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                student = new Student();
                student.setStudId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(STUD_ID))));
                student.setStudName(cursor.getString(cursor.getColumnIndex(STUD_NAME)));
                student.setStudPhone(cursor.getString(cursor.getColumnIndex(STUD_PHONE)));
                student.setStudAddress(cursor.getString(cursor.getColumnIndex(STUD_ADDRESS)));
                student.setStudHobbies(cursor.getString(cursor.getColumnIndex(STUD_HOBBIES)));
            }
        }

        // return employee
        return student;
    }

    public Cursor getStud(String name) throws SQLException {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor mCursor = db.query(TABLE_STUDENT, new String[]{
                        STUD_NAME, STUD_PHONE, STUD_ADDRESS, STUD_HOBBIES, PHOTO}, STUD_NAME + "=?", new String[]{name}, null,
                null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor getStud1(int name) throws SQLException {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor mCursor = db.query(TABLE_STUDENT, new String[]{
                        STUD_NAME, STUD_PHONE, STUD_ADDRESS, STUD_HOBBIES, PHOTO}, STUD_ID + "=" + name, null,
                null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    // Getting All Employee
    public List<Student> getAllEmployees() {
        List<Student> employeeList = new ArrayList<Student>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_STUDENT;

        SQLiteDatabase db = this.getWritableDatabase();
      Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Student employee = new Student();
                employee.setStudId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(STUD_ID))));
                employee.setStudName(cursor.getString(cursor.getColumnIndex(STUD_NAME)));
                employee.setStudPhone(cursor.getString(cursor.getColumnIndex(STUD_PHONE)));
                employee.setStudAddress(cursor.getString(cursor.getColumnIndex(STUD_ADDRESS)));
                employee.setStudHobbies(cursor.getString(cursor.getColumnIndex(STUD_HOBBIES)));
                employee.setImage(cursor.getString(cursor.getColumnIndex(PHOTO)));
                // Adding contact to list
                employeeList.add(employee);
            } while (cursor.moveToNext());
        }

        // return contact list
        return employeeList;
    }

    public void deleteStud(Student student) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_STUDENT, STUD_NAME + " = ?", new String[]{student.getStudName()});
        db.close();
    }

    void update(Student employee, String name) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(STUD_NAME, employee.getStudName());
        values.put(STUD_PHONE, employee.getStudPhone());
        values.put(STUD_ADDRESS, employee.getStudAddress());
        values.put(STUD_HOBBIES, employee.getStudHobbies());
        values.put(PHOTO, employee.getImage());

        db.update(TABLE_STUDENT, values, STUD_NAME + " = ?",
                new String[]{name});

        db.close(); // Closing database connection

    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
