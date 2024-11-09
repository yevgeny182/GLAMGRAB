package com.example.shopphileappactual;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

public class AccountSettings extends AppCompatActivity {
    ImageButton back;
    Button logout;
    GlamGrabAuthentication db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_account_settings);

        db = new GlamGrabAuthentication(this);

        back = findViewById(R.id.imageBack);
        logout = findViewById(R.id.LogoutButton);

        mAuth = FirebaseAuth.getInstance();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alertLogout = new AlertDialog.Builder(AccountSettings.this);
                alertLogout.setTitle("Logout?");
                alertLogout.setMessage("Are you sure you want to logout?");
                alertLogout.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(LogOutUser()){
                            startActivity(new Intent(AccountSettings.this, MainActivity.class));
                            finish();
                        }else{
                            Toast.makeText(AccountSettings.this, "Logout failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                alertLogout.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
             alertLogout.show();
            }
        });




        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    boolean LogOutUser(){

        mAuth.signOut();
        Toast.makeText(this, "Logout Successful", Toast.LENGTH_SHORT).show();
        return true;

//        Cursor cursor = db.fetchDataFromDB();
//        if(cursor!=null && cursor.getCount() > 0){
//           while(cursor.moveToNext()){
//               String username = cursor.getString(1);
//               db.setLoggedIn(username, false);
//           }
//           cursor.close();
//           return true;
//        }
//        return false;



    }

}