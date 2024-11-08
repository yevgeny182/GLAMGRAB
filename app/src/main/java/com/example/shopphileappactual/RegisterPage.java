package com.example.shopphileappactual;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterPage extends AppCompatActivity {
    GlamGrabAuthentication db;
    EditText user, pass, homeAdd, passConfirm;
    Button register, registerSeller;
    ImageButton back;
    TextView login;
    private FirebaseAuth mAuth;


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
        passConfirm = findViewById(R.id.passwordInputRegisterConfirm);

        mAuth = FirebaseAuth.getInstance();


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
                String passwordConfirm = passConfirm.getText().toString();

                //                String username = user.getText().toString();
//                String password = pass.getText().toString();
//                String passwordConfirm = passConfirm.getText().toString();
//                String home = homeAdd.getText().toString();
//                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password) || TextUtils.isEmpty(home)) {
//                    Snackbar.make(view, "⚠\uFE0F All fields must not be empty.", Snackbar.LENGTH_SHORT).show();
//                } else {
//                    String usertype = "customer";
//                    String shopname = "";
//                    boolean inserted = db.registerUser(username, password, usertype, shopname, home);
//                    if(inserted){
//                        Toast.makeText(RegisterPage.this, "You are registered to GLAMGRAB✨! " +
//                                "Please login with your registered credentials", Toast.LENGTH_LONG).show();
//                        startActivity(new Intent(RegisterPage.this, LoginPage.class));
//                    } else {
//                        Toast.makeText(RegisterPage.this, "Failed Registration, Registration Error.", Toast.LENGTH_SHORT).show();
//                    }
//                }



                if (password.equals(passwordConfirm)) {
                    Log.d("DEBUG DATA", "pass1: " + password + "pass2: " + passwordConfirm);
                    if (isValidPassword(password)) {
                        registerNewUser(username, password);
                        startActivity(new Intent(RegisterPage.this, LoginPage.class));
                    } else {
                        Toast.makeText(RegisterPage.this, "Password must be at least 8 characters, " +
                            "with uppercase, lowercase, digit, and special character.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(RegisterPage.this, "Passwords do not match. Please retype.", Toast.LENGTH_LONG).show();
                }

            }
        });

    }
    public void registerNewUser(String username, String password){

        if(TextUtils.isEmpty(username) || TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please fill all fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(username, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(RegisterPage.this, "Registration successful with GLAMGRAB✨!", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(RegisterPage.this, "Registration Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    public boolean isValidPassword(String password){
        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{}|;:'\",.<>?/]).{8,}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        boolean isMatch = matcher.matches();
        Log.d("PasswordCheck", "Overall Match: " + isMatch);
        return isMatch;
    }



}