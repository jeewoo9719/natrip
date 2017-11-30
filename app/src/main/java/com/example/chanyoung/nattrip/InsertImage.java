package com.example.chanyoung.nattrip;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import static android.R.attr.id;

public class InsertImage extends AppCompatActivity {

    private ImageButton mPhotoPickerButton;
    private Button mUploadBtn;
    private ImageView mImageView;

    private static final int GALLERY_INTENT =  2;
    private static final int CAMERA_REQUEST_CODE =  1;

    private ProgressDialog mProgressDialog;
    private StorageReference mPhotoStorageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_image);

        mPhotoStorageReference = FirebaseStorage.getInstance().getReference();
        mProgressDialog =new ProgressDialog(this);

        mPhotoPickerButton = (ImageButton)findViewById(R.id.mPhotoPickerButton);
        mPhotoPickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, GALLERY_INTENT);
            }
        });

        mUploadBtn =(Button)findViewById(R.id.upload);
        mImageView =(ImageView)findViewById(R.id.imgView);

        mUploadBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,CAMERA_REQUEST_CODE);
            }
        });

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK){
            mProgressDialog.setMessage("Uploading ...");
            mProgressDialog.show();

            Uri uri = data.getData();
            StorageReference filepath = mPhotoStorageReference.child("Photos").child(uri.getLastPathSegment());
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mProgressDialog.dismiss();
                    Toast.makeText(InsertImage.this, "Upload Done.",Toast.LENGTH_LONG).show();
                }
            });
        }
        else if(requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK){

            mProgressDialog.setMessage("Uploading ...");
            mProgressDialog.show();

            Uri uri = data.getData();
            StorageReference filepath = mPhotoStorageReference.child("Photos").child(uri.getLastPathSegment());
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mProgressDialog.dismiss();
                    Uri downloadUri = taskSnapshot.getDownloadUrl();
                    Picasso.with(InsertImage.this).load(downloadUri).fit().centerCrop().into(mImageView);
                    Toast.makeText(InsertImage.this,"Uploading Finished ...",Toast.LENGTH_LONG).show();
                }
            }); //.addOnFailureListener()
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_home:
                    Intent main_page = new Intent(InsertImage.this,MainActivity.class);
                    main_page.putExtra("userID",id);
                    startActivity(main_page);
                    return true;
                case R.id.action_MyTour:
                    Intent chat_page = new Intent(InsertImage.this,MainActivity.class);
                    chat_page.putExtra("userID",id);
                    startActivity(chat_page);
                    return true;
                case R.id.action_Messenger:
                    Intent msg_page = new Intent(InsertImage.this,MainActivity.class);
                    msg_page.putExtra("userID",id);
                    startActivity(msg_page);
                    return true;
                case R.id.action_setting:
                    Intent setting_page = new Intent(InsertImage.this,MainActivity.class);
                    setting_page.putExtra("userID",id);
                    startActivity(setting_page);
                    return true;
            }
            return false;
        }

    };

}
