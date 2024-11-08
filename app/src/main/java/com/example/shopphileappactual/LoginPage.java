package com.example.shopphileappactual;

import static androidx.core.content.ContextCompat.getSystemService;
import static com.google.android.material.internal.ViewUtils.hideKeyboard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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

import org.w3c.dom.Text;

public class LoginPage extends AppCompatActivity {

    ImageButton back;
    TextView forgotPasswordTxt;
    EditText user, pass;
    GlamGrabAuthentication db;
    Button login, signUpLoginSeller;
    TextView signup;
    private FirebaseAuth mAuth;

    @SuppressLint({"MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_page);

        //database connection
        db = new GlamGrabAuthentication(this);

        //user text fields to input username and password
        user = findViewById(R.id.usernameInput);
        pass = findViewById(R.id.passwordInput);
        login = findViewById(R.id.userLoginButton);
        signUpLoginSeller = findViewById(R.id.loginAsSeller);

        //back
        back = findViewById(R.id.chevronLeft);
        //forgotpassword
        forgotPasswordTxt = findViewById(R.id.forgotText);
        //signup
        signup = findViewById(R.id.signUpText);

        mAuth = FirebaseAuth.getInstance();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginPage.this, MainActivity.class));
            }
        });

        //login authentication
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = user.getText().toString();
                String password = pass.getText().toString();

                 /*
                if(db.checkUser(username, password)){
                    db.setLoggedIn(username, true);
                    startActivity(new Intent(LoginPage.this, MainActivity.class));
                    Intent toMainActivity = new Intent(LoginPage.this, MainActivity.class);
                    startActivity(toMainActivity);
                }else if(username.isEmpty() || password.isEmpty()){
                    Snackbar.make(view, "⚠\uFE0F Please enter your username and password to continue.", Snackbar.LENGTH_SHORT).show();
                }else{
                    Snackbar.make(view, " ⚠\uFE0F Invalid username or password, please try again.", Snackbar.LENGTH_SHORT).show();
                }
                */

                mAuth.signInWithEmailAndPassword(username, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(LoginPage.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(LoginPage.this, MainActivity.class));
                                }else{
                                    Snackbar.make(view, " ⚠\uFE0F Invalid username or password, please try again.", Snackbar.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
        //sign up intent
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             startActivity(new Intent(LoginPage.this, RegisterPage.class));
            }
        });
        //sign up intent seller
        signUpLoginSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginPage.this, RegisterPageSeller.class));
            }
        });
        //forgot password handling
        forgotPasswordTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginPage.this, ForgotPassword.class));
            }
        });


    }





}

