package com.example.shopphileappactual;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class MyDataBaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "GlamGrab.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "GlamGrabDataLibrary";
    private static final String PROD_ID = "_id";
    private static final String PROD_TITLE = "prod_title";
    private static final String PROD_DESC = "prod_desc";
    private static final String PROD_PRICE = "prod_price";
    private static final String PROD_CATEGORY = "prod_category";
    private static final String PROD_IMAGE = "prod_image";
    private static final String RATING = "rating";
    private static final String LIKED = "isLiked";


    MyDataBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String productTable = "CREATE TABLE " + TABLE_NAME + " (" +
                PROD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PROD_TITLE + " TEXT, " +
                PROD_DESC + " TEXT, " +
                PROD_CATEGORY + " TEXT, " +
                PROD_PRICE + " TEXT, " +
                PROD_IMAGE + " TEXT, " +
                RATING + " TEXT, " +
                LIKED + " TEXT);";
        sqLiteDatabase.execSQL(productTable);
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public byte[] getBitmapByteArray(Bitmap bitmap){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
        return outputStream.toByteArray();
    }

    void createData(String title, String desc, String category, String price, String image){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues content = new ContentValues();
//        List<String> imagePaths
//        JSONArray json = new JSONArray(imagePaths);
//        String imageJson = json.toString();

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
    void updateData(String rowID, String title, String desc, String category, String price, String image) {
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

    void deleteData(String rowID){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME, "_id=?", new String[]{rowID});
        if(result == -1){
            Toast.makeText(context, "⚠\uFE0F Error deleting data", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "\uD83E\uDDF9\uD83E\uDEA3 Data deletion successful.", Toast.LENGTH_SHORT).show();
        }
    }

    public List<String> getProdImages(int prodID){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =
        db.rawQuery("SELECT " + PROD_IMAGE + " FROM " + TABLE_NAME + " WHERE " + PROD_ID + "=?", new String[]{String.valueOf(prodID)});
        List <String> imagePath = new ArrayList<>();
        if(cursor.moveToFirst()){
            @SuppressLint("Range")
            String imageJson = cursor.getString(cursor.getColumnIndex(PROD_IMAGE));
            try{
                JSONArray json = new JSONArray(imageJson);
                for (int i = 0; i < json.length(); i++){
                    imagePath.add(json.getString(i));
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        cursor.close();
        db.close();
        return imagePath;
    }

}
