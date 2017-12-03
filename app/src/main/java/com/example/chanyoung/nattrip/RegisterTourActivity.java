package com.example.chanyoung.nattrip;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

public class RegisterTourActivity extends AppCompatActivity {

    String ID=null;
    DatabaseReference table;
    ArrayList<String> spinnerlist = new ArrayList<String>();
    EditText tour_name, tour_thing;
    String tour_place;
    public int year_s,month_s,day_s,day_e,year_e,month_e;
    public String years,months,yeare,monthe;
    Button startbutton,endbutton;
    static  final int DILOG_S=0;
    static  final int DILOG_E=1;
    //사진
    private ImageButton mPhotoPickerButton1;
    private ImageView mImageView1;
    private ImageButton mPhotoPickerButton2;
    private ImageView mImageView2;
    private ImageButton mPhotoPickerButton3;
    private ImageView mImageView3;
    private int index;//몇번째 사진인지
    // 3개니까 1~3까지

    private static final String TAG="InsertImage";
    private Uri showImage;
    private Uri image1FilePath;
    private Uri image2FilePath;
    private Uri image3FilePath;
    private static final int GALLERY_INTENT =  2;
    private static final int CAMERA_REQUEST_CODE =  1;
    private ProgressDialog mProgressDialog;
    private StorageReference mPhotoStorageReference;
    //올리기

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_home:
                    Intent main_page = new Intent(RegisterTourActivity.this,SearchActivity.class);
                    main_page.putExtra("userID",ID);
                    startActivity(main_page);
                    break;
                case R.id.action_MyTour:
                    Intent chat_page = new Intent(RegisterTourActivity.this,MainActivity.class);
                    chat_page.putExtra("userID",ID);
                    startActivity(chat_page);
                    break;

