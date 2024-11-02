package com.example.shopphileappactual;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class AccountPage extends AppCompatActivity {
    private ImageButton account, home, settings, likes, discover;
    private TextView accountTxt, homeTxt, userNameTxt, userTypeTxt;

    ArrayList<String> userID, username, user_type, shop_name, isLoggedin;
    GlamGrabAuthentication authDB;

    Button login, signup;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_account_page);

        account = findViewById(R.id.AccountsMePage_Acccount);
        accountTxt = findViewById(R.id.AccountsMePage2);


        home = findViewById(R.id.homeButtonInAccountPage);
        homeTxt = findViewById(R.id.homeTextInAccountsPage);

        userNameTxt = findViewById(R.id.firstAndLastName);
        userTypeTxt = findViewById(R.id.userType);

        //settings
        settings = findViewById(R.id.settingsIcon);


        //btn login
        login = findViewById(R.id.loginButton);
        signup = findViewById(R.id.signUpButton);

        //discover
        discover = findViewById(R.id.discoverButtonInAccountPage);
        //likes
        likes = findViewById(R.id.likesButtonInAccountPage);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent backToMainHomePage = new Intent( AccountPage.this,  MainActivity.class);
                startActivity(backToMainHomePage);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toLoginScreen = new Intent(AccountPage.this, LoginPage.class);
                startActivity(toLoginScreen);
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AccountPage.this, RegisterPage.class));
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AccountPage.this, AccountSettings.class));
            }
        });

        discover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor cursor = authDB.fetchDataFromDB();
                boolean isLoggedIn = false;
                if(cursor != null && cursor.getCount() > 0){
                   while(cursor.moveToNext()){
                       String username = cursor.getString(1);

                       if(authDB.isLoggedIn(username)){
                           Log.d("DEBUG data",  "Logged in user: " + username);
                           isLoggedIn = true;
                           break;
                       }
                   }
                   if(isLoggedIn){
                       startActivity(new Intent(AccountPage.this, DiscoverPage.class));
                   }else{
                       startActivity(new Intent(AccountPage.this, LoginPage.class));
                   }
                }else{
                    startActivity(new Intent(AccountPage.this, LoginPage.class));
                }
                cursor.close();
            }
        });



        int color = Color.parseColor("#FEC63A");
        account.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        accountTxt.setTextColor(Color.parseColor("#FEC63A"));

        //database stuff
        //userID, username, user_type, shop_name, isLoggedin
        authDB = new GlamGrabAuthentication(this);
        userID = new ArrayList<>();
        username = new ArrayList<>();
        user_type = new ArrayList<>();
        shop_name = new ArrayList<>();
        isLoggedin = new ArrayList<>();


        displayAuthDataFromDB();

        likes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor cursor = authDB.fetchDataFromDB();
                boolean isUserLoggedIn = false;
                if(cursor != null && cursor.getCount() > 0){
                    while (cursor.moveToNext()) {
                        String username = cursor.getString(1);
                        if (authDB.isLoggedIn(username)) {
                            Log.d("DEBUG data",  "Logged in user: " + username);
                            isUserLoggedIn = true;
                            break;  // Exit loop once a logged-in user is found
                        }
                    }
                    if (isUserLoggedIn) {
                        startActivity(new Intent(AccountPage.this, LikesPage.class));
                    } else {
                        startActivity(new Intent(AccountPage.this, LoginPage.class));
                    }

                }else{
                    startActivity(new Intent(AccountPage.this, LoginPage.class));
                }
                cursor.close();
            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    void displayAuthDataFromDB() {
        Cursor cursor = authDB.fetchDataFromDB();

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                // Assuming your database has columns ordered as: ID, username, password, user_type, shop_name, is_logged_in
                String userId = cursor.getString(0);
                String usernameValue = cursor.getString(1);
                String userType = cursor.getString(3);
                String shopName = cursor.getString(4);


                // Populate ArrayLists
                userID.add(userId);
                username.add(usernameValue);
                user_type.add(userType);
                shop_name.add(shopName);

                // Set LoggedIn status
                if(authDB.isLoggedIn(usernameValue)){
                    switch(userType){
                        case "customer":
                            Log.d("CustomerLogin", "Customer " + usernameValue + " is logged in");
                            login.setVisibility(View.GONE);
                            signup.setVisibility(View.GONE);

                            userNameTxt.setVisibility(View.VISIBLE);
                            userNameTxt.setText(usernameValue);

                            userTypeTxt.setVisibility(View.VISIBLE);
                            userTypeTxt.setText("Customer");
                            break;

                        case "seller":
                            Log.d("CustomerLogin", "Seller " + usernameValue + " is logged in");
                            login.setVisibility(View.GONE);
                            signup.setVisibility(View.GONE);

                            userNameTxt.setVisibility(View.VISIBLE);
                            userNameTxt.setText(shopName);

                            userTypeTxt.setVisibility(View.VISIBLE);
                            userTypeTxt.setText("Seller");
                            break;
                        default:
                            Toast.makeText(this, "usertype switch in default condition", Toast.LENGTH_SHORT).show();
                            break;

                    }
                }

            }
        } else {
            Toast.makeText(this, "No user data available", Toast.LENGTH_SHORT).show();
        }

        if (cursor != null) {
            cursor.close();
        }
    }
}