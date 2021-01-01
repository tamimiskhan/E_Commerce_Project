package com.tamim.ecommerceproject.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.tamim.ecommerceproject.R;
import com.tamim.ecommerceproject.Sellers.SellerProductsCategoryActivity;

import java.util.HashMap;

public class AdminMaintainProductsActivity extends AppCompatActivity {

    //TODO:Video 35
    Button applyChangesBtn,deleteProducts;
    EditText name, price, description;
    ImageView image;

    String productId = ""; //TODO:Video 36
    DatabaseReference productsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_maintain_products);

        productId = getIntent().getStringExtra("pid"); //TODO:Video 36

        productsRef = FirebaseDatabase.getInstance().getReference().child("Products").child(productId);


        name = findViewById(R.id.product_name_maintain);
        price = findViewById(R.id.product_price_maintain);
        description = findViewById(R.id.product_description_maintain);
        image = findViewById(R.id.product_image_maintain);

        deleteProducts=findViewById(R.id.delete_products_btn); //TODO:Video 38

        deleteProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//TODO:Video 38

                DeleteThisProducts();
            }
        });


        applyChangesBtn = findViewById(R.id.apply_changes_btn);

        applyChangesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO:Video 36

                ApplyChanges();
            }
        });

        DisplaySpecificProductsInfo(); //TODO:Video 36


    }

    private void DeleteThisProducts() {
        productsRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {//TODO:Video 38


                Toast.makeText(AdminMaintainProductsActivity.this, "This products is deleted successfully", Toast.LENGTH_SHORT).show();
            }
        });



    }

    private void ApplyChanges() {

        String pName = name.getText().toString();
        String pPrice = price.getText().toString();
        String pDescription = description.getText().toString();

        if (pName.equals("")) {
            Toast.makeText(this, "Write down products Name", Toast.LENGTH_SHORT).show();
        } else if (pPrice.equals("")) {
            Toast.makeText(this, "Write down products Price", Toast.LENGTH_SHORT).show();

        } else if (pDescription.equals("")) {
            Toast.makeText(this, "Write down products Description", Toast.LENGTH_SHORT).show();

        } else {
            HashMap<String, Object> productMap = new HashMap<>();
            productMap.put("pid", productId);
            productMap.put("description", pDescription);
            productMap.put("price", pPrice);
            productMap.put("pname", pName);

            productsRef.updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){

                        Intent intent=new Intent(AdminMaintainProductsActivity.this, SellerProductsCategoryActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        Toast.makeText(AdminMaintainProductsActivity.this, "Changes apply successfully", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }


    }

    private void DisplaySpecificProductsInfo() {
        //TODO:Video 36
        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    String pName = snapshot.child("pname").getValue().toString();
                    String pPrice = snapshot.child("price").getValue().toString();
                    String pDescription = snapshot.child("description").getValue().toString();
                    String pImage = snapshot.child("image").getValue().toString();


                    name.setText(pName);
                    price.setText(pPrice);
                    description.setText(pDescription);

                    Picasso.get().load(pImage).into(image);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}