                case R.id.action_Messenger:
                    Intent msg_page = new Intent(RegisterTourActivity.this,ChatList.class);
                    msg_page.putExtra("userID",ID);
                    startActivity(msg_page);
                    break;
                case R.id.action_setting:
                    Intent setting_page = new Intent(RegisterTourActivity.this,SettingActivity.class);
                    setting_page.putExtra("userID",ID);
                    startActivity(setting_page);
                    break;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_tour);
        //사진
        mPhotoStorageReference = FirebaseStorage.getInstance().getReference();
        mProgressDialog =new ProgressDialog(this);//저장소

        mPhotoPickerButton1 = (ImageButton)findViewById(R.id.mPhotoPickerButton1);//갤러리 버튼
        mPhotoPickerButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index=1;
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, GALLERY_INTENT);
            }
        });
        mPhotoPickerButton2 = (ImageButton)findViewById(R.id.mPhotoPickerButton2);//갤러리 버튼
        mPhotoPickerButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index=2;
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, GALLERY_INTENT);
            }
        });
        mPhotoPickerButton3 = (ImageButton)findViewById(R.id.mPhotoPickerButton3);//갤러리 버튼
        mPhotoPickerButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index=3;
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, GALLERY_INTENT);
            }
        });
        mImageView1 =(ImageView)findViewById(R.id.imgView1);
        mImageView2 =(ImageView)findViewById(R.id.imgView2);
        mImageView3 =(ImageView)findViewById(R.id.imgView3);
        //올리기

        table = FirebaseDatabase.getInstance().getReference("Tours");

        Bundle bundle = getIntent().getExtras();//클릭시 intent로 온 UserID 받음
        if(bundle != null){
            ID = bundle.getString("userID");
        }

        final Calendar cal = Calendar.getInstance();
        year_s=cal.get(Calendar.YEAR);
        month_s=cal.get(Calendar.MONTH);
        day_s=cal.get(Calendar.DAY_OF_MONTH);
        year_e=cal.get(Calendar.YEAR);
        month_e=cal.get(Calendar.MONTH);
        day_e=cal.get(Calendar.DAY_OF_MONTH);

        // 날짜초기화
        tour_thing=  (EditText)findViewById(R.id.tourDetail);
        tour_name=  (EditText)findViewById(R.id.tourName);

        showDialog();

        startbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(DILOG_S);
            }
        });
        endbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(DILOG_E);
            }
        });

        Spinner spinner  = (Spinner) findViewById(R.id.spinner);

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
                tour_place = spinnerlist.get(i);
                //textView.setText(number); 장소 받아오기
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Button regist  =(Button) findViewById(R.id.btnRegiTour);
        regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                regiTour();
                finish();
            }
        });


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }
    public void showDialog(){
        startbutton = (Button) findViewById(R.id.btnStart);
        endbutton =(Button) findViewById(R.id.btnEnd);
        //final DatePicker dp=(DatePicker) findViewById(R.id.dp);
        startbutton.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v1){
                        showDialog(DILOG_S);
                    }

                }
        );
        endbutton.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v2){
                        showDialog(DILOG_E);
                    }

                }
        );
    }
    protected Dialog onCreateDialog(int id1){
        if(id1==DILOG_S) {
            return new DatePickerDialog(this, dpickerListner, year_s, month_s,day_s);
        }
        else{
            return new DatePickerDialog(this, dpickerListner2, year_e, month_e,day_e);
        }
    }

    private DatePickerDialog.OnDateSetListener dpickerListner
            =new DatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker view1, int year, int monthOfYear , int day){
            year_s=year;
            month_s=monthOfYear+1;
            day_s=day;
            years=String.valueOf(year_s);
            months=String.valueOf(month_s);

        }
    };

    private DatePickerDialog.OnDateSetListener dpickerListner2
            =new DatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker view2, int year,int monthOfYear,  int day){
            year_e=year;
            month_e=monthOfYear+1;
            day_e=day;
            yeare=String.valueOf(year_e);
            monthe=String.valueOf(month_e);
        }
    };
    public void regiTour(){

        table= FirebaseDatabase.getInstance().getReference("tours");
        Tour newtour = new Tour(ID,tour_name.getText().toString(),tour_thing.getText().toString(),
                tour_place, years, months, yeare, monthe,image1FilePath.toString(),image2FilePath.toString(),image3FilePath.toString());
        table.child(tour_place).child(ID).setValue(newtour);

        tour_name.setText("");
        tour_thing.setText("");
        tour_place="";
        //사진이랑날짜
        
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == GALLERY_INTENT && resultCode == RESULT_OK&&index==1){
            showImage = data.getData();
            try {
                //Uri 파일을 Bitmap으로 만들어서 ImageView에 집어 넣는다.
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), showImage);
                mImageView1.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        if(requestCode == GALLERY_INTENT && resultCode == RESULT_OK&&index==2){
            showImage = data.getData();
            //Log.d(TAG,"uri:" + String.valueOf(filePathImage1));
            try {
                //Uri 파일을 Bitmap으로 만들어서 ImageView에 집어 넣는다.
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), showImage);
                mImageView2.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(requestCode == GALLERY_INTENT && resultCode == RESULT_OK&&index==3){
            showImage = data.getData();
            //Log.d(TAG,"uri:" + String.valueOf(filePath));
            try {
                //Uri 파일을 Bitmap으로 만들어서 ImageView에 집어 넣는다.
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), showImage);
                mImageView3.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK){
            mProgressDialog.setMessage("Uploading ...");
            mProgressDialog.show();

            Uri uri = data.getData();
            StorageReference filepath = mPhotoStorageReference.child("Photos").child("tours").child(ID).child(uri.getLastPathSegment());
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    if(index == 1){
                        image1FilePath = taskSnapshot.getDownloadUrl();
                    }
                    if(index == 2){
                        image2FilePath = taskSnapshot.getDownloadUrl();
                    }
                    if(index == 3){
                        image3FilePath = taskSnapshot.getDownloadUrl();
                    }
                    mProgressDialog.dismiss();
                    Toast.makeText(RegisterTourActivity.this, "Upload Done.",Toast.LENGTH_LONG).show();
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
