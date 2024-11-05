package com.example.shopphileappactual;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class LikesDatabaseHelper  extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "GlamGrabLikes.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME_LIKES = "LikesTable";
    private static final String LIKE_ID = "like_id";
    private static final String USER_ID = "user_id";
    private static final String PROD_ID = "product_id";

    public LikesDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String tableQuery = "CREATE TABLE " + TABLE_NAME_LIKES + " (" +
                LIKE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                USER_ID + " INTEGER, " +
                PROD_ID + " INTEGER);"; // Removed foreign keys
        sqLiteDatabase.execSQL(tableQuery);
    }



    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVer, int newVer){
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_LIKES);
        onCreate(sqLiteDatabase);
    }

    public void likeProduct(int userID, int prodID){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USER_ID, userID);
        values.put(PROD_ID, prodID);
        db.insert(TABLE_NAME_LIKES, null, values);
        db.close();
    }

    public void unlikeProduct(int userId, int productId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME_LIKES, USER_ID + "=? AND " + PROD_ID + "=?", new String[]{String.valueOf(userId), String.valueOf(productId)});
        db.close();
    }

    public boolean isProductLiked(int userId, int productId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME_LIKES + " WHERE " + USER_ID + "=? AND " + PROD_ID + "=?",
            new String[]{String.valueOf(userId), String.valueOf(productId)});

        boolean isLiked = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return isLiked;
    }


    public Cursor fetchLikesByUser(String userId, String productId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME_LIKES + " WHERE " +
                USER_ID + " = ? AND " +
                PROD_ID + " = ?";
        return db.rawQuery(query, new String[]{userId, productId});
    }

    public Cursor fetchLikesByUser(String userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME_LIKES + " WHERE " + USER_ID + " = ?";
        return db.rawQuery(query, new String[]{userId});
    }






}
