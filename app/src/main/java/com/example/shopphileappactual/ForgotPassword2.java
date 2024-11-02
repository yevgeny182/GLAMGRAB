package com.example.shopphileappactual;

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

public class ForgotPassword2 extends AppCompatActivity {
    Button reset;
    EditText password;
    GlamGrabAuthentication authDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password2);

        int userID = getIntent().getIntExtra("user_id", -1);

        reset = findViewById(R.id.userResetPassButton);
        password = findViewById(R.id.passwordInput);

        authDB = new GlamGrabAuthentication(this);


        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pass = password.getText().toString();
                if(!pass.isEmpty()){
                    authDB.resetPassword(pass, userID);
                    Toast.makeText(ForgotPassword2.this, "Password reset success", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ForgotPassword2.this, LoginPage.class));
                    finish();
                }else{
                    Snackbar.make(view, "Please enter a new password", Snackbar.LENGTH_LONG).show();
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