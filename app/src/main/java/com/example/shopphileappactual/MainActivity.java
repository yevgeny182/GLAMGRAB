package com.example.shopphileappactual;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView recycler;
    ImageButton add;
    MyDataBaseHelper mainDB;
    ArrayList<String> prodID, prodName, prodDesc, prodPrice, prodCategory;
    ArrayList<String> prodImg;  // Changed to ArrayList<String> for image paths
    ProductAdapter prodAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        recycler = findViewById(R.id.recyclerViewProducts);
        add = findViewById(R.id.addListing);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addProd = new Intent(MainActivity.this, AddProduct.class);
                startActivityForResult(addProd, 1);
            }
        });

        mainDB = new MyDataBaseHelper(MainActivity.this);
        prodID = new ArrayList<>();
        prodName = new ArrayList<>();
        prodDesc = new ArrayList<>();
        prodPrice = new ArrayList<>();
        prodCategory = new ArrayList<>();
        prodImg = new ArrayList<>();

        displayDataFromDB();

        prodAdapter = new ProductAdapter(MainActivity.this, MainActivity.this, prodID, prodName, prodDesc, prodPrice, prodCategory, prodImg);
        recycler.setAdapter(prodAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, 3);
        recycler.setLayoutManager(gridLayoutManager);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            recreate();
        }
    }

    // Method to display data from the database
    void displayDataFromDB() {
        Cursor cursor = mainDB.readData();
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No data available", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                prodID.add(cursor.getString(0));
                prodName.add(cursor.getString(1));
                prodDesc.add(cursor.getString(2));
                prodCategory.add(cursor.getString(3));
                prodPrice.add(cursor.getString(4));
                prodImg.add(cursor.getString(5));  // Retrieving image path as String
            }
        }
    }
}
