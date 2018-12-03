package com.example.nicho.instagramclone;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.nicho.instagramclone.Model.ImagePost;
import com.example.nicho.instagramclone.Util.MyStringManipulationHelper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class ShareActivity extends AppCompatActivity implements View.OnClickListener
    {
        //Widgets
        private AppBarLayout appBarLayout;
        private Toolbar toolbar;
        private ImageView imageView;
        private EditText descriptionEditText;
        private Button button;
        private ProgressBar progressBar;

        //Values
        private String imageUrl;
        private String description;
        private String userID;
        private String imageName;

        //Firebase
        private FirebaseDatabase firebaseDatabase;
        private FirebaseUser firebaseUser;
        private DatabaseReference databaseReference;
        private StorageReference storageReference;

        //Constant
        private static final String TAG = "ShareActivity";

        @Override
        protected void onCreate(Bundle savedInstanceState)
            {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_share);

                InitialiseWidgets();
                GetIncomingIntent();

                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                userID = firebaseUser.getUid();
                firebaseDatabase = FirebaseDatabase.getInstance();
                databaseReference = firebaseDatabase.getReference("user_images").child(userID);
                storageReference = FirebaseStorage.getInstance().getReference("imagepost").child(userID);
            }

        private void InitialiseWidgets()
            {
                appBarLayout = findViewById(R.id.activity_share_appbarlayout);
                toolbar = findViewById(R.id.activity_share_toolbar);
                setSupportActionBar(toolbar);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                imageView = findViewById(R.id.activity_share_imageview);
                progressBar = findViewById(R.id.activity_share_progressbar);
                descriptionEditText = findViewById(R.id.activity_share_edittext);
                button = findViewById(R.id.activity_share_button);
                button.setOnClickListener(this);
            }

        private void GetIncomingIntent()
            {
                Intent incomingIntent = getIntent();
                imageUrl = incomingIntent.getStringExtra("SELECTED_IMAGE_URL");
                if(imageUrl != null && !imageUrl.isEmpty())
                    {
                        Glide.with(this).load(imageUrl).into(imageView);
                    }
            }

        @Override
        public boolean onSupportNavigateUp()
            {
                onBackPressed();
                return true;
            }

        @Override
        public void onClick(View v)
            {
                switch (v.getId())
                    {
                        case R.id.activity_share_button:
                            imageName = System.currentTimeMillis() + "." + MyStringManipulationHelper.GetImageName(imageUrl);
                            Log.d(TAG, "onClick: Image Storage Path: " + imageName);
                            UploadImageToStorage();
                            break;
                    }
            }

        /**
         * This method uploads the image as a byte[] to firebase storage with they key **imageName**
         * In its onSuccesss(), UploadImageData() is called.
         */
        private void UploadImageToStorage()
            {
                Log.d(TAG, "UploadImageToStorage: Method called");
                progressBar.setVisibility(View.VISIBLE);
                imageView.setDrawingCacheEnabled(true);
                imageView.buildDrawingCache();
                Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                byte[] data = outputStream.toByteArray();

                UploadTask uploadTask = storageReference.child(imageName).putBytes(data);
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
                    {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                            {
                                Log.d(TAG, "onSuccess: Image uploaded successfully");
                                UploadImageData();
                            }
                    })
                        .addOnFailureListener(new OnFailureListener()
                            {
                                @Override
                                public void onFailure(@NonNull Exception e)
                                    {
                                        Log.d(TAG, "Image upload failed: " + e.getLocalizedMessage());
                                        Toast.makeText(ShareActivity.this, "Upload failed", Toast.LENGTH_SHORT).show();

                                    }
                            });
                progressBar.setVisibility(View.GONE);
            }

        /**
         * Uploads image data to firebase realtime database under "user_post_url" node, child **userID**
         */
        private void UploadImageData()
            {
                Log.d(TAG, "UploadImageData: Method called");
                String databaseImageKey = String.valueOf(System.currentTimeMillis());
                Log.d(TAG, "UploadImageData: databaseImageKey: " + databaseImageKey);
                description = descriptionEditText.getText().toString().trim();

                ImagePost imagePost = new ImagePost(description, "10/07/2018",
                        imageName,databaseImageKey , userID, "Sample tags");

                //Upload to Firebase Database (description & image url)
                databaseReference.child(databaseImageKey).setValue(imagePost)
                        .addOnSuccessListener(this, new OnSuccessListener<Void>()
                            {
                                @Override
                                public void onSuccess(Void aVoid)
                                    {
                                        Log.d(TAG, "onSuccess: Upload image data successful");
                                        Toast.makeText(ShareActivity.this, "Upload successful", Toast.LENGTH_SHORT).show();
                                    }
                            })
                        .addOnFailureListener(this, new OnFailureListener()
                            {
                                @Override
                                public void onFailure(@NonNull Exception e)
                                    {
                                        Log.d(TAG, "onFailure: Upload image data failed");
                                        Toast.makeText(ShareActivity.this, "Upload Failed: ", Toast.LENGTH_SHORT).show();
                                    }
                            });
            }


    }
