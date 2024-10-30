package com.example.shopphileappactual;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;

public class RegisterPageSeller extends AppCompatActivity {
    GlamGrabAuthentication db;
    EditText shopname, userName, pass_word;
    ImageButton back;
    Button register;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register_page_seller);


        back = findViewById(R.id.chevronLeft);
        db = new GlamGrabAuthentication(this);

        shopname = findViewById(R.id.shopName);
        userName = findViewById(R.id.userNameSeller);
        pass_word = findViewById(R.id.passwordSeller);
        register = findViewById(R.id.registerButton);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = userName.getText().toString();
                String password = pass_word.getText().toString();
                String shopName = shopname.getText().toString();

             if(TextUtils.isEmpty(username) || TextUtils.isEmpty(password)){
                 Snackbar.make(view, "Username and Password fields must not be empty.", Snackbar.LENGTH_SHORT).show();
             } else {
                 String usertype = "seller";
                 String home = "";
                 Log.d("RegisterData", "Username: "
                         + username + ", Password: " + password +
                         ", UserType: " + usertype + ", ShopName: "
                         + shopName + ", Home: " + home);

                 boolean inserted = db.registerUser(username, password, usertype,shopName, home);
                     if(inserted){
                         Toast.makeText(RegisterPageSeller.this, "You are registered to GLAMGRAB✨ as Seller.", Toast.LENGTH_SHORT).show();
                         startActivity(new Intent(RegisterPageSeller.this, LoginPage.class));
                     }else{
                         Toast.makeText(RegisterPageSeller.this, "Failed Registration, Registration Error", Toast.LENGTH_SHORT).show();
                     }
              }

            }
        });




        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}