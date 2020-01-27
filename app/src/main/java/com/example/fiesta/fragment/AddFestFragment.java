package com.example.fiesta.fragment;

import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.fiesta.AddEvent;
import com.example.fiesta.CollegeFragement;
import com.example.fiesta.R;
import com.example.fiesta.models.College;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

import static android.app.Activity.RESULT_OK;

public class AddFestFragment extends Fragment {

    FirebaseFirestore firestore;
    FirebaseUser firebaseUser;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    Toolbar toolbar;

    Calendar c;
    DatePickerDialog datePickerDialog;
    TextView startDateDisplay,endDateDisplay;

    Uri imageuri;
    ImageView imagePreview;

    private static final int PICK_IMAGE_REQUEST = 1;

    ImageButton openImagebtn, endcalendarBtn, calendarBtn;
    Button registerbtn;
    ProgressBar progressBarResult;
    EditText collegename, festname, enddate, festddetails, festContact;

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST &&
                resultCode == RESULT_OK &&
                data != null &&
                data.getData() != null) {
            imageuri = data.getData();

            openImagebtn.setVisibility(openImagebtn.GONE);
            imagePreview.setVisibility(imagePreview.VISIBLE);

            Picasso.get().load(imageuri).into(imagePreview);
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_registration,container,false);

        firestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference("imagesUpload");

        imagePreview = view.findViewById(R.id.collegeImageView);
        openImagebtn = view.findViewById(R.id.imageButton);
        registerbtn = view.findViewById(R.id.registerbtn);
        collegename = view.findViewById(R.id.collegename);
        festname = view.findViewById(R.id.festname);
        festContact = view.findViewById(R.id.festContact);
        festddetails = view.findViewById(R.id.festddetails);
        startDateDisplay = view.findViewById(R.id.startDisplayDate);
        endDateDisplay = view.findViewById(R.id.endDateDisplay);
        endcalendarBtn = view.findViewById(R.id.endcalendarBtn);

        calendarBtn = view.findViewById(R.id.calendarBtn);

//        enddate = view.findViewById(R.id.calendarBtn);
        progressBarResult = view.findViewById(R.id.progressBarResult);
//        startDateCalendarView =view.findViewById(R.id.startDateCalendarView);


        calendarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c = Calendar.getInstance();
                int day = c.get(Calendar.DAY_OF_MONTH);
                int month = c.get(Calendar.MONTH);
                int year = c.get(Calendar.YEAR);

                datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        startDateDisplay.setText(dayOfMonth+"/"+(month+1)+"/"+year);
                    }
                },day,month,year);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });

        endcalendarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c = Calendar.getInstance();
                int day = c.get(Calendar.DAY_OF_MONTH);
                int month = c.get(Calendar.MONTH);
                int year = c.get(Calendar.YEAR);

                datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        endDateDisplay.setText(dayOfMonth+"/"+(month+1)+"/"+year);
                    }
                },day,month,year);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });

        openImagebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("REGISTER", "image btn clicked");
                openImagebtn.setVisibility(openImagebtn.VISIBLE);
                imagePreview.setVisibility(imagePreview.GONE);
                openFilePreview();
            }
        });

        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerbtn.setVisibility(registerbtn.GONE);
                progressBarResult.setVisibility(progressBarResult.VISIBLE);
                registerdatatoFirestore();
            }
        });

        return view;
    }

    public String getFileExtension(Uri uri) {
        ContentResolver cr = getContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    private void openFilePreview() {
        Log.d("REGISTER", "image btn clicked");
        openImagebtn.setVisibility(openImagebtn.VISIBLE);
        imagePreview.setVisibility(imagePreview.GONE);
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void registerdatatoFirestore(){

        final College collegedata = new College();

        if(imageuri != null){
            final StorageReference fileRef = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageuri));

            fileRef.putFile(imageuri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) throw task.getException();
                    return fileRef.getDownloadUrl();
                }
            })
                    .addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();

                                collegedata.setcollegeImage(downloadUri.toString());
                                collegedata.setCollegeItemName(collegename.getText().toString());
                                collegedata.setCollegeListFestName(festname.getText().toString());
                                collegedata.setCollegeContact(festContact.getText().toString());
                                collegedata.setCollegeListFestDetails(festddetails.getText().toString());
                                collegedata.setCollegeListStartDate(startDateDisplay.getText().toString());
                                collegedata.setCollegeListEndDate(endDateDisplay.getText().toString());
                                collegedata.setUserId(firebaseUser.getUid());


                                firestore.collection("festdata").add(collegedata)
                                        .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentReference> task) {

                                                registerbtn.setVisibility(View.VISIBLE);
                                                progressBarResult.setVisibility(View.GONE);

                                                Toast.makeText(
                                                        getContext(),
                                                        "Fest Details Saved",
                                                        Toast.LENGTH_LONG
                                                ).show();

                                                Intent newpage = new Intent(getContext(), AddEvent.class).putExtra("festId", task.getResult().getId());
                                                startActivity(newpage);
                                            }
                                        });
                            }
                        }
                    });
        }

    }
}
