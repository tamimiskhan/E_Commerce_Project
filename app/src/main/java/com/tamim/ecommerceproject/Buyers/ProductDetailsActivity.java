package com.tamim.ecommerceproject.Buyers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.tamim.ecommerceproject.Model.Products;
import com.tamim.ecommerceproject.Prevalent.Prevalent;
import com.tamim.ecommerceproject.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetailsActivity extends AppCompatActivity {

    //TODO:Video 20

    Button addToChartBtn;
    ImageView productImage;
    TextView productName, productPrice, productDiscription;
    String productId = "", state = "Normal";

    ElegantNumberButton numberButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        productId = getIntent().getStringExtra("pid");

        addToChartBtn = findViewById(R.id.add_product_to_chart_btn);

        productName = findViewById(R.id.products_name_details);
        productPrice = findViewById(R.id.products_price_details);
        productDiscription = findViewById(R.id.products_description_details);

        numberButton = findViewById(R.id.number_btn);

        productImage = findViewById(R.id.products_image_details);


        getProductDetails(productId);
        addToChartBtn.setOnClickListener(new View.OnClickListener() {//TODO:Video 22
            @Override
            public void onClick(View v) {

                  //TODO:Video 28

                if (state.equals("Order Placed") || state.equals("Order Shipped")) {

                    Toast.makeText(ProductDetailsActivity.this,
                            "You can add purches more products, once your order is shipped or confirmed.", Toast.LENGTH_LONG).show();

                } else {
                    addingToCartList();
                }
            }
        });
    }

    private void addingToCartList() {

        //TODO:Video 22

        String saveCurrentDate, saveCurrentTime;
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());
        String productRandomKey = saveCurrentDate + "," + saveCurrentTime;

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");

        HashMap<String, Object> cartMap = new HashMap<>();

        cartMap.put("pid", productId);
        cartMap.put("pname", productName.getText().toString());
        cartMap.put("price", productPrice.getText().toString());
        cartMap.put("date", saveCurrentDate);
        cartMap.put("time", saveCurrentTime);
        cartMap.put("quantity", numberButton.getNumber());
        cartMap.put("discount", "");

        cartListRef.child("User View").child(Prevalent.currentOnlineUser.getPhone())
                .child("Products")
                .child(productId)
                .updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    cartListRef.child("Admin View").child(Prevalent.currentOnlineUser.getPhone())
                            .child("Products")
                            .child(productId)
                            .updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ProductDetailsActivity.this, "Added to Cart List", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(ProductDetailsActivity.this, HomeActivity.class);
                                startActivity(intent);
                            }
                        }
                    });
                }

            }
        });


    }

    private void getProductDetails(String productId) {

        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference().child("Products");

        productRef.child(productId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    //TODO:Video 21
                    Products products = snapshot.getValue(Products.class);

                    productName.setText(products.getPname());
                    productPrice.setText(products.getPrice());
                    productDiscription.setText(products.getDescription());

                    Picasso.get().load(products.getImage()).into(productImage);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        checkState();
    }

    public void checkState() {
        //TODO: VIDEO 28
        DatabaseReference orderRef = FirebaseDatabase.getInstance()
                .getReference().child("Orders").child(Prevalent.currentOnlineUser.getPhone());

        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {

                    String shippingState = snapshot.child("state").getValue().toString();


                    if (shippingState.equals("shipped")) {

                        state = "Order Shipped";
                    } else if (shippingState.equals("not shipped")) {

                        state = "Order Placed";

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}