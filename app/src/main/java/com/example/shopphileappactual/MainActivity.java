package com.example.shopphileappactual;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView recycler;
    ImageButton add, account, heartButton, home, discover;
    TextView Account, Home;
    MyDataBaseHelper mainDB;
    ArrayList<String> prodID, prodName, prodDesc, prodPrice, prodCategory;
    ArrayList<String> prodImg;  // Changed to ArrayList<String> for image paths
    ProductAdapter prodAdapter;
    GlamGrabAuthentication authDB;
    ArrayList<String> userID, username, user_type, shop_name, isLoggedin;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        recycler = findViewById(R.id.recyclerViewProducts);
        add = findViewById(R.id.addListing);


        //image button footer
        account = findViewById(R.id.AccountsMePage);
        home = findViewById(R.id.homeMainButton);


        //textview button clickable
        Account = findViewById(R.id.AccountsMePage2Txt);
        Home = findViewById(R.id.homeMainTxt);

        //color for footer buttons
        int color = Color.parseColor("#FEC63A");
        home.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        Home.setTextColor(Color.parseColor("#FEC63A"));


        discover = findViewById(R.id.discoverMainButton);
        heartButton = findViewById(R.id.heart);


        //account image button intent to AccountPage.java
        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toAccountPage = new Intent(MainActivity.this, AccountPage.class);
                startActivity(toAccountPage);
            }
        });




        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addProd = new Intent(MainActivity.this, AddProduct.class);
                startActivityForResult(addProd, 1);
            }
        });

        mainDB = new MyDataBaseHelper(MainActivity.this);
        prodID = new ArrayList<>();
        prodName = new ArrayList<>();
        prodDesc = new ArrayList<>();
        prodPrice = new ArrayList<>();
        prodCategory = new ArrayList<>();
        prodImg = new ArrayList<>();

        displayDataFromDB();

        prodAdapter = new ProductAdapter(MainActivity.this, MainActivity.this, prodID, prodName, prodDesc, prodPrice, prodCategory, prodImg);
        recycler.setAdapter(prodAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, 3);
        recycler.setLayoutManager(gridLayoutManager);

        //userID, username, user_type, shop_name, isLoggedin
        authDB = new GlamGrabAuthentication(MainActivity.this);
        userID = new ArrayList<>();
        username = new ArrayList<>();
        user_type = new ArrayList<>();
        shop_name = new ArrayList<>();
        isLoggedin = new ArrayList<>();
        authDataFromDB();


        discover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, DiscoverPage.class));
                /*
                Cursor cursor = authDB.fetchDataFromDB();
                boolean isUserLoggedIn = false;  // Flag to check if any user is logged in
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        String username = cursor.getString(1);
                        if (authDB.isLoggedIn(username)) {
                            Log.d("DEBUG data",  "Logged in user: " + username);
                            isUserLoggedIn = true;
                            break;
                        }
                    }

                    if (isUserLoggedIn) {
                        Log.d("DEBUG", "User is logged in, redirecting to DiscoverPage.");

                    } else {
                        Log.d("DEBUG", "User is not logged in, redirecting to LoginPage.");
                        startActivity(new Intent(MainActivity.this, LoginPage.class));
                    }
                } else {
                    Log.d("DEBUG", "No users in database, redirecting to LoginPage.");
                    startActivity(new Intent(MainActivity.this, LoginPage.class));
                }

                if (cursor != null) {
                    cursor.close();
                }
                 */
            }
        });

        heartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor cursor = authDB.fetchDataFromDB();
                boolean isUserLoggedIn = false;
                if(cursor!=null && cursor.getCount() > 0){
                    while (cursor.moveToNext()) {
                        String username = cursor.getString(1);
                        if (authDB.isLoggedIn(username)) {
                            Log.d("DEBUG data",  "Logged in user: " + username);
                            isUserLoggedIn = true;
                            break;  // Exit loop once a logged-in user is found
                        }
                    }
                    if (isUserLoggedIn) {
                        startActivity(new Intent(MainActivity.this, LikesPage.class));
                    } else {
                        startActivity(new Intent(MainActivity.this, LoginPage.class));
                    }//
                }else{
                    startActivity(new Intent(MainActivity.this, LoginPage.class));
                }


            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            recreate();
        }
    }

    // Method to display data from the database
    void displayDataFromDB() {
        Cursor cursor = mainDB.readData();
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No data available", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                prodID.add(cursor.getString(0));
                prodName.add(cursor.getString(1));
                prodDesc.add(cursor.getString(2));
                prodCategory.add(cursor.getString(3));
                prodPrice.add(cursor.getString(4));
                prodImg.add(cursor.getString(5));  // Retrieving image path as String
            }
        }
    }

    void authDataFromDB(){
        Cursor cursor = authDB.fetchDataFromDB();
        boolean isSeller = false;
        if(cursor != null && cursor.getCount() > 0){
            while(cursor.moveToNext()){
                String id = cursor.getString(0);
                String usernameValue = cursor.getString(1);
                String userType = cursor.getString(3);
                String shopName = cursor.getString(4);

                userID.add(id);
                username.add(usernameValue);
                user_type.add(userType);
                shop_name.add(shopName);

                if(authDB.isLoggedIn(usernameValue)){
                    switch(userType){
                        case "customer":
                        Log.d("DEBUG Data", "Customer " + usernameValue + " is logged in");
                            add.setVisibility(View.GONE);
                        break;
                        case "seller":
                        isSeller = true;
                        Log.d("DEBUG Data", "Seller " + usernameValue + " is logged in");
                            add.setVisibility(View.VISIBLE);
                        break;
                        default:
                            Log.d("DEBUG Data", "problem defaulting in default " + usernameValue + " is logged in");
                            add.setVisibility(View.GONE);
                        break;
                    }
                    break;
                }
            }
            if (!isSeller) {
                add.setVisibility(View.GONE);
                Log.d("DEBUG Data", "No logged-in user found or user is a customer, hiding add button.");
            }else{
                Log.d("DEBUG Data", "im in else block, seller is logged in");
            }
        }else{
            Toast.makeText(this, "Auth Error", Toast.LENGTH_SHORT).show();
        }

        if (cursor != null) {
            cursor.close();
        }
    }


}
