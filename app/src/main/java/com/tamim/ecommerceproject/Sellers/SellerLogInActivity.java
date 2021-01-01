package com.tamim.ecommerceproject.Sellers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.tamim.ecommerceproject.R;

public class SellerLogInActivity extends AppCompatActivity {

    //TODO:Video 44

    Button loginSellerBtn;
    EditText emailInput, passwordInput;

    FirebaseAuth mAuth;
    ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_log_in);
        mAuth = FirebaseAuth.getInstance();
        loadingBar = new ProgressDialog(this);

        loginSellerBtn =findViewById(R.id.seller_login_btn);
        emailInput=findViewById(R.id.seller_login_email);
        passwordInput=findViewById(R.id.seller_login_password);

        loginSellerBtn.setOnClickListener(new View.OnClickListener() {// TODO:video 44
            @Override
            public void onClick(View v) {
                loginSeller();// TODO:video 44
            }
        });
    }

    private void loginSeller() {// TODO:video 44

        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();

        if ( !email.equals("") && !password.equals("") ){
            loadingBar.setTitle("Login Seller Account");
            loadingBar.setMessage("Please wait,while you are checking the credential");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    loadingBar.dismiss();
                    Toast.makeText(SellerLogInActivity.this, "You are register successfully", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(SellerLogInActivity.this, SellerHomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();



                }
            });



        }else {
            Toast.makeText(this, "Please compleate the login form", Toast.LENGTH_SHORT).show();

        }

    }
}