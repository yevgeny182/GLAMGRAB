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
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AccountPage extends AppCompatActivity {
    private ImageButton account, home, settings, likes, discover;
    private TextView accountTxt, homeTxt, userNameTxt, userTypeTxt;

    ArrayList<String> userID, username, user_type, shop_name, isLoggedin;
    GlamGrabAuthentication authDB;
    private FirebaseAuth mAuth;
    private FirebaseFirestore userInfoDB;
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

        mAuth = FirebaseAuth.getInstance();
        userInfoDB = FirebaseFirestore.getInstance();

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
//            public void onClick(View view) {
//                Cursor cursor = authDB.fetchDataFromDB();
//                boolean isLoggedIn = false;
//                if(cursor != null && cursor.getCount() > 0){
//                   while(cursor.moveToNext()){
//                       String username = cursor.getString(1);
//
//                       if(authDB.isLoggedIn(username)){
//                           Log.d("DEBUG data",  "Logged in user: " + username);
//                           isLoggedIn = true;
//                           break;
//                       }
//                   }
//                   if(isLoggedIn){
//                       startActivity(new Intent(AccountPage.this, DiscoverPage.class));
//                   }else{
//                       startActivity(new Intent(AccountPage.this, LoginPage.class));
//                   }
//                }else{
//                    startActivity(new Intent(AccountPage.this, LoginPage.class));
//                }
//                cursor.close();
//            }
            public void onClick(View view) {
                //Cursor cursor = authDB.fetchDataFromDB();
                FirebaseUser currUser = mAuth.getCurrentUser();
                boolean isUserLoggedIn = currUser != null ? true : false;
                Log.d("DEBUG", "User logged in? " + isUserLoggedIn);
                  /*
                if(cursor != null && cursor.getCount() > 0  ) {
                    /*while (cursor.moveToNext()) {
                        String username = cursor.getString(1);
                        if (authDB.isLoggedIn(username)) {
                            Log.d("DEBUG data",  "Logged in user: " + username);
                            isUserLoggedIn = true;
                            break;
                        }
                    }

                    */

                if (isUserLoggedIn) {
                    startActivity(new Intent(AccountPage.this, LikesPage.class));
                } else {
                    startActivity(new Intent(AccountPage.this, LoginPage.class));
                }

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


        //displayAuthDataFromDB();
        displayAuthDataFromFirebase();

        likes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Cursor cursor = authDB.fetchDataFromDB();
                FirebaseUser currUser = mAuth.getCurrentUser();
                boolean isUserLoggedIn = currUser != null ? true : false;
                Log.d("DEBUG", "User logged in? " + isUserLoggedIn);
                  /*
                if(cursor != null && cursor.getCount() > 0  ) {
                    /*while (cursor.moveToNext()) {
                        String username = cursor.getString(1);
                        if (authDB.isLoggedIn(username)) {
                            Log.d("DEBUG data",  "Logged in user: " + username);
                            isUserLoggedIn = true;
                            break;
                        }
                    }

                    */

                    if (isUserLoggedIn) {
                        startActivity(new Intent(AccountPage.this, LikesPage.class));
                    } else {
                        startActivity(new Intent(AccountPage.this, LoginPage.class));
                    }

                }
                //cursor.close();

        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    /*
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
     */


    private void displayAuthDataFromFirebase() {
        FirebaseUser currUser = mAuth.getCurrentUser();

        if (currUser != null) {
            String userEmail = currUser.getEmail();
            String userType = "Non User";

            Log.d("DEBUG data", "Current User ID: " + currUser.getUid());
            Log.d("DEBUG data", "Current User Email: " + userEmail);


            login.setVisibility(View.GONE);
            signup.setVisibility(View.GONE);


            userNameTxt.setVisibility(View.VISIBLE);
            userNameTxt.setText(userEmail);

            userTypeTxt.setVisibility(View.VISIBLE);
            userTypeTxt.setText(userType);  // Display the default userType initially


            userInfoDB.collection("users")
                    .whereEqualTo("email", userEmail)  // Match the email from Firebase Authentication
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if (!queryDocumentSnapshots.isEmpty()) {
                                // Get all data if match from Array 0
                                DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);

                                String userType = documentSnapshot.getString("userType");
                                String shopName = documentSnapshot.getString("shopName");
                                String home = documentSnapshot.getString("home");

                                // Debugging logs to see the values fetched
                                Log.d("DEBUG data", " userType: " + userType);
                                Log.d("DEBUG data", " shopName: " + shopName);
                                Log.d("DEBUG data", " home: " + home);

                                if (userType != null) {
                                    // Update UI with the fetched data
                                    userNameTxt.setText(shopName);  // Display shopName (if available)
                                    userTypeTxt.setText(userType.equals("customer") ? "Customer" : "Seller");  // Display userType
                                } else {
                                    userTypeTxt.setText("Customer");
                                }
                            } else {
                                Log.d("DEBUG data", "No user data found in Firestore.");
                                userTypeTxt.setText("Non User");
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Handle failure to fetch user data
                            Log.e("DEBUG data", "Error getting user data: " + e.getMessage());
                            userTypeTxt.setText("Customer");
                        }
                    });

        } else {
            // If the user not logged in
            login.setVisibility(View.VISIBLE);
            signup.setVisibility(View.VISIBLE);
            userNameTxt.setVisibility(View.GONE);
            userTypeTxt.setVisibility(View.GONE);
        }
    }



}