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

public class LikesPage extends AppCompatActivity {
    ImageButton home, discover, likes, account;
    TextView liketext;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_likes_page);

        home = findViewById(R.id.homeButtonInLikesPage);
        discover = findViewById(R.id.discoverButtonInLikesPage);
        likes = findViewById(R.id.likesButtonInLikesPage);
        account = findViewById(R.id.accountButtonInLikesPage);
        liketext = findViewById(R.id.likesTextInLikesPage);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LikesPage.this, MainActivity.class));
            }
        });

        discover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LikesPage.this, DiscoverPage.class));
            }
        });
        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LikesPage.this, AccountPage.class));
            }
        });


        int color = Color.parseColor("#FEC63A");
        likes.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        liketext.setTextColor(Color.parseColor("#FEC63A"));

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}