package com.tamim.ecommerceproject.ViewHolder;

import android.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tamim.ecommerceproject.R;

public class AdminOrderViewHolder extends RecyclerView.ViewHolder  {
    //TODO:30 video

    public TextView userName,userPhoneNumber,userTotalPrice,userDateTime,userShippingAddress;

    public Button showOrdersBtn;

    public AdminOrderViewHolder(@NonNull View itemView) {
        super(itemView);

        userName=itemView.findViewById(R.id.order_user_name);
        userPhoneNumber=itemView.findViewById(R.id.order_phone_number);
        userTotalPrice=itemView.findViewById(R.id.order_total_price);
        userDateTime=itemView.findViewById(R.id.order_date_time);
        userShippingAddress=itemView.findViewById(R.id.order_address_city);

        showOrdersBtn=itemView.findViewById(R.id.show_all_products_btn);
    }


}
