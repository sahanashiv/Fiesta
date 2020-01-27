package com.example.fiesta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.fiesta.fragment.AddFestFragment;
import com.example.fiesta.fragment.CollegeDisplayFragment;
import com.example.fiesta.fragment.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class CollegeFragement extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    Toolbar toolbar;
    BottomNavigationView bottomNavigationView;
//    Button profileLogoutButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_college_fragement);

//        profileLogoutButton =findViewById(R.id.ProfilelogoutBtn);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new CollegeDisplayFragment()).commit();
        }
        else {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new CollegeDisplayFragment()).commit();
        }

        bottomNavigationView = findViewById(R.id.bottom_nav_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavListener);

        toolbar = findViewById(R.id.fiesta_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Home");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CollegeFragement.this,HomeActivity.class));
            }
        });
        firebaseAuth = FirebaseAuth.getInstance();

    }




    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment selectedFragment = null;

            switch (menuItem.getItemId()) {

                case R.id.bottom_nav_home: {
                    selectedFragment = new CollegeDisplayFragment();
                    toolbar.setTitle("Home");
                    setSupportActionBar(toolbar);
                    toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black);
                    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(CollegeFragement.this,HomeActivity.class));
                        }
                    });
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, selectedFragment).commit();
                    break;
                }

                case R.id.bottom_nav_collection: {
                    if (FirebaseAuth.getInstance().getCurrentUser() != null){
                        selectedFragment = new AddFestFragment();
                        toolbar.setTitle("AddFest");
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, selectedFragment).commit();
                    }
                    else {
                        Intent loginpage = new Intent(CollegeFragement.this,HomeActivity.class);
                        startActivity(loginpage);
                    }
                    break;
                }

                case R.id.bottom_nav_account: {
                    if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                        selectedFragment = new ProfileFragment();
                        toolbar.setTitle("Profile");
                        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(CollegeFragement.this,CollegeFragement.class));
                            }
                        });
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, selectedFragment).commit();
                    }
                    else {
                        Intent loginpage = new Intent(CollegeFragement.this,HomeActivity.class);
                        startActivity(loginpage);
                    }

                    break;
                }
            }

            return true;
        }
    };

}
