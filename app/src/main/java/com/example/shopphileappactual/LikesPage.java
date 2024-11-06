package com.example.shopphileappactual;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Arrays;

public class LikesPage extends AppCompatActivity {
    ImageButton home, discover, likes, account;
    TextView liketext;
    ArrayList<String> prodName, prodDesc, prodPrice, prodCategory, prodImg;
    ArrayList<Integer> prodID;
    LikesDatabaseHelper likesDB;
    GlamGrabAuthentication authDB;
    MyDataBaseHelper productDB;
    RecyclerView recycler;
    Spinner categories;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_likes_page);

        home = findViewById(R.id.homeButtonInLikesPage);
        discover = findViewById(R.id.discoverButtonInLikesPage);
        likes = findViewById(R.id.likesButtonInLikesPage);
        account = findViewById(R.id.accountButtonInLikesPage);
        liketext = findViewById(R.id.likesTextInLikesPage);
        recycler = findViewById(R.id.recyclerViewProductsFromLikes);
        categories = findViewById(R.id.category);



        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LikesPage.this, MainActivity.class));
            }
        });

        discover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LikesPage.this, DiscoverPage.class));
            }
        });
        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LikesPage.this, AccountPage.class));
            }
        });


        productDB = new MyDataBaseHelper(LikesPage.this);
        likesDB = new LikesDatabaseHelper(LikesPage.this);
        authDB = new GlamGrabAuthentication(LikesPage.this);


        prodID = new ArrayList<>();
        prodName = new ArrayList<>();
        prodDesc = new ArrayList<>();
        prodPrice = new ArrayList<>();
        prodCategory = new ArrayList<>();
        prodImg = new ArrayList<>();

        LikesProductAdapter adapter = new LikesProductAdapter(LikesPage.this, LikesPage.this, prodID, prodName, prodDesc, prodPrice, prodCategory, prodImg);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(LikesPage.this, 2);
        recycler.setLayoutManager(gridLayoutManager);

        int spanCount = 2; // Number of columns in the GridLayout
        int spacing = 32; // Spacing in pixels, adjust this value as needed
        boolean includeEdge = true; // Set to true to include spacing on the edges
        recycler.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
        recycler.setAdapter(adapter);


        //fetch user id
        Cursor cursor = authDB.fetchDataFromDB();
        int userID = -1;
        boolean isLoggedIn = false;
        if(cursor != null && cursor.getCount() > 0){
            while (cursor.moveToNext()){
                String username = cursor.getString(1);
                if (authDB.isLoggedIn(username)) {
                    userID = cursor.getInt(0);
                    Log.d("DEBUG data",  "Logged in user: " + username);
                    isLoggedIn = true;
                    break;
                }
            }

        }

        if(isLoggedIn && userID != - 1){
            Cursor likesData = likesDB.fetchLikesByUser(String.valueOf(userID));
            if(likesData != null && likesData.getCount() > 0){
                while(likesData.moveToNext()){
                    int productId = likesData.getInt(2);
                    displayProductDataFromDB(productId);
//                    Log.d("DEBUG data", "productID's = " + productId + " from userID " + userID);
                }

            }else{
              Toast.makeText(LikesPage.this, "You have no liked products, browse products now.", Toast.LENGTH_SHORT).show();
                Log.d("DEBUG data", "User has no liked products ");
            }
        } else {
            Log.d("LikesPage", "User not logged in " + userID);
        }

        int color = Color.parseColor("#FEC63A");
        likes.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        liketext.setTextColor(Color.parseColor("#FEC63A"));

        if(prodCategory.isEmpty()){
            Toast.makeText(LikesPage.this, "No categories available.", Toast.LENGTH_SHORT).show();
        }else{
            ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, prodCategory);
            categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            categories.setAdapter(categoryAdapter);
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    void displayProductDataFromDB(int prodIDHolder) {
        Cursor cursor = productDB.readData();
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No data available", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                if (id == prodIDHolder) {
                    String name = cursor.getString(1);
                    String desc = cursor.getString(2);
                    String category = cursor.getString(3);
                    String price = cursor.getString(4);
                    String img = cursor.getString(5);

                    if(category == null || category.trim().isEmpty()){
                        category = "No Category";
                    }
                    // Convert the ID to a String and add to ArrayLists
                    prodID.add(id);
                    prodName.add(name);
                    prodDesc.add(desc);
                    prodCategory.add(category);
                    prodPrice.add(price);
                    prodImg.add(img);

                    // Log the data for debugging
                    Log.d("DisplayProductData", "Product ID: " + prodID);
                    Log.d("DisplayProductData", "Name: " + prodName);
                    Log.d("DisplayProductData", "Description: " + prodDesc);
                    Log.d("DisplayProductData", "Category: " + prodCategory);
                    Log.d("DisplayProductData", "Price: " + prodPrice);
                    Log.d("DisplayProductData", "Image Path: " + prodImg);

                    break;
                }
            }
        }
        cursor.close();
    }

}