package com.example.shopphileappactual;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AccountPage extends AppCompatActivity {
    private ImageButton account, home;
    private TextView accountTxt, homeTxt, userNameTxt;
    boolean LoggedIn = false;

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

        if(!LoggedIn){
            userNameTxt.setVisibility(View.GONE);
        }else{
            userNameTxt.setVisibility(View.VISIBLE);
        }

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent backToMainHomePage = new Intent( AccountPage.this,  MainActivity.class);
                startActivity(backToMainHomePage);
            }
        });

        int color = Color.parseColor("#FEC63A");
        account.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        accountTxt.setTextColor(Color.parseColor("#FEC63A"));



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}