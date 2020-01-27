package com.example.fiesta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fiesta.models.Users;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomeActivity extends AppCompatActivity {

    FirebaseUser firebaseUser;
    FirebaseFirestore firestore;

    Button googlebtn,loginbtn;
    ProgressBar googleLoginProgressBar;
    TextView signupview;
    Toolbar toolbar;

    private final static int RC_SIGN_IN = 2;
    private FirebaseAuth mAuth;
    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firestore = FirebaseFirestore.getInstance();

        googlebtn = findViewById(R.id.googlebtn);
        loginbtn = findViewById(R.id.loginbtn);
        signupview = findViewById(R.id.signupview);
        googleLoginProgressBar = findViewById(R.id.googleLoginProgressBar);

        toolbar= findViewById(R.id.fiesta_toolbar);
        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,CollegeFragement.class));
            }
        });

        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        googlebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInWithGoogle();
                googleLoginProgressBar.setVisibility(googleLoginProgressBar.VISIBLE);
            }
        });


        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent loginpage = new Intent(HomeActivity.this,LoginActivity.class);
                startActivity(loginpage);

            }
        });

        signupview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent signuppage = new Intent(HomeActivity.this,SignupActivity.class);
                startActivity(signuppage);
            }
        });


        if (firebaseUser != null) {
            finish();
            startActivity(new Intent(HomeActivity.this, CollegeFragement.class));
        }

    }

    public void signInWithGoogle(){
        Intent signIntent = mGoogleSignInClient.getSignInIntent();

        startActivityForResult(signIntent, RC_SIGN_IN);
        googleLoginProgressBar.setVisibility(googleLoginProgressBar.INVISIBLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("TAG", "Google sign in failed", e);
                // ...
            }
        }
    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d("TAG", "firebaseAuthWithGoogle: " + account.getEmail() + " " + account.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("TAG", "SignIn SUCCESS");

                            FirebaseUser firebaseUser = mAuth.getCurrentUser();

                            final Users user = new Users();
                            user.setUsername(firebaseUser.getEmail().split("@")[0]);
                            user.setEmail(firebaseUser.getEmail());
                            user.setFullname(firebaseUser.getDisplayName());
                            user.setPassword("");

                            firestore.collection("users").document(task.getResult().getUser().getUid())
                                    .set(user)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            finish();
                                            startActivity(new Intent(HomeActivity.this, CollegeFragement.class));
                                            googleLoginProgressBar.setVisibility(googleLoginProgressBar.INVISIBLE);
                                        }
                                    });
                        }
                        else {
                            Log.d("TAG", "SignIn ERROR" + task.getException());
                            Toast.makeText(HomeActivity.this, "Signin ERRRORRRR", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
