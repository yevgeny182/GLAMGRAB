package com.example.shopphileappactual;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {

    private Context context;
    private ArrayList prodID, prodName, prodDesc, prodPrice, prodCategory;
    private ArrayList<String> prodImg;

    Activity activity;
    int position;

    ProductAdapter(Activity activity, Context context, ArrayList prodID, ArrayList prodName, ArrayList prodDesc, ArrayList prodPrice, ArrayList prodCategory, ArrayList<String> prodImg) {
        this.context = context;
        this.activity = activity;
        this.prodID = prodID;
        this.prodName = prodName;
        this.prodDesc = prodDesc;
        this.prodPrice = prodPrice;
        this.prodCategory = prodCategory;
        this.prodImg = prodImg; // Updated type

    }

    @NonNull
    @Override
    public ProductAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflate = LayoutInflater.from(context);
        View v = inflate.inflate(R.layout.product_layout, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        this.position = position;

        holder.name.setText(String.valueOf(prodName.get(position)));
        holder.desc.setText(String.valueOf(prodDesc.get(position)));
        holder.price.setText(String.valueOf(prodPrice.get(position)));

        // Load image from file path
        String imgPath = prodImg.get(position);
        if (imgPath != null && !imgPath.isEmpty()) {
            File imgFile = new File(imgPath);
            if (imgFile.exists()) {
                Bitmap bmp = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                holder.img.setImageBitmap(bmp);
            } else {
                holder.img.setImageResource(R.drawable.placeholder_image); // If the file doesn't exist, use a placeholder image
            }
        } else {
            holder.img.setImageResource(R.drawable.placeholder_image); // If no image path, use a placeholder
        }

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, ProductPreview.class);
                intent.putExtra("id", String.valueOf(prodID.get(position)));
                intent.putExtra("title", String.valueOf(prodName.get(position)));
                intent.putExtra("description", String.valueOf(prodDesc.get(position)));
                intent.putExtra("price", String.valueOf(prodPrice.get(position)));
                intent.putExtra("category", String.valueOf(prodCategory.get(position)));
                intent.putExtra("image", prodImg.get(position)); // Pass the image file path as a string
                activity.startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return prodID.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView name, desc, price, category;
        ConstraintLayout mainLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.prodImageView);
            name = itemView.findViewById(R.id.productName);
            desc = itemView.findViewById(R.id.productDescription);
            price = itemView.findViewById(R.id.productPrice);

            mainLayout = itemView.findViewById(R.id.prodLayoutMain);
        }
    }
}
