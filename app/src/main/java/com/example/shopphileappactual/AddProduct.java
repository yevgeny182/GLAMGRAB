package com.example.shopphileappactual;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class AddProduct extends AppCompatActivity {
    ImageButton back;
    AppCompatButton imageSelectorButton, submit;
    ImageView productImage;
    ActivityResultLauncher<Intent> resLauncher;

    EditText prodName, prodDesc, prodPrice, prodCategory;
    String imagePath = null; // Switch to String to store image path

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        back = findViewById(R.id.imageBack);
        imageSelectorButton = findViewById(R.id.buttonImage);
        productImage = findViewById(R.id.prodImage);
        submit = findViewById(R.id.submitDataButton);

        prodName = findViewById(R.id.prodBrand);
        prodDesc = findViewById(R.id.prodDesc);
        prodPrice = findViewById(R.id.prodPrice);
        prodCategory = findViewById(R.id.prodCat);

        back.setOnClickListener(view -> finish());
        registerResult();

        imageSelectorButton.setOnClickListener(view -> pickImage());

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (prodName.getText().toString().isEmpty()
                        && prodDesc.getText().toString().isEmpty()
                        && prodPrice.getText().toString().trim().isEmpty()
                        && prodCategory.getText().toString().trim().isEmpty()) {
                    Toast.makeText(AddProduct.this, "Fields must not be empty", Toast.LENGTH_SHORT).show();
                } else {
                    MyDataBaseHelper myDb = new MyDataBaseHelper(AddProduct.this);
                    myDb.createData(
                            prodName.getText().toString().trim(),
                            prodDesc.getText().toString().trim(),
                            prodCategory.getText().toString().trim(),
                            Float.valueOf(prodPrice.getText().toString().trim()),
                            imagePath
                    );
                    Toast.makeText(AddProduct.this, "Product Details Added Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        resLauncher.launch(intent);
    }

    private void registerResult() {
        resLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                            Uri imageUri = result.getData().getData();
                            productImage.setImageURI(imageUri);
                            try {
                                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                                // Save the image and get the file path
                                imagePath = saveImageToFile(bitmap);
                                // Load the image back from file path
                                Bitmap loadedBitmap = BitmapFactory.decodeFile(imagePath);
                                if (loadedBitmap != null) {
                                    productImage.setImageBitmap(loadedBitmap);
                                } else {
                                    Toast.makeText(AddProduct.this, "Failed to load image from file", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(AddProduct.this, "Failed to load image", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(AddProduct.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    // Method to save the selected image to a file and return the file path
    private String saveImageToFile(Bitmap bitmap) {
        File imageFile = new File(getExternalFilesDir(null), "product_image_" + System.currentTimeMillis() + ".png");
        try (FileOutputStream fos = new FileOutputStream(imageFile)) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            return imageFile.getAbsolutePath();  // Return the file path
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to save image", Toast.LENGTH_SHORT).show();
            return null;
        }
    }
}
