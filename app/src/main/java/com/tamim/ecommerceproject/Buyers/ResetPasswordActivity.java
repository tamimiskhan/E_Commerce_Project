package com.tamim.ecommerceproject.Buyers;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tamim.ecommerceproject.Prevalent.Prevalent;
import com.tamim.ecommerceproject.R;

import java.util.HashMap;

public class ResetPasswordActivity extends AppCompatActivity {//TODO: Create activity in 39 video

    //TODO:Find variable in 40 video

    TextView pageTitle, titleQuestion;
    Button verifyButton;
    EditText phoneNumber, qustion1, question2;
    private String check = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        check = getIntent().getStringExtra("check");

        pageTitle = findViewById(R.id.page_title);
        titleQuestion = findViewById(R.id.title_question);
        verifyButton = findViewById(R.id.verify_btn);
        phoneNumber = findViewById(R.id.find_phone_number);
        qustion1 = findViewById(R.id.question_1);
        question2 = findViewById(R.id.question_2);

    }

    @Override
    protected void onStart() {
        super.onStart();

        phoneNumber.setVisibility(View.GONE);

        if (check.equals("settings")) {

            pageTitle.setText("Set question");

            verifyButton.setText("Set");
            titleQuestion.setText("Please set answer for the following security question.");

            displayPreviousAnswer();//TODO:Place here to see user previous question here

            verifyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    setAnswer();//TODO:video 40 video


                }
            });


        }
        if (check.equals("login")) {

            phoneNumber.setVisibility(View.VISIBLE);

            verifyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    verifyUser();//TODO:Video 41 check user phone,and question to create a new password when you forget password
                }
            });
        }
    }

    private void verifyUser() {
        //TODO:Video 41
        String phone = phoneNumber.getText().toString();

        String answer1 = qustion1.getText().toString().toLowerCase();
        String answer2 = question2.getText().toString().toLowerCase();

        if (!phone.equals("") && !answer1.equals("") && !answer2.equals("")) {
            final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users")
                    .child(phone);

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (snapshot.exists()) {
                        String mPhone = snapshot.child("phone").getValue().toString();

                        if (snapshot.hasChild("Security Question")) {

                            String ans1 = snapshot.child("Security Question").child("answer1").getValue().toString();
                            String ans2 = snapshot.child("Security Question").child("answer2").getValue().toString();

                            if (!ans1.equals(answer1)) {

                                Toast.makeText(ResetPasswordActivity.this, "Youe 1st answer is wrong", Toast.LENGTH_SHORT).show();

                            } else if (!ans2.equals(answer2)) {
                                Toast.makeText(ResetPasswordActivity.this, "Youe 2nd answer is wrong", Toast.LENGTH_SHORT).show();
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(ResetPasswordActivity.this);
                                builder.setTitle("New password");

                                final EditText newPassword = new EditText(ResetPasswordActivity.this);
                                newPassword.setHint("Write password here...");

                                builder.setView(newPassword);

                                builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        if (!newPassword.getText().toString().equals("")) {
                                            ref.child("password")
                                                    .setValue(newPassword.getText().toString())
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {

                                                            if (task.isSuccessful()) {

                                                                Toast.makeText(ResetPasswordActivity.this, "Password change successfully", Toast.LENGTH_SHORT).show();

                                                                Intent intent = new Intent(ResetPasswordActivity.this, MainActivity.class);
                                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                startActivity(intent);
                                                                finish();
                                                            }

                                                        }
                                                    });

                                        }

                                    }
                                });

                                builder.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        dialog.cancel();

                                    }
                                });

                                builder.show();

                            }


                        } else {
                            Toast.makeText(ResetPasswordActivity.this, "You have not set the security question", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(ResetPasswordActivity.this, "This phone number not exist", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        } else {
            Toast.makeText(this, "Please complete the form", Toast.LENGTH_SHORT).show();
        }


    }

    private void setAnswer() {

        String answer1 = qustion1.getText().toString().toLowerCase();
        String answer2 = question2.getText().toString().toLowerCase();

        if (answer1.equals("") && answer2.equals("")) {

            Toast.makeText(ResetPasswordActivity.this, "Please answer both the question.", Toast.LENGTH_SHORT).show();
        } else {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users")
                    .child(Prevalent.currentOnlineUser.getPhone());

            HashMap<String, Object> userDataMap = new HashMap<>();

            userDataMap.put("answer1", answer1);
            userDataMap.put("answer2", answer2);

            ref.child("Security Question").updateChildren(userDataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()) {
                        Toast.makeText(ResetPasswordActivity.this, "You have set the question successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ResetPasswordActivity.this, HomeActivity.class);
                        startActivity(intent);

                    }

                }
            });

        }
    }

    private void displayPreviousAnswer() {//TODO:video 40 video
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(Prevalent.currentOnlineUser.getPhone());

        ref.child("Security Question").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    String ans1 = snapshot.child("answer1").getValue().toString();
                    String ans2 = snapshot.child("answer2").getValue().toString();

                    qustion1.setText(ans1);
                    question2.setText(ans2);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}