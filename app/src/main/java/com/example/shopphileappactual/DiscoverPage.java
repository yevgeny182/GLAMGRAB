package com.example.shopphileappactual;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
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

public class DiscoverPage extends AppCompatActivity {
    ImageButton discover, home, likes, account;
    TextView discoverText;
    GlamGrabAuthentication authDB;
    MyDataBaseHelper productDB;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_discover_page);

        authDB = new GlamGrabAuthentication(this);
        productDB = new MyDataBaseHelper(this);

        discover = findViewById(R.id.discoverButton);
        discoverText = findViewById(R.id.discoverText);

        home = findViewById(R.id.homeButtonInDiscoverPage);
        likes = findViewById(R.id.likesButtonInDiscoverPage);
        account = findViewById(R.id.accountsButtonInDiscoverPage);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DiscoverPage.this, MainActivity.class));
            }
        });

        likes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DiscoverPage.this, LikesPage.class));
            }
        });




        int color = Color.parseColor("#FEC63A");
        discover.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        discoverText.setTextColor(Color.parseColor("#FEC63A"));






        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}