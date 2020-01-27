package com.example.fiesta.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fiesta.CollegeFragement;
import com.example.fiesta.R;
import com.example.fiesta.adapter.ProfileListAdapter;
import com.example.fiesta.models.College;
import com.example.fiesta.models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    FirebaseFirestore firestore;
    ProfileListAdapter profileListAdapter;
    RecyclerView userFestRecyclerView;
    TextView profileName;
    ImageView profileImage;
    Button profileLogoutButton;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_profile,container,false);

        profileLogoutButton = view.findViewById(R.id.ProfilelogoutBtn);
        userFestRecyclerView = view.findViewById(R.id.userFestRecyclerView);
        userFestRecyclerView.setHasFixedSize(true);
        userFestRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        profileName = view.findViewById(R.id.profilename);
        profileImage = view.findViewById(R.id.profileImage);
        firestore.collection("users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            Users users = task.getResult().toObject(Users.class);
                            profileName.setText(users.getFullname());
                        }
                    }
                });



        firestore.collection("festdata")
              .whereEqualTo("userId", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        List<College> profileList = new ArrayList<>();

                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            College college = new College();

                            college.setUserId(doc.getString(Users.USER_ID));
                            college.setCollegeItemName(doc.getString("collegeItemName"));
                            college.setCollegeListFestName(doc.getString("collegeListFestName"));
                            college.setCollegeContact(doc.getString(doc.getString("collegeContact")));
                            college.setcollegeImage(doc.getString("collegeImage"));
                            college.setCollegeListEndDate(doc.getString("collegeListEndDate"));
                            college.setCollegeListFestDetails(doc.getString("collegeListFestDetails"));
                            college.setCollegeListStartDate(doc.getString("collegeListStartDate"));
                            college._setCollegeId(doc.getId());

                            profileList.add(college);
                        }

                        profileListAdapter = new ProfileListAdapter(getContext(), profileList);
                        userFestRecyclerView.setAdapter(profileListAdapter);
                    }
                });

        profileLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                
                startActivity(new Intent(getContext(), CollegeFragement.class));
            }
        });

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firestore = FirebaseFirestore.getInstance();
    }
}
