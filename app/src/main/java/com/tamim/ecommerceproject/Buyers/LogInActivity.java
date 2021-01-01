package com.tamim.ecommerceproject.Buyers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.CheckBox;
import com.tamim.ecommerceproject.Admin.AdminHomeActivity;
import com.tamim.ecommerceproject.Sellers.SellerProductsCategoryActivity;
import com.tamim.ecommerceproject.Model.Users;
import com.tamim.ecommerceproject.Prevalent.Prevalent;
import com.tamim.ecommerceproject.R;

import io.paperdb.Paper;

public class LogInActivity extends AppCompatActivity {

    EditText inputNumber, inputPassword;
    Button loginButton;

    ProgressDialog loadingBar;
    TextView adminLink, noAdminLink, forgetPassword;
    CheckBox checkBoxRemembarme;
    private String parentDbName = "Users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        inputNumber = findViewById(R.id.login_phone_number_input);
        inputPassword = findViewById(R.id.login_password_input);
        loginButton = findViewById(R.id.login_btn);

        loadingBar = new ProgressDialog(this);

        forgetPassword = findViewById(R.id.forget_password_link);//TODO:Video 39 forget password come to Settings activity

        adminLink = findViewById(R.id.admin_panel_link);
        noAdminLink = findViewById(R.id.not_admin_panel_link);


        checkBoxRemembarme = findViewById(R.id.remember_me_chkb);
        Paper.init(this);

        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LogInActivity.this, ResetPasswordActivity.class);
                intent.putExtra("check","login");
                startActivity(intent);

            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogInUser();
            }
        });

        adminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.setText("Login Admin");
                adminLink.setVisibility(View.INVISIBLE);
                noAdminLink.setVisibility(View.VISIBLE);
                parentDbName = "Admins";
            }
        });
        noAdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.setText("Login");
                adminLink.setVisibility(View.VISIBLE);
                noAdminLink.setVisibility(View.INVISIBLE);
                parentDbName = "Users";
            }
        });
    }

    private void LogInUser() {
        String phone = inputNumber.getText().toString();
        String password = inputPassword.getText().toString();

        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "Please enter your Phone", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter your Password", Toast.LENGTH_SHORT).show();
        } else {
            loadingBar.setTitle("Login Account");
            loadingBar.setMessage("Please wait,while you are checking the credential");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            AllowAccessToAccount(phone, password);
        }

    }

    private void AllowAccessToAccount(final String phone, final String password) {

        if (checkBoxRemembarme.isChecked()) {
            Paper.book().write(Prevalent.UserPhoneKey, phone);
            Paper.book().write(Prevalent.UserPasswordKey, password);

            //TODO:Go to main activity to save phone and password data ;
        }

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.child(parentDbName).child(phone).exists()) {

                    Users usersData = snapshot.child(parentDbName).child(phone).getValue(Users.class);

                    if (usersData.getPhone().equals(phone)) {

                        if (usersData.getPassword().equals(password)) {

                            if (parentDbName.equals("Admins")) {
                                Toast.makeText(LogInActivity.this, "Wellcome admin you are Logged in successfully", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                Intent intent = new Intent(LogInActivity.this, AdminHomeActivity.class);
                                startActivity(intent);
                            } else if (parentDbName.equals("Users")) {
                                Toast.makeText(LogInActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                Intent intent = new Intent(LogInActivity.this, HomeActivity.class);
                                Prevalent.currentOnlineUser = usersData; //TODO:video 16 come from Home Activity to collect user data n go to Home
                                startActivity(intent);
                            }


                        }
                    }

                } else {
                    Toast.makeText(LogInActivity.this, "Account with this " + phone + " do not exists", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}