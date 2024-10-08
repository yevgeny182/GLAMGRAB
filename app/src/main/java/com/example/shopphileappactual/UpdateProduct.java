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
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.ByteArrayOutputStream;

public class UpdateProduct extends AppCompatActivity {
    ImageButton back;
    EditText prodTitle, prodPrice, prodDesc, prodCategory;
    ImageView prodImage;
    String id, title, price, description, category;
    AppCompatButton saveButton, imageSubmitButton;
    TextView delete;
    byte[] imageByte;
    ActivityResultLauncher<Intent> resLauncher;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_product);

        back = findViewById(R.id.imageBack2);
        prodTitle = findViewById(R.id.brandName);
        prodPrice = findViewById(R.id.prodPrice2);
        prodDesc = findViewById(R.id.prodDesc2);
        prodCategory = findViewById(R.id.prodCat2);
        prodImage = findViewById(R.id.prodImage2);
        saveButton = findViewById(R.id.saveButton);

        delete = findViewById(R.id.buttonDelete);
        imageSubmitButton = findViewById(R.id.buttonImage2);

        back.setOnClickListener(view -> finish());

        registerResult();
        imageSubmitButton.setOnClickListener(view -> pickImage());

        getData();
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String curTitle = prodTitle.getText().toString();
                String curDesc = prodDesc.getText().toString();
                String curCategory = prodCategory.getText().toString();
                float curPrice = Float.parseFloat(prodPrice.getText().toString());


                prodImage.setDrawingCacheEnabled(true);
                prodImage.buildDrawingCache();
                Bitmap curBitmap = prodImage.getDrawingCache();
                byte[] curImageByte = convertBitmapToByteArray(curBitmap);

                boolean isImageChanged = !java.util.Arrays.equals(imageByte, curImageByte);

                if (curTitle.equals(title) && curDesc.equals(description) &&
                        curCategory.equals(category) && curPrice == Float.parseFloat(price) || !isImageChanged) {
                    Toast.makeText(UpdateProduct.this, "No changes detected.", Toast.LENGTH_SHORT).show();
                } else {
                    MyDataBaseHelper myDB = new MyDataBaseHelper(UpdateProduct.this);
                    if(isImageChanged){
                        myDB.updateData(id, curTitle, curDesc, curCategory, curPrice, curImageByte);
                        Toast.makeText(UpdateProduct.this, "Product updated successfully.", Toast.LENGTH_SHORT).show();

                        /*
                        Intent intent = new Intent(UpdateProduct.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                         */
                    } else {
                        myDB.updateData(id, curTitle, curDesc, curCategory, curPrice, imageByte);
                        Toast.makeText(UpdateProduct.this, "Product and image updated successfully.", Toast.LENGTH_SHORT).show();

                        /*
                        Intent intent = new Intent(UpdateProduct.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                        */
                    }
                }

                Intent resIntent = new Intent();
                resIntent.putExtra("id", id);
                resIntent.putExtra("title", curTitle);
                resIntent.putExtra("description", curDesc);
                resIntent.putExtra("price", String.valueOf(curPrice));
                resIntent.putExtra("category", curCategory);
                resIntent.putExtra("image", isImageChanged ? curImageByte : imageByte);
                setResult(RESULT_OK, resIntent);
                finish();
            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }
    private byte[] convertBitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
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
                            prodImage.setImageURI(imageUri);
                            try{
                                Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                                ByteArrayOutputStream imgStream = new ByteArrayOutputStream();
                                bm.compress(Bitmap.CompressFormat.PNG, 100, imgStream);
                                imageByte = imgStream.toByteArray();
                            }catch (Exception e){
                                e.printStackTrace();
                                Toast.makeText(UpdateProduct.this, "Failed to load image", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(UpdateProduct.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}