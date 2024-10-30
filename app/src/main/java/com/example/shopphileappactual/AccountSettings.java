package com.example.shopphileappactual;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AccountSettings extends AppCompatActivity {
    ImageButton back;
    Button logout;
    GlamGrabAuthentication db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_account_settings);

        db = new GlamGrabAuthentication(this);

        back = findViewById(R.id.imageBack);
        logout = findViewById(R.id.LogoutButton);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              if(LogOutUser()){
                  Toast.makeText(AccountSettings.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
                  startActivity(new Intent(AccountSettings.this, MainActivity.class));
              }else{
                  Toast.makeText(AccountSettings.this, "Logout failed", Toast.LENGTH_SHORT).show();
              }
            }
        });




        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    boolean LogOutUser(){
        Cursor cursor = db.fetchDataFromDB();
        if(cursor!=null && cursor.getCount() > 0){
           while(cursor.moveToNext()){
               String username = cursor.getString(1);
               db.setLoggedIn(username, false);
           }
           cursor.close();
           return true;
        }
        return false;
    }
}