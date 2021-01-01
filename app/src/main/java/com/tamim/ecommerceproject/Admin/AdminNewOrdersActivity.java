package com.tamim.ecommerceproject.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tamim.ecommerceproject.Model.AdminOrders;
import com.tamim.ecommerceproject.R;
import com.tamim.ecommerceproject.ViewHolder.AdminOrderViewHolder;

public class AdminNewOrdersActivity extends AppCompatActivity {

    RecyclerView orderList;
    DatabaseReference orderRef;
    RecyclerView.LayoutManager layoutManager;

  //TODO:30 video
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_admin_new_orders);

        orderRef = FirebaseDatabase.getInstance().getReference().child("Orders");

        orderList = findViewById(R.id.order_list);

        orderList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        //TODO:30 video

        FirebaseRecyclerOptions<AdminOrders> options = new FirebaseRecyclerOptions.Builder<AdminOrders>()
                .setQuery(orderRef, AdminOrders.class)
                .build();

        FirebaseRecyclerAdapter<AdminOrders, AdminOrderViewHolder> adapter = new FirebaseRecyclerAdapter<AdminOrders, AdminOrderViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AdminOrderViewHolder holder, int position, @NonNull AdminOrders model) {

                holder.userName.setText("Name : "+model.getName());
                holder.userPhoneNumber.setText("Phone : "+model.getPhone());
                holder.userTotalPrice.setText("Total Amount : $"+model.getTotalAmount());
                holder.userShippingAddress.setText("Shipping Address : "+model.getAddress()+" , "+model.getCity());
                holder.userDateTime.setText("Oreder at : "+model.getDate()+" , "+model.getTime());

                holder.showOrdersBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String Uid=getRef(position).getKey();//TODO:This is pass your email or phone whatever you use it in login.

                        Intent intent=new Intent(AdminNewOrdersActivity.this,AdminUserProductsActivity.class);
                        intent.putExtra("uid",Uid);
                        startActivity(intent);
                        Toast.makeText(AdminNewOrdersActivity.this, Uid, Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @NonNull
            @Override
            public AdminOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(AdminNewOrdersActivity.this).inflate(R.layout.orders_item_layout, parent, false);
                return new AdminOrderViewHolder(view);
            }
        };
        orderList.setAdapter(adapter);
        adapter.startListening();

    }
}