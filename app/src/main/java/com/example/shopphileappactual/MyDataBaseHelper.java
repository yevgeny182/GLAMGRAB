package com.example.shopphileappactual;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class MyDataBaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "ShopphileDB.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "ShopphileDataLibrary";
    private static final String PROD_ID = "_id";
    private static final String PROD_TITLE = "prod_title";
    private static final String PROD_DESC = "prod_desc";
    private static final String PROD_PRICE = "prod_price";
    private static final String PROD_CATEGORY = "prod_category";
    private static final String PROD_IMAGE = "prod_image";



    MyDataBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE " + TABLE_NAME + " (" +
                PROD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PROD_TITLE + " TEXT, " +
                PROD_DESC + " TEXT, " +
                PROD_CATEGORY + " TEXT, " +
                PROD_PRICE + " FLOAT, " +
                PROD_IMAGE + " BLOB);";
        sqLiteDatabase.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public byte[] getBitmapByteArray(Bitmap bitmap){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        return outputStream.toByteArray();
    }

    void createData(String title, String desc, String category, float price, byte[] image){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues content = new ContentValues();
        content.put(PROD_TITLE, title);
        content.put(PROD_DESC, desc);
        content.put(PROD_CATEGORY, category);
        content.put(PROD_PRICE, price);
        content.put(PROD_IMAGE, image);

        long result = db.insert(TABLE_NAME, null, content);
        if(result == -1){
            Toast.makeText(context, "Failed to add data", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Product Added Successfully!", Toast.LENGTH_SHORT).show();
        }

    }

    Cursor readData(){
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }
    void updateData(String rowID, String title, String desc, String category, float price, byte[] image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PROD_TITLE, title);
        values.put(PROD_DESC, desc);
        values.put(PROD_CATEGORY, category);
        values.put(PROD_PRICE, price);
        values.put(PROD_IMAGE, image);

        long result = db.update(TABLE_NAME, values, "_id=?", new String[]{rowID});
        if (result == -1) {
            Log.e("UpdateData", "Error updating data for ID: " + rowID);
            Toast.makeText(context, "⚠\uFE0F Error updating data", Toast.LENGTH_SHORT).show();
        } else {
            Log.d("UpdateData", "Data updated successfully for ID: " + rowID);
            Toast.makeText(context, "\uD83D\uDCDD Data has been updated", Toast.LENGTH_SHORT).show();
        }
    }

}
