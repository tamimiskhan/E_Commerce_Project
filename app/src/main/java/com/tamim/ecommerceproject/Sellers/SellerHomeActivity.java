package com.tamim.ecommerceproject.Sellers;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.tamim.ecommerceproject.Buyers.MainActivity;
import com.tamim.ecommerceproject.Model.Products;
import com.tamim.ecommerceproject.R;
import com.tamim.ecommerceproject.ViewHolder.SellerViewHolder;

public class SellerHomeActivity extends AppCompatActivity {

    RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;
    private DatabaseReference unverifiedproductsRef;


    //TODO:Video 44

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_home);

        unverifiedproductsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        recyclerView = findViewById(R.id.seller_home_recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        BottomNavigationView bottomNavigationView = findViewById(R.id.buttom_nevigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:

                        Intent intentHome = new Intent(SellerHomeActivity.this, SellerHomeActivity.class);
                        startActivity(intentHome);

                        return true;
                    case R.id.navigation_add:

                        Intent intentCategory = new Intent(SellerHomeActivity.this, SellerProductsCategoryActivity.class);
                        startActivity(intentCategory);

                        return true;
                    case R.id.navigation_logout:

                        FirebaseAuth.getInstance().signOut();

                        Intent intentMain = new Intent(SellerHomeActivity.this, MainActivity.class);
                        intentMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intentMain);
                        finish();


                }
                return false;
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(unverifiedproductsRef.orderByChild("sid").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()), Products.class)
                .build();

        FirebaseRecyclerAdapter<Products, SellerViewHolder> adapter = new FirebaseRecyclerAdapter<Products, SellerViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull SellerViewHolder holder, int position, @NonNull Products model) {

                holder.txtProductName.setText(model.getPname());
                holder.txtProductDescription.setText(model.getDescription());
                holder.txtProductPrice.setText("Price :$ "+model.getPrice());
                holder.txtProductStatus.setText("Status "+model.getProductState());

                Picasso.get().load(model.getImage()).into(holder.imageView);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final String productsId = model.getPid();

                        CharSequence options[] = new CharSequence[]{
                                "Yes",
                                "No"
                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(SellerHomeActivity.this);
                        builder.setTitle("Do you want to Delete this products");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if (position == 0) {

                                    deleteProducts(productsId);
                                }
                                if (position == 1) {

                                }

                            }
                        });
                        builder.show();

                    }
                });


            }

            @NonNull
            @Override
            public SellerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.seller_item_view, parent, false);
                SellerViewHolder holder = new SellerViewHolder(view); //TODO:48 video
                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();


    }

    private void deleteProducts(String productsId) {

        unverifiedproductsRef.child(productsId)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        Toast.makeText(SellerHomeActivity.this, "This item has been deleted successfully.", Toast.LENGTH_SHORT).show();
                    }
                });

    }
}