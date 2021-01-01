package com.tamim.ecommerceproject.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tamim.ecommerceproject.Interface.ItemClickListener;
import com.tamim.ecommerceproject.R;

public class SellerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    //TODO:48 video
    public TextView txtProductName, txtProductDescription, txtProductPrice,txtProductStatus;
    public ImageView imageView;
    public ItemClickListener itemClickListener;

    public SellerViewHolder(@NonNull View itemView) {
        super(itemView);

        imageView = (ImageView) itemView.findViewById(R.id.product_seller_image);
        txtProductName = (TextView) itemView.findViewById(R.id.product_seller_name);
        txtProductDescription = (TextView) itemView.findViewById(R.id.product_seller_description);
        txtProductPrice = (TextView) itemView.findViewById(R.id.product_seller_price);
        txtProductStatus = (TextView) itemView.findViewById(R.id.product_seller_status);
    }

    public void setItemClickListener(ItemClickListener listener) {
        this.itemClickListener = listener;

    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(), false);
    }
}
