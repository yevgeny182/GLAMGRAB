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

public class LikesProductAdapter extends RecyclerView.Adapter<LikesProductAdapter.MyViewHolderLikes> {

    private Context context;
    private ArrayList<Integer> prodID; // Specify types for better safety
    private ArrayList<String> prodName, prodDesc, prodPrice, prodCategory;
    private ArrayList<String> prodImg;

    private Activity activity;
    ImageView addToCart;

    // Constructor
    public LikesProductAdapter(Activity activity, Context context, ArrayList<Integer> prodID,
                               ArrayList<String> prodName, ArrayList<String> prodDesc,
                               ArrayList<String> prodPrice, ArrayList<String> prodCategory,
                               ArrayList<String> prodImg) {
        this.context = context;
        this.activity = activity;
        this.prodID = prodID;
        this.prodName = prodName;
        this.prodDesc = prodDesc;
        this.prodPrice = prodPrice;
        this.prodCategory = prodCategory;
        this.prodImg = prodImg; // Ensure this is the correct type
    }

    @NonNull
    @Override
    public MyViewHolderLikes onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflate = LayoutInflater.from(context);
        View v = inflate.inflate(R.layout.likes_product_layout, parent, false);
        return new MyViewHolderLikes(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderLikes holder, @SuppressLint("RecyclerView") final int position) {
        holder.name.setText(prodName.get(position));
        holder.desc.setText(prodDesc.get(position));
        holder.price.setText(prodPrice.get(position));

        // Load image from file path
        String imgPath = prodImg.get(position);
        if (imgPath != null && !imgPath.isEmpty()) {
            File imgFile = new File(imgPath);
            if (imgFile.exists()) {
                Bitmap bmp = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                holder.img.setImageBitmap(bmp);
            } else {
                holder.img.setImageResource(R.drawable.placeholder_image); // Use a placeholder image if the file doesn't exist
            }
        } else {
            holder.img.setImageResource(R.drawable.placeholder_image); // Use a placeholder if no image path
        }

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, ProductPreview.class);
                intent.putExtra("id", String.valueOf(prodID.get(position)));
                intent.putExtra("title", prodName.get(position));
                intent.putExtra("description", prodDesc.get(position));
                intent.putExtra("price", prodPrice.get(position));
                intent.putExtra("category", prodCategory.get(position));
                intent.putExtra("image", prodImg.get(position)); // Pass the image file path as a string
                activity.startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return prodID.size();
    }

    public static class MyViewHolderLikes extends RecyclerView.ViewHolder {
        ImageView img;
        TextView name, desc, price;
        ConstraintLayout mainLayout;

        public MyViewHolderLikes(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.prodImageViewLikes);
            name = itemView.findViewById(R.id.productNameLikes);
            desc = itemView.findViewById(R.id.productDescriptionLikes);
            price = itemView.findViewById(R.id.productPriceLikes);
            mainLayout = itemView.findViewById(R.id.prodLayoutLikes);
        }
    }
}
