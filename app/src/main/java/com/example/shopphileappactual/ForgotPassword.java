package com.example.shopphileappactual;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;

public class ForgotPassword extends AppCompatActivity {

    GlamGrabAuthentication authDB;
    EditText username;
    Button next;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password);


        authDB = new GlamGrabAuthentication(this);
        username = findViewById(R.id.usernameInputReset);
        next = findViewById(R.id.userResetNextButton);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = username.getText().toString();
                int userID = authDB.getUserID(user);
                if(authDB.checkUserName(user)){
                    //Toast.makeText(ForgotPassword.this, "User exists", Toast.LENGTH_SHORT).show();
                    Intent forgotpass2 = new Intent(ForgotPassword.this, ForgotPassword2.class);
                    forgotpass2.putExtra("user_id", userID);
                    startActivity(forgotpass2);
                }else{
                    Snackbar.make(view, "⚠\uFE0FUser does not exist", Snackbar.LENGTH_LONG).show();
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