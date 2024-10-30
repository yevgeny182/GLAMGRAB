package com.example.shopphileappactual;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;

public class RegisterPage extends AppCompatActivity {
    GlamGrabAuthentication db;
    EditText user, pass, homeAdd;
    Button register, registerSeller;
    ImageButton back;
    TextView login;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register_page);

        db = new GlamGrabAuthentication(this);

        back = findViewById(R.id.chevronLeft);
        user = findViewById(R.id.usernameInputRegister);
        pass = findViewById(R.id.passwordInputRegister);
        homeAdd = findViewById(R.id.homeAddress);

        login = findViewById(R.id.loginText);

        register = findViewById(R.id.registerButton);
        registerSeller = findViewById(R.id.signUpAsSeller);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        registerSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterPage.this, RegisterPageSeller.class));
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterPage.this, LoginPage.class));
            }
        });


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = user.getText().toString();
                String password = pass.getText().toString();
                String home = homeAdd.getText().toString();
                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password) || TextUtils.isEmpty(home)) {
                    /* Toast.makeText(RegisterPage.this, "All fields must be filled.", Toast.LENGTH_SHORT).show(); */
                    Snackbar.make(view, "⚠\uFE0F All fields must not be empty.", Snackbar.LENGTH_SHORT).show();
                } else {
                    String usertype = "customer";
                    String shopname = "";
                    boolean inserted = db.registerUser(username, password, usertype, shopname, home);
                    if(inserted){
                        Toast.makeText(RegisterPage.this, "You are registered to GLAMGRAB✨! " +
                                "Please login with your registered credentials", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(RegisterPage.this, LoginPage.class));
                    } else {
                        Toast.makeText(RegisterPage.this, "Failed Registration, Registration Error.", Toast.LENGTH_SHORT).show();
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