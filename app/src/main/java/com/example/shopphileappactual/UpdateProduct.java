package com.example.shopphileappactual;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class UpdateProduct extends AppCompatActivity {
    ImageButton back;
    EditText prodTitle, prodPrice, prodDesc, prodCategory;
    ImageView prodImage;
    String id, title, price, description, category;
    AppCompatButton saveButton, imageSubmitButton;
    TextView delete;
    ActivityResultLauncher<Intent> resLauncher;
    String image = null;




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
                String curPrice = prodPrice.getText().toString();

                // Get the current image URI as a string
                String curImageUriString = image; // This is set in pickImage()

                // Check if any changes were made
                boolean isTitleChanged = !curTitle.equals(title);
                boolean isDescChanged = !curDesc.equals(description);
                boolean isCategoryChanged = !curCategory.equals(category);
                boolean isPriceChanged = !curPrice.equals(price);


                boolean isImageChanged = (image != null && !image.isEmpty());


                // Debug logs
                Log.d("UpdateProduct", "isTitleChanged: " + isTitleChanged);
                Log.d("UpdateProduct", "isDescChanged: " + isDescChanged);
                Log.d("UpdateProduct", "isCategoryChanged: " + isCategoryChanged);
                Log.d("UpdateProduct", "isPriceChanged: " + isPriceChanged);
                Log.d("UpdateProduct", "isImageChanged: " + isImageChanged);
                Log.d("UpdateProduct", "Current Image URI: " + curImageUriString);
                Log.d("UpdateProduct", "Stored Image URI: " + image);

                if (!isTitleChanged && !isDescChanged && !isCategoryChanged && !isPriceChanged && !isImageChanged) {
                    Toast.makeText(UpdateProduct.this, "No changes detected.", Toast.LENGTH_SHORT).show();
                } else {
                    MyDataBaseHelper myDB = new MyDataBaseHelper(UpdateProduct.this);
                    // Use the new image if it has changed, else keep the old one
                    String finalImageUri = isImageChanged ? curImageUriString : image;

                    // Log the final image URI being used for the update
                    Log.d("UpdateProduct", "Final Image URI to save: " + finalImageUri);

                    myDB.updateData(id, curTitle, curDesc, curCategory, curPrice, finalImageUri);
                    Toast.makeText(UpdateProduct.this, "Product updated successfully.", Toast.LENGTH_SHORT).show();

                    Intent resIntent = new Intent();
                    resIntent.putExtra("id", id);
                    resIntent.putExtra("title", curTitle);
                    resIntent.putExtra("description", curDesc);
                    resIntent.putExtra("price", String.valueOf(curPrice));
                    resIntent.putExtra("category", curCategory);
                    resIntent.putExtra("image", finalImageUri); // Pass the image URI string
                    setResult(RESULT_OK, resIntent);
                    finish();
                }
            }
        });

        delete.setOnClickListener(view -> confirmDeleteDialog());

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
                            try {
                                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                                // Save the image and get the file path
                                image = saveImageToFile(bitmap);
                                Log.d("UpdateProduct", "New Image URI: " + image); // Log the new image path

                                // Load the image back from file path
                                Bitmap loadedBitmap = BitmapFactory.decodeFile(image);
                                if (loadedBitmap != null) {
                                    prodImage.setImageBitmap(loadedBitmap);
                                    Toast.makeText(UpdateProduct.this, "Image Selected Successfully", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(UpdateProduct.this, "Failed to load image from file", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(UpdateProduct.this, "Failed to load image", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(UpdateProduct.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    void confirmDeleteDialog(){
          AlertDialog.Builder build = new AlertDialog.Builder(this);
          build.setTitle("Delete Listing?");
          build.setMessage("Are you sure you want to delete" + description + "? this cannot be undone");
          build.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialogInterface, int i) {
                  MyDataBaseHelper db = new MyDataBaseHelper(UpdateProduct.this);
                  db.deleteData(id);
                  Intent intent = new Intent(UpdateProduct.this, MainActivity.class);
                  intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                  startActivity(intent);
                  finish();

              }
          });
          build.setNegativeButton("No", new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialogInterface, int i) {
                  //Toast.makeText(UpdateProduct.this, "\uD83D\uDE45\u200D♂\uFE0F No changes made", Toast.LENGTH_SHORT).show();
              }
          });
          AlertDialog alert = build.create();
          alert.show();
      }

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