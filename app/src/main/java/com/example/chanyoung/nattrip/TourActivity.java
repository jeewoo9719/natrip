package com.example.chanyoung.nattrip;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class TourActivity extends AppCompatActivity {

    String ID; //userID
    String place; // 여행 지역
    String guideID; // 가이드 이름
    String re;
    DatabaseReference table;

    //투어 정보
    TextView guideIDView;
    TextView placeView;
    TextView tourNameView;
    TextView tourDetailView;
    ImageView tourImageView1;
    ImageView tourImageView2;
    ImageView tourImageView3;

    //가이드 정보
    TextView guideNameView;
    TextView guideEmailView;
    //ImageView guidePictureView
    //String guidePictureURL
    //TextView guidePhoneView;

    //투어 예약하기
    Button btnReservation;
    //채팅방 이름
    String chatID;
    SimpleDateFormat mSimpleDateFormat;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_home:
                    Intent main_page = new Intent(TourActivity.this,SearchActivity.class);
                    main_page.putExtra("userID",ID);
                    startActivity(main_page);
                    break;
                case R.id.action_MyTour:
                    Intent chat_page = new Intent(TourActivity.this,MainActivity.class);
                    chat_page.putExtra("userID",ID);
                    startActivity(chat_page);
                    break;
                case R.id.action_Messenger:
                    Intent msg_page = new Intent(TourActivity.this,ChatList.class);
                    msg_page.putExtra("userID",ID);
                    startActivity(msg_page);
                    break;
                case R.id.action_setting:
                    Intent setting_page = new Intent(TourActivity.this,SettingActivity.class);
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
        setContentView(R.layout.activity_tour);

        guideIDView = (TextView)findViewById(R.id.guideID);
        placeView = (TextView)findViewById(R.id.place);
        tourNameView = (TextView)findViewById(R.id.tourName);
        tourDetailView = (TextView)findViewById(R.id.tourDetail);
        tourImageView1 = (ImageView)findViewById(R.id.tourImage1);
        tourImageView2 = (ImageView)findViewById(R.id.tourImage2);
        tourImageView3 = (ImageView)findViewById(R.id.tourImage3);

        guideEmailView = (TextView)findViewById(R.id.guideEmail);
        guideNameView = (TextView)findViewById(R.id.guideName);
        mSimpleDateFormat = new SimpleDateFormat("a h:mm", Locale.getDefault());

        table = FirebaseDatabase.getInstance().getReference("tours");
        Bundle bundle = getIntent().getExtras();//MainActivity에서 item 클릭시 intent로 온 UserID, place, guideName 받음
        if(bundle != null){
            ID = bundle.getString("userID");
            place = bundle.getString("tourPlace");
            guideID = bundle.getString("guideID");
            re = bundle.getString("regist");
        }

        //투어 정보
        table.child(place).child(guideID).addValueEventListener(new ValueEventListener(){
            //여행지역과 가이드이름과 맞는 투어정보 가져옴
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                guideIDView.setText(guideID);
                placeView.setText(place);
                tourNameView.setText(dataSnapshot.child("tourName").getValue().toString());
                tourDetailView.setText(dataSnapshot.child("detail").getValue().toString());
                Uri image1Uri = Uri.parse(dataSnapshot.child("image1FilePath").getValue().toString());
                Uri image2Uri = Uri.parse(dataSnapshot.child("image2FilePath").getValue().toString());
                Uri image3Uri = Uri.parse(dataSnapshot.child("image3FilePath").getValue().toString());

                Glide.with(TourActivity.this).load(image1Uri).into(tourImageView1);
                Glide.with(TourActivity.this).load(image2Uri).into(tourImageView2);
                Glide.with(TourActivity.this).load(image3Uri).into(tourImageView3);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        //가이드 정보
        table = FirebaseDatabase.getInstance().getReference("users");
        table.child(guideID).addValueEventListener(new ValueEventListener(){
            //여행지역과 가이드이름과 맞는 투어정보 가져옴
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                
                guideEmailView.setText(dataSnapshot.child("userEmail").getValue().toString());
                guideNameView.setText(dataSnapshot.child("userName").getValue().toString());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        btnReservation = (Button)findViewById(R.id.btnReservation);
        if(re.equals("0")){
            btnReservation.setText("예약됨");
        }

        btnReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //투어에 예약자 저장
                table = FirebaseDatabase.getInstance().getReference("tours");
                table.child(place).child(guideID).child("reserveUserID").setValue(ID);

                table = FirebaseDatabase.getInstance().getReference("messageDB");
                if (ID.compareTo(guideID) < 0) {//대화 상대에 따라 다른 데이터 베이스 생성
                    chatID = ID + guideID;
                } else {
                    chatID = guideID + ID;
                }
                table=FirebaseDatabase.getInstance().getReference("messageDB").child(chatID).push();
                //table.child("").setValue(guideID);
                table.child("message").setValue("");
                table.child("time").setValue(mSimpleDateFormat.format(System.currentTimeMillis()));
                table.child("userName").setValue("");
                Toast.makeText(TourActivity.this, "Reservation Done.",Toast.LENGTH_LONG).show();
                finish();
            }
        });


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
