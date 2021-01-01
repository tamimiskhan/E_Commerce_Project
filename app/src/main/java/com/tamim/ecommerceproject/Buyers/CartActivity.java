package com.tamim.ecommerceproject.Buyers;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tamim.ecommerceproject.Model.Cart;
import com.tamim.ecommerceproject.Prevalent.Prevalent;
import com.tamim.ecommerceproject.R;
import com.tamim.ecommerceproject.ViewHolder.CartViewHolder;

public class CartActivity extends AppCompatActivity {
    //TODO:VIDEO 23
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    Button nextProcessBtn;

    private TextView txtTotalPrice, textMsg;
    private int overTotalPrice = 0;  //TODO: 26 video

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = findViewById(R.id.cart_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        nextProcessBtn = findViewById(R.id.next_process_button);
        txtTotalPrice = findViewById(R.id.total_price);
        textMsg = findViewById(R.id.msg1);

        nextProcessBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                txtTotalPrice.setText("Total Price = $" + String.valueOf(overTotalPrice));

                Intent intent = new Intent(CartActivity.this, ConfirmFinalOrderActivity.class);
                intent.putExtra("Total Price", String.valueOf(overTotalPrice));
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {


        super.onStart(); //TODO: 24 video
       checkState();//TODO: 28 video

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference()
                .child("Cart List");

//TODO: 24 video create Cart model Class

        FirebaseRecyclerOptions<Cart> options = new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartListRef.child("User View")
                        .child(Prevalent.currentOnlineUser.getPhone()).child("Products"), Cart.class)
                .build();

//TODO: 24 video create CartViewHolder  Class


        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull Cart model) {

                holder.textProductName.setText(model.getPname());
                holder.textProductPrice.setText("Price : " + model.getPrice());
                holder.textProductQuentity.setText("Quantity :" + model.getQuantity());

                final int onTypeProductPrice = (Integer.valueOf(model.getPrice())) * (Integer.valueOf(model.getQuantity()));  //TODO: 26 video

                overTotalPrice = overTotalPrice + onTypeProductPrice; //TODO: 26 video

                //TODO: 24 video go to home activity and add FloatAction intent to come cartActivity.

                //TODO:video 25 start here

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence options[] = new CharSequence[]{

                                "Edit",
                                "Remove"
                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                        builder.setTitle("Cart Options");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if (which == 0) {
                                    Intent intent = new Intent(CartActivity.this, ProductDetailsActivity.class);
                                    intent.putExtra("pid", model.getPid());
                                    startActivity(intent);
                                }
                                if (which == 1) {


                                    cartListRef.child("User View").child(Prevalent.currentOnlineUser.getPhone())
                                            .child("Products").child(model.getPid())
                                            .removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(CartActivity.this, "Item remove successfully", Toast.LENGTH_SHORT).show();

                                                        Intent intent = new Intent(CartActivity.this, HomeActivity.class);

                                                        startActivity(intent);
                                                    }
                                                }
                                            });
                                }

                            }
                        });
                        builder.show();

                    }
                });


            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(CartActivity.this).inflate(R.layout.cart_item_layout, parent, false);
                return new CartViewHolder(view);
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();

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
                    String userName = snapshot.child("name").getValue().toString();

                    if (shippingState.equals("shipped")) {

                        txtTotalPrice.setText("Dear " + userName + "\norder is shipped successfully");
                        recyclerView.setVisibility(View.GONE);
                        textMsg.setVisibility(View.VISIBLE);
                        textMsg.setText("Congratulations,your final order has been shipped successfully,Soon you will reciverd your order at your door step.");
                        nextProcessBtn.setVisibility(View.GONE);
                        Toast.makeText(CartActivity.this, "You can purches more product,once you have recive your first final order", Toast.LENGTH_SHORT).show();

                    } else if (shippingState.equals("not shipped")) {
                        txtTotalPrice.setText("Shipping state == not shipped");
                        recyclerView.setVisibility(View.GONE);
                        textMsg.setVisibility(View.VISIBLE);
                        nextProcessBtn.setVisibility(View.GONE);
                        Toast.makeText(CartActivity.this, "You can purches more product,once you have recive your first final order", Toast.LENGTH_SHORT).show();



                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}