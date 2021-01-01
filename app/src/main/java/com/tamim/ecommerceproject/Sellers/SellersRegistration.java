package com.tamim.ecommerceproject.Sellers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tamim.ecommerceproject.Buyers.MainActivity;
import com.tamim.ecommerceproject.R;

import java.util.HashMap;

public class SellersRegistration extends AppCompatActivity {

    Button sellerLoginButton, registerButton;
    EditText nameInput, emailInput, passwordInput, addressInput, phoneInput;

    FirebaseAuth mAuth;

    ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sellers_registration);

        mAuth = FirebaseAuth.getInstance();
        loadingBar = new ProgressDialog(this);

        registerButton = findViewById(R.id.seller_register);
        nameInput = findViewById(R.id.seller_name);
        emailInput = findViewById(R.id.seller_email);
        passwordInput = findViewById(R.id.seller_password);
        phoneInput = findViewById(R.id.seller_phone);
        addressInput = findViewById(R.id.seller_address);

        sellerLoginButton = findViewById(R.id.seller_already_have_account);

        sellerLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellersRegistration.this, SellerLogInActivity.class);
                startActivity(intent);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerSeller();// TODO:video 43
            }
        });

    }

    private void registerSeller() {// TODO:video 43
        String name = nameInput.getText().toString();
        String phone = phoneInput.getText().toString();
        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();
        String address = addressInput.getText().toString();

        if (!name.equals("") && !phone.equals("") && !email.equals("") && !password.equals("") && !address.equals("")) {

            loadingBar.setTitle("Creating Seller Account");
            loadingBar.setMessage("Please wait,while you are checking the credential");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();


            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {

                        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

                        String sId = mAuth.getCurrentUser().getUid();

                        HashMap<String, Object> sellerMap = new HashMap<>();

                        sellerMap.put("sid", sId);
                        sellerMap.put("name", name);
                        sellerMap.put("email", email);
                        sellerMap.put("password", password);
                        sellerMap.put("phone", phone);
                        sellerMap.put("address", address);

                        rootRef.child("Sellers").child(sId).updateChildren(sellerMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                loadingBar.dismiss();
                                Toast.makeText(SellersRegistration.this, "You are register successfully", Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(SellersRegistration.this, SellerHomeActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();

                            }
                        });


                    }

                }
            });


        } else {
            Toast.makeText(this, "Please compleate the registration form", Toast.LENGTH_SHORT).show();
        }

    }
}