package com.example.fiesta.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fiesta.R;
import com.example.fiesta.adapter.CollegeListAdapter;
import com.example.fiesta.models.College;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CollegeDisplayFragment extends Fragment {

    FirebaseFirestore firestore;
    CollegeListAdapter collegeListAdapter;
    RecyclerView collegeRecyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_college_display,container,false);

        collegeRecyclerView = view.findViewById(R.id.collegeRecyclerView);
        collegeRecyclerView.setHasFixedSize(true);
        collegeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        firestore.collection("festdata")
//                .whereEqualTo("userId", firebaseUser.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        List<College> collegeList = new ArrayList<>();

                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            College clg = new College();

                            clg._setCollegeId(doc.getId());

                            clg.setCollegeContact(doc.getString("collegeContact"));
                            clg.setCollegeItemName(doc.getString("collegeItemName"));
                            clg.setCollegeListFestName(doc.getString("collegeListFestName"));
                            clg.setCollegeListStartDate(doc.getString("collegeListStartDate"));
                            clg.setCollegeListEndDate(doc.getString("collegeListEndDate"));
                            clg.setcollegeImage(doc.getString("collegeImage"));
                            clg.setCollegeListFestDetails(doc.getString("collegeListFestDetails"));

                            collegeList.add(clg);

                        }

                        collegeListAdapter = new CollegeListAdapter(getContext(),collegeList);
                        collegeRecyclerView.setAdapter(collegeListAdapter);
                    }
                });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem searchItem = menu.findItem(R.id.searchBarMenu);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                collegeListAdapter.getFilter().filter(s);
                return false;
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firestore = FirebaseFirestore.getInstance();
        setHasOptionsMenu(true);  //for setting search icon
    }
}
