package com.example.chanyoung.nattrip;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class regist_guide extends AppCompatActivity {
    ArrayList<String> spinnerlist  = new ArrayList<String>();
    public String  guide_place;
    RadioGroup rg;
    public String guide_language;
    public TextView photo;
    public String ID;
    DatabaseReference table;
    //사진
    private ImageButton mPhotoPickerButton;
    private ImageView mImageView;
    private ImageButton mPhotoPickerButtonPose;
    private ImageView mImageViewPose;

    private static final String TAG="InsertImage";
    private Uri filePath;
    private static final int GALLERY_INTENT =  2;
    private static final int CAMERA_REQUEST_CODE =  1;
    private boolean pose=false;
    //포즈 사진인 경우 true, 그냥 사진인 경우 false

    private ProgressDialog mProgressDialog;
    private StorageReference mPhotoStorageReference;
    //올리기

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist_guide);
    //사진
        mPhotoStorageReference = FirebaseStorage.getInstance().getReference();
        mProgressDialog =new ProgressDialog(this);//저장소

        mPhotoPickerButton = (ImageButton)findViewById(R.id.mPhotoPickerButton);//갤러리 버튼
        mPhotoPickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pose=false;//신분증 사진이다
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, GALLERY_INTENT);
            }
        });
        mPhotoPickerButtonPose = (ImageButton)findViewById(R.id.mPhotoPickerButtonPose);//갤러리 버튼
        mPhotoPickerButtonPose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pose=true;//포즈사진이다
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, GALLERY_INTENT);
            }
        });
        mImageView =(ImageView)findViewById(R.id.imgView);
        mImageViewPose =(ImageView)findViewById(R.id.imgViewPose);
        //올리기
        Bundle bundle = getIntent().getExtras();//클릭시 intent로 온 UserID 받음
        if(bundle != null){
            ID = bundle.getString("userID");
        }
        table = FirebaseDatabase.getInstance().getReference("users");

        Spinner spinner  =  (Spinner)findViewById(R.id.spinner2);
        spinnerlist.add("Seoul");
        spinnerlist.add("London");
        spinnerlist.add("Paris");
        spinnerlist.add("L.A.");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item,
                spinnerlist);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                guide_place = spinnerlist.get(i);
                //textView.setText(number); 장소 받아오기
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        rg = (RadioGroup)findViewById(R.id.rg);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.radioButton){
                    guide_language = "Korean";
                }else if(checkedId==R.id.radioButton2){
                    guide_language = "English";
                }else if(checkedId==R.id.radioButton3){
                    guide_language =  "Both";
                }
            }
        });
        photo = (TextView) findViewById(R.id.textView15);

        Random rand = new Random();
        int  num = rand.nextInt()%3;
        switch(num){
            case 0:
                photo.setText("O표시");
                break;
            case 1:
                 photo.setText("X표시");
                break;
            case 2:
                photo.setText("V표시");
                break;
        }


        //table.child(ID).child("guide").setValue(1);
        //table.child(ID).child("userLanguage").setValue(guide_language);
        //table.child(ID).child("userPlace").setValue(guide_place);

        Button registGuide  = (Button) findViewById(R.id.button4);
        registGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                table.child(ID).child("guide").setValue(1);
                table.child(ID).child("userLanguage").setValue(guide_language);
                table.child(ID).child("userPlace").setValue(guide_place);
                finish();
            }
        });

    }
    //사진
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == GALLERY_INTENT && resultCode == RESULT_OK&&pose==false){
            filePath = data.getData();
            Log.d(TAG,"uri:" + String.valueOf(filePath));
            try {
                //Uri 파일을 Bitmap으로 만들어서 ImageView에 집어 넣는다.
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                mImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(requestCode == GALLERY_INTENT && resultCode == RESULT_OK&&pose==true){
            filePath = data.getData();
            Log.d(TAG,"uri:" + String.valueOf(filePath));
            try {
                //Uri 파일을 Bitmap으로 만들어서 ImageView에 집어 넣는다.
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                mImageViewPose.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK){
            mProgressDialog.setMessage("Uploading ...");
            mProgressDialog.show();

            Uri uri = data.getData();
            StorageReference filepath = mPhotoStorageReference.child("Photos").child(uri.getLastPathSegment());
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mProgressDialog.dismiss();
                    Toast.makeText(regist_guide.this, "Upload Done.",Toast.LENGTH_LONG).show();
                }
            });
        }
        /*else if(requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK){
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
        }*/
    }
}
