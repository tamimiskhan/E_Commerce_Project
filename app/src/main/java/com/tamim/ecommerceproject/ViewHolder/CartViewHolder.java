package com.tamim.ecommerceproject.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tamim.ecommerceproject.Interface.ItemClickListener;
import com.tamim.ecommerceproject.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    //TODO: 24 video
    public TextView textProductName, textProductQuentity, textProductPrice;
    private ItemClickListener itemClickListener;

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);

        textProductName = itemView.findViewById(R.id.cart_product_name);
        textProductPrice = itemView.findViewById(R.id.cart_product_price);
        textProductQuentity = itemView.findViewById(R.id.cart_product_quantity);

    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
