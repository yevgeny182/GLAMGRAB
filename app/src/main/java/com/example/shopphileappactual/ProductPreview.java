package com.example.shopphileappactual;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class ProductPreview extends AppCompatActivity {

    ImageButton back;
    TextView prodTitle, prodPrice, prodDesc, prodCategory;
    ImageView prodImage, like;
    String id, title, price, description, category, image;
    TextView edit;

    GlamGrabAuthentication authDB;
    LikesDatabaseHelper likesDB;
    MyDataBaseHelper productDB;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product_preview);

        back = findViewById(R.id.imageBack);
        prodTitle = findViewById(R.id.prodTitle);
        prodDesc = findViewById(R.id.prodDescription);
        prodPrice = findViewById(R.id.price);
        prodImage = findViewById(R.id.imageMain);
        prodCategory = findViewById(R.id.category);
        like = findViewById(R.id.imageViewFav);


        edit = findViewById(R.id.editListing);


        back.setOnClickListener(view -> finish());
        edit.setOnClickListener(view -> {
            Intent intent = new Intent(ProductPreview.this, UpdateProduct.class);
            intent.putExtra("id", id);
            intent.putExtra("title", title);
            intent.putExtra("description", description);
            intent.putExtra("price", price);
            intent.putExtra("category", category);
            intent.putExtra("image", image);
            startActivityForResult(intent, 1);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor cursor = authDB.fetchDataFromDB();
                int userID = -1;
                boolean isUserLoggedIn = false;  // Flag to check if any user is logged in
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        String username = cursor.getString(1);
                        if (authDB.isLoggedIn(username)) {
                            isUserLoggedIn = true;
                            userID = authDB.getUserID(username);
                            Log.d("DEBUG", "userID: " + userID);
                            Log.d("DEBUG","prodID: " + Integer.parseInt(id));
                            break;
                        }
                    }

                    if (isUserLoggedIn && userID != - 1) {
                      if(!likesDB.isProductLiked(userID, Integer.parseInt(id))){
                          likesDB.likeProduct(userID, Integer.parseInt(id));
                          Toast.makeText(ProductPreview.this, "Product added to your likes!", Toast.LENGTH_SHORT).show();
                          startActivity(new Intent(ProductPreview.this, LikesPage.class));
                      }else{
                          Snackbar.make(view, "Product already added to your likes!", Snackbar.LENGTH_SHORT).show();
                      }
                    }else{
                        Toast.makeText(ProductPreview.this, "You must login or sign up to like products!", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(ProductPreview.this, LoginPage.class));
                    }
                }

                if (cursor != null) {
                    cursor.close();
                }
            }
        });


        getData();


        authDB = new GlamGrabAuthentication(ProductPreview.this);
        authDataFromDB();
        likesDB = new LikesDatabaseHelper(ProductPreview.this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {

            title = data.getStringExtra("title");
            description = data.getStringExtra("description");
            price = data.getStringExtra("price");
            category = data.getStringExtra("category");
            image = data.getStringExtra("image");



            prodTitle.setText(title);
            prodDesc.setText(description);
            prodPrice.setText(price);
            prodCategory.setText(category);

            if (image != null && !image.isEmpty()) {
                Bitmap bitmap = BitmapFactory.decodeFile(image);
                prodImage.setImageBitmap(bitmap);
            } else {
                prodImage.setImageResource(R.drawable.placeholder_image);
            }
        }
    }

    void getData() {
        if (getIntent().hasExtra("id") && getIntent().hasExtra("title") && getIntent().hasExtra("description")
                && getIntent().hasExtra("price") && getIntent().hasExtra("category") && getIntent().hasExtra("image")) {
            id = getIntent().getStringExtra("id");
            title = getIntent().getStringExtra("title");
            description = getIntent().getStringExtra("description");
            price = getIntent().getStringExtra("price");
            category = getIntent().getStringExtra("category");
            image = getIntent().getStringExtra("image");

            prodTitle.setText(title);
            prodPrice.setText(price);
            prodDesc.setText(description);
            prodCategory.setText(category);

            if (image != null && !image.isEmpty()) {
                Bitmap bitmap = BitmapFactory.decodeFile(image);
                prodImage.setImageBitmap(bitmap);
            } else {
                prodImage.setImageResource(R.drawable.placeholder_image);
            }

        } else {
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        }
    }

    void authDataFromDB(){
        Cursor cursor = authDB.fetchDataFromDB();
        boolean isSeller = false;
        if(cursor != null && cursor.getCount() > 0){
            while(cursor.moveToNext()){
                String usernameValue = cursor.getString(1);
                String userType = cursor.getString(3);
                if(authDB.isLoggedIn(usernameValue)){
                    switch(userType){
                        case "customer":
                            Log.d("DEBUG Data", "Customer " + usernameValue + " is logged in");
                            break;
                        case "seller":
                            isSeller = true;
                            Log.d("DEBUG Data", "Seller " + usernameValue + " is logged in");
                            break;
                        default:
                            Log.d("DEBUG Data", "problem defaulting in default " + usernameValue + " is logged in");
                            break;
                    }
                    break;
                }
            }
            if(isSeller){
                edit.setVisibility(View.VISIBLE);
            }else{
                edit.setVisibility(View.GONE);
            }
        }else{
            Toast.makeText(this, "Auth Error", Toast.LENGTH_SHORT).show();
        }
        if (cursor != null) {
            cursor.close();
        }
    }



}
