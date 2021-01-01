package com.tamim.ecommerceproject.Admin;

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
import com.tamim.ecommerceproject.Model.Cart;
import com.tamim.ecommerceproject.R;
import com.tamim.ecommerceproject.ViewHolder.CartViewHolder;

public class AdminUserProductsActivity extends AppCompatActivity {

    private RecyclerView productsList;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference cartListRef;

    private String userId="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_products);

        userId = getIntent().getStringExtra("uid");

        cartListRef = FirebaseDatabase.getInstance().getReference()
                .child("Cart List")
                .child("Admin View")
                .child(userId)
                .child("Products");



        productsList = findViewById(R.id.products_list);
        productsList.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        productsList.setLayoutManager(layoutManager);



    }

    @Override
    protected void onStart() {
        super.onStart();

        //TODO: Cart model reference same as our products item,so no need to create extra model .
        FirebaseRecyclerOptions<Cart> options = new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartListRef, Cart.class)
                .build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter=new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull Cart model) {


                holder.textProductName.setText(model.getPname());
                holder.textProductPrice.setText("Price : " + model.getPrice());
                holder.textProductQuentity.setText("Quantity :" + model.getQuantity());

            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout, parent, false);
                return new CartViewHolder(view);
            }
        };
        productsList.setAdapter(adapter);
        adapter.startListening();

    }
}