package com.example.fiesta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    Toolbar toolbar;
    EditText loginid,pswd;
    Button loginbtn;
    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    ProgressBar loginProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth =FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();

        toolbar = findViewById(R.id.fiesta_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Login");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);


        loginid = findViewById(R.id.mailtxt);
        pswd = findViewById(R.id.passwordtxt);
        loginbtn = findViewById(R.id.loginbtn);
        loginProgressBar = findViewById(R.id.loginProgressBar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            }
        });


        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String loginusr = loginid.getText().toString().trim();
                String usrpswd = pswd.getText().toString().trim();
                loginProgressBar.setVisibility(View.VISIBLE);
                loginbtn.setVisibility(View.GONE);
                userLoginwithDatabase(loginusr,usrpswd);
            }
        });

        if (firebaseUser != null) {
            finish();
            startActivity(new Intent(LoginActivity.this, CollegeFragement.class));
        }


    }

    public void userLoginwithDatabase(String loginusr,String usrpswd){

        mAuth.signInWithEmailAndPassword(loginusr, usrpswd).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d("LOGIN: ", "loginWithEmailAndPassword SUCCESS");
//                    preferencesConfig.setLoginStatus(true);
                    loginProgressBar.setVisibility(View.GONE);
                    loginbtn.setVisibility(View.VISIBLE);
                    startActivity(new Intent(LoginActivity.this, CollegeFragement.class));
                    finish();

//                    Toast.makeText(LoginActivity.this,"Error",Toast.LENGTH_LONG).show();
                }
                else {
                    Log.d("LOGIN: ", "Error in loginWithEmailAndPassword", task.getException());
                    Toast.makeText(LoginActivity.this, "Error in login", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
