package com.example.fiesta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.fiesta.adapter.CollegeListAdapter;
import com.example.fiesta.adapter.EventListAdapter;
import com.example.fiesta.models.College;
import com.example.fiesta.models.Event;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class EventDisplayActivity extends AppCompatActivity {

    RecyclerView eventRecyclerView;
    FirebaseFirestore firestore;
    EventListAdapter eventListAdapter;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_display);

        toolbar = findViewById(R.id.fiesta_toolbar);
        toolbar.setTitle("Event Details");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EventDisplayActivity.this,CollegeFragement.class));
            }
        });

        firestore = FirebaseFirestore.getInstance();
        eventRecyclerView = findViewById(R.id.eventRecyclerView);
        eventRecyclerView.setHasFixedSize(true);
        eventRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        Log.d("EVENT_DIS", getIntent().getStringExtra("festid") + " userid");

        firestore.collection("eventdata")
                .whereEqualTo("festId", getIntent().getStringExtra("festid"))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.getResult() != null) {
                            List<Event> collegeList = task.getResult().toObjects(Event.class);

                            Log.d("EVENT_DIS", collegeList.toString());

                            if (collegeList.size() > 0) {
                                eventListAdapter = new EventListAdapter(EventDisplayActivity.this,collegeList);
                                eventRecyclerView.setAdapter(eventListAdapter);
                            } else {
                                Toast.makeText(EventDisplayActivity.this, "No Event to be Displayed", Toast.LENGTH_SHORT).show();
                            }


                        }
                    }
                });

    }
}
