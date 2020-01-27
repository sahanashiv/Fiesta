package com.example.fiesta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fiesta.models.Event;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


public class AddEvent extends AppCompatActivity {

    EditText regEventName,regEventContact,regDescription,regEventCoordinator;
    Button addEvent;

    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        firestore = FirebaseFirestore.getInstance();

        regEventName = findViewById(R.id.regEventName);
        regEventContact = findViewById(R.id.regEventContact);
        regDescription = findViewById(R.id.regDescription);
        addEvent = findViewById(R.id.addEvent);
        regEventCoordinator = findViewById(R.id.regEventCoordinator);

        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String eventName = regEventName.getText().toString().trim();
                String eventContact = regEventContact.getText().toString().trim();
                String eventDescription = regDescription.getText().toString().trim();
                String eventCoordinator = regEventCoordinator.getText().toString().trim();

                final Event addEvent = new Event();

                addEvent.setEventName(eventName);
                addEvent.setEventContact(eventContact);
                addEvent.setEventDescription(eventDescription);
                addEvent.setEventCoordinator(eventCoordinator);
                addEvent.setFestId(getIntent().getStringExtra("festId"));

                firestore.collection("eventdata")
                        .add(addEvent)
                        .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                Toast.makeText(AddEvent.this,"Data Saved",Toast.LENGTH_LONG).show();

                                AlertDialog.Builder builder = new AlertDialog.Builder(AddEvent.this);
                                builder.setMessage("Add more Event?")
                                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                regEventName.setText("");
                                                regEventContact.setText("");
                                                regEventCoordinator.setText("");
                                                regDescription.setText("");
                                                dialog.cancel();
                                            }
                                        })
                                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                startActivity(new Intent(AddEvent.this,CollegeFragement.class));
                                            }
                                        });

                                AlertDialog alert = builder.create();
                                alert.show();
                            }
                        });



            }
        });

    }

//    public void registerEventtoFirestore(String eventName, String eventContact,String eventDate,String eventDescription){
//
//        final Event addEvent = new Event();
//
//        addEvent.setEventName(eventName);
//        addEvent.setEventContact(eventContact);
//        addEvent.setEventDate(eventDate);
//        addEvent.setEventDescription(eventDescription);
//
//        firestore.collection("eventdata")
//                .add(addEvent)
//                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentReference> task) {
//                        Toast.makeText(AddEvent.this,"Data Saved",Toast.LENGTH_LONG).show();
//                    }
//                });
//
//    }
}
