package com.tamim.ecommerceproject.Buyers;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;
import com.tamim.ecommerceproject.Model.Products;
import com.tamim.ecommerceproject.R;
import com.tamim.ecommerceproject.ViewHolder.ProductViewHolder;

public class SearchProductsActivity extends AppCompatActivity {

    RecyclerView search_list;

    RecyclerView.LayoutManager layoutManager;
    Button search_btn;
    EditText inputText;

    String serachInput;
    DatabaseReference productRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_products);



        inputText = findViewById(R.id.search_product_name);
        search_btn = findViewById(R.id.search_btn);

        search_list = findViewById(R.id.search_list);
        search_list.setLayoutManager(new LinearLayoutManager(SearchProductsActivity.this));

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                serachInput = inputText.getText().toString();
                onStart();

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        productRef  = FirebaseDatabase.getInstance().getReference().child("Products");

        Query query = productRef.orderByChild("pname").startAt(serachInput).endAt(serachInput+ "\uf8ff").limitToLast(50);


        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(query, Products.class).build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter = new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull Products model) {

                holder.txtProductName.setText(model.getPname());
                holder.txtProductDescription.setText(model.getDescription());
                holder.txtProductPrice.setText(model.getPrice());

                Picasso.get().load(model.getImage()).into(holder.imageView);
                holder.imageView.setOnClickListener(new View.OnClickListener() {//TODO: vidoe 34 collect data from Home Activity 21 class copy
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(SearchProductsActivity.this, ProductDetailsActivity.class);
                        intent.putExtra("pid", model.getPid());
                        startActivity(intent);

                    }
                });


            }

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_layout, parent, false);
                ProductViewHolder holder = new ProductViewHolder(view);
                return holder;
            }
        };
        search_list.setAdapter(adapter);
        adapter.startListening();
        Toast.makeText(this, "Find", Toast.LENGTH_SHORT).show();
    }
}