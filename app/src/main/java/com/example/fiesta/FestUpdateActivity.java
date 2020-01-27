package com.example.fiesta;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.fiesta.models.College;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class FestUpdateActivity extends AppCompatActivity {

    FirebaseFirestore firestore;
    FirebaseUser firebaseUser;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    Uri updateimageuri;
    ImageView updateImagePreview;
    College updatecollege;

    private static final int PICK_IMAGE_REQUEST = 1;

    ImageButton updateImageBtn;
    Button updateFestbtn;
    ProgressBar updateProgressBarResult;
    EditText updateCollegeName, updateFestName, updateStartDate, updateEndDate, updateFestdDetails;

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST &&
                resultCode == RESULT_OK &&
                data != null &&
                data.getData() != null) {
            updateimageuri = data.getData();

            updateImageBtn.setVisibility(updateImageBtn.GONE);
            updateImagePreview.setVisibility(updateImagePreview.VISIBLE);

            Picasso.get().load(updateimageuri).into(updateImagePreview);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fest_update);

        firestore = FirebaseFirestore.getInstance(); // storing database
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseStorage = FirebaseStorage.getInstance(); // storing images
        storageReference = firebaseStorage.getReference("imagesUpload");

        updatecollege = new College();


        updateImagePreview = findViewById(R.id.updateCollegeImageView);
        updateImageBtn = findViewById(R.id.updateImageButton);
        updateFestbtn = findViewById(R.id.updateFestBtn);
        updateCollegeName = findViewById(R.id.updateCollegeName);
        updateFestName = findViewById(R.id.updateFestName);
        updateFestdDetails = findViewById(R.id.updateFestdDetails);
        updateStartDate = findViewById(R.id.updateStartDate);
        updateEndDate = findViewById(R.id.updateEndDate);
        updateProgressBarResult = findViewById(R.id.updateProgressBarResult);


        firestore.collection("festdata")
                .document(getIntent().getStringExtra("festId"))
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        updatecollege = task.getResult().toObject(College.class);

                        updateCollegeName.setText(updatecollege.getCollegeItemName());
                        updateFestName.setText(updatecollege.getCollegeListFestName());
                        updateFestdDetails.setText(updatecollege.getCollegeListFestDetails());
                        updateStartDate.setText(updatecollege.getCollegeListStartDate());
                        updateEndDate.setText(updatecollege.getCollegeListEndDate());

                        if (updatecollege.getCollegeImage() != null) {
                            updateImageBtn.setVisibility(View.GONE);
                            Picasso.get().load(updatecollege.getCollegeImage()).into(updateImagePreview);
                        }

                    }
                });



        updateImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("REGISTER", "image btn clicked");
                updateImageBtn.setVisibility(updateImageBtn.VISIBLE);
                updateImagePreview.setVisibility(updateImagePreview.GONE);
                openFilePreview();
            }
        });

        updateFestbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateFestbtn.setVisibility(updateFestbtn.GONE);
                updateProgressBarResult.setVisibility(updateProgressBarResult.VISIBLE);
                storeFestImage();
            }
        });


    }

    public String getFileExtension(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    private void openFilePreview() {
        Log.d("REGISTER", "image btn clicked");
        updateImageBtn.setVisibility(updateImageBtn.VISIBLE);
        updateImagePreview.setVisibility(updateImagePreview.GONE);
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }


    public void storeFestImage() {
        final College collegedata = new College();

        if(updateimageuri != null){
            final StorageReference fileRef = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(updateimageuri));

            fileRef.putFile(updateimageuri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
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


                                updateFestData(collegedata);
                            }
                        }
                    });
        }else {
            updateFestData(collegedata);
        }
    }


    private void updateFestData(College college) {

        college.setCollegeItemName(updateCollegeName.getText().toString());
        college.setCollegeListFestName(updateFestName.getText().toString());
        college.setCollegeListFestDetails(updateFestdDetails.getText().toString());
        college.setCollegeListStartDate(updateStartDate.getText().toString());
        college.setCollegeListEndDate(updateEndDate.getText().toString());

        firestore.collection("festdata").document(getIntent().getStringExtra("festId"))
                .set(college)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateFestbtn.setVisibility(updateFestbtn.VISIBLE);
                        updateProgressBarResult.setVisibility(updateProgressBarResult.GONE);
                    }
                });
    }


}
