package com.example.shopphileappactual;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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

public class ProductPreview extends AppCompatActivity {

    ImageButton back;
    TextView prodTitle, prodPrice, prodDesc, prodCategory;
    ImageView prodImage;
    String id, title, price, description, category;
    TextView edit;



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


        edit = findViewById(R.id.editListing);


        back.setOnClickListener(view -> finish());
        edit.setOnClickListener(view -> {
            Intent intent = new Intent(ProductPreview.this, UpdateProduct.class);
            intent.putExtra("id", id);
            intent.putExtra("title", title);
            intent.putExtra("description", description);
            intent.putExtra("price", price);
            intent.putExtra("category", category);
            intent.putExtra("image", getIntent().getByteArrayExtra("image"));
            startActivityForResult(intent, 1);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        getData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {

            title = data.getStringExtra("title");
            description = data.getStringExtra("description");
            price = data.getStringExtra("price");
            category = data.getStringExtra("category");
            byte[] updatedImage = data.getByteArrayExtra("image");


            prodTitle.setText(title);
            prodDesc.setText(description);
            prodPrice.setText(price);
            prodCategory.setText(category);

            if (updatedImage != null && updatedImage.length > 0) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(updatedImage, 0, updatedImage.length);
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

            prodTitle.setText(title);
            prodPrice.setText(price);
            prodDesc.setText(description);
            prodCategory.setText(category);

            byte[] imageByte = getIntent().getByteArrayExtra("image");
            if (imageByte != null && imageByte.length > 0) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length);
                prodImage.setImageBitmap(bitmap);
            } else {
                prodImage.setImageResource(R.drawable.placeholder_image);
            }
        } else {
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        }
    }
}
