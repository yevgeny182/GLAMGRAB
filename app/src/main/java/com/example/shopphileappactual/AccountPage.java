package com.example.shopphileappactual;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
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
    private ImageButton account, home, settings;
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
                 // Store as String for consistency with other lists

                // Set LoggedIn status
                if (authDB.isLoggedIn(usernameValue)  && userType.equals("customer")) {

                    login.setVisibility(View.GONE);
                    signup.setVisibility(View.GONE);

                    userNameTxt.setVisibility(View.VISIBLE);
                    userNameTxt.setText(usernameValue);

                    userTypeTxt.setVisibility(View.VISIBLE);
                    userTypeTxt.setText("Customer");
                }else if(authDB.isLoggedIn(usernameValue) && userType.equals("seller")){
                    login.setVisibility(View.GONE);
                    signup.setVisibility(View.GONE);

                    userNameTxt.setVisibility(View.VISIBLE);
                    userNameTxt.setText(shopName);

                    userTypeTxt.setVisibility(View.VISIBLE);
                    userTypeTxt.setText("Seller");
                }else{

                    login.setVisibility(View.VISIBLE);
                    signup.setVisibility(View.VISIBLE);

                    userTypeTxt.setVisibility(View.GONE);
                    userNameTxt.setVisibility(View.GONE);
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