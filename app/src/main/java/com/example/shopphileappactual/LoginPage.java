package com.example.shopphileappactual;

import static androidx.core.content.ContextCompat.getSystemService;
import static com.google.android.material.internal.ViewUtils.hideKeyboard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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

import org.w3c.dom.Text;

public class LoginPage extends AppCompatActivity {

    ImageButton back;
    TextView forgotPasswordTxt;
    EditText user, pass;
    GlamGrabAuthentication db;
    Button login, signUpLoginSeller;
    TextView signup;

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

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //login authentication
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = user.getText().toString();
                String password = pass.getText().toString();

                if(db.checkUser(username, password)){
                    Toast.makeText(LoginPage.this, "Successful login!", Toast.LENGTH_SHORT).show();
                    db.setLoggedIn(username, true);
                    startActivity(new Intent(LoginPage.this, MainActivity.class));
                    /* Intent toMainActivity = new Intent(LoginPage.this, MainActivity.class);
                    startActivity(toMainActivity); */
                }else if(username.isEmpty() || password.isEmpty()){
                    Snackbar.make(view, "⚠\uFE0F Please enter your username and password to continue.", Snackbar.LENGTH_SHORT).show();
                }else{
                    Snackbar.make(view, " ⚠\uFE0F Invalid username or password, please try again.", Snackbar.LENGTH_SHORT).show();
                }
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

            }
        });




        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

}

