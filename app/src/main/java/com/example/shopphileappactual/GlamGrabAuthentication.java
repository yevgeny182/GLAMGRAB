package com.example.shopphileappactual;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class GlamGrabAuthentication extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "GlamGrab_Auth.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "AuthDataLibrary";
    private static final String USER_ID = "ID";
    private static final String USERNAME ="username";
    private static final String PASSWORD ="password";
    private static final String USER_TYPE = "user_type";
    private static final String SHOP_NAME = "shop_name";
    private static final String IS_LOGGED_IN_USER = "isLoggedIn";
    private static final String HOME_ADD = "home_add";



   GlamGrabAuthentication(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                USERNAME + " TEXT, " +
                PASSWORD + " TEXT, " +
                USER_TYPE + " TEXT, " +
                SHOP_NAME + " TEXT, " +
                IS_LOGGED_IN_USER + " INTEGER, " +  // Ensure this column is added
                HOME_ADD + " TEXT" +  // Adding the home_add column
                ");");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean registerUser(String username, String password, String usertype, String shopname, String homeAdd){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues data = new ContentValues();
        data.put(USERNAME, username);
        data.put(PASSWORD, password);
        data.put(HOME_ADD, homeAdd);
        data.put(IS_LOGGED_IN_USER, 0);
        if (usertype.equals("seller")){
            data.put(USER_TYPE, usertype);
            data.put(SHOP_NAME, shopname);
        }else{
            data.put(USER_TYPE, usertype);
        }
        long result = db.insert(TABLE_NAME, null, data);
        return result != -1;
    }
        /* The checkUser method in your code is designed to verify if a user exists in the database with the given username and password.
         Here’s a step-by-step explanation of what this method does: */
    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE USERNAME=? AND PASSWORD=?", new String[]{username, password});
        if (cursor.getCount() > 0) {
            return true;
        }
        cursor.close();
        return false;
    }

    public void setLoggedIn(String username, boolean login){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues data = new ContentValues();
        data.put(IS_LOGGED_IN_USER, login ? 1 : 0);
        db.update(TABLE_NAME, data, USERNAME + "=?", new String[]{username});
    }

    public boolean isLoggedIn(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + IS_LOGGED_IN_USER + " FROM " + TABLE_NAME + " WHERE USERNAME=?", new String[]{username});
        if (cursor.moveToFirst()) {
            return cursor.getInt(0) == 1; // Convert stored value back to boolean
        }
        cursor.close();
        return false; // Default return if no entry found
    }


    Cursor fetchDataFromDB(){
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public boolean checkUserName(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE USERNAME=?", new String[]{username});
        if (cursor.getCount() > 0) {
            return true;
        }
        cursor.close();
        return false;
    }

    public void resetPassword(String password, int userID){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME + " SET " + PASSWORD + " = ? WHERE " + USER_ID + " = ?";
        SQLiteStatement statement = db.compileStatement(query);

        statement.bindString(1, password);
        statement.bindLong(2, userID);
        statement.executeUpdateDelete();
        statement.close();
        db.close();
    }

    public int getUserID(String username){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT "+ USER_ID + " FROM " + TABLE_NAME + " WHERE " + USERNAME + "=?", new String[]{username});
        if(cursor != null && cursor.moveToFirst()){
            @SuppressLint("Range")
            int userid = cursor.getInt(cursor.getColumnIndex(USER_ID));
            return userid;
        }
        if(cursor != null){
            cursor.close();
        }
        return -1;
    }
}



