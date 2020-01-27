package com.example.fiesta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fiesta.models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class SignupActivity extends AppCompatActivity {

    Toolbar toolbar;

    EditText emailidtxt,pswdtxt,fullnametxt,usernametxt;
    Button createbtn;

    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        toolbar = findViewById(R.id.fiesta_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Login");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, HomeActivity.class));
            }
        });

        usernametxt = findViewById(R.id.usernametxt);
        emailidtxt = findViewById(R.id.emailidtxt);
        pswdtxt = findViewById(R.id.pswdtxt);
        fullnametxt = findViewById(R.id.fullnametxt);
        createbtn = findViewById(R.id.createbtn);

        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        firestore = FirebaseFirestore.getInstance();

        createbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String usrname = usernametxt.getText().toString().trim();
                String emailtxtview = emailidtxt.getText().toString().toLowerCase().trim();
                String pswdtxtview = pswdtxt.getText().toString().trim();
                String fullnameview = fullnametxt.getText().toString().trim();

                LogInWithEmail(usrname,emailtxtview,pswdtxtview,fullnameview);

            }
        });


        if (firebaseUser != null) {
            finish();
            startActivity(new Intent(SignupActivity.this, CollegeFragement.class));
        }

    }

    public void LogInWithEmail(String usrname,String emailtxtview,String pswdtxtview,String fullnameview){


        final Users user = new Users();
        user.setUsername(usrname);
        user.setEmail(emailtxtview);
        user.setFullname(fullnameview);
        user.setPassword(pswdtxtview);

        mAuth.createUserWithEmailAndPassword(emailtxtview,pswdtxtview)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("Error Mauth", "Success");
                            Toast.makeText(SignupActivity.this, "save successfully", Toast.LENGTH_LONG).show();

                            firestore.collection("users").document(task.getResult().getUser().getUid())
                                    .set(user)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Intent loginintent = new Intent(SignupActivity.this,LoginActivity.class);
                                            startActivity(loginintent);
                                        }
                                    });

                        } else {
                            Log.d("Error Mauth", "Failed", task.getException());
                            Toast.makeText(SignupActivity.this, "Enter Correct details", Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }
}
