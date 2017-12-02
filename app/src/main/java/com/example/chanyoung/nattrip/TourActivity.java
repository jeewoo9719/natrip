package com.example.chanyoung.nattrip;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TourActivity extends AppCompatActivity {

    String ID; //userID
    String place; // 여행 지역
    String guideID; // 가이드 이름
    DatabaseReference table;

    //투어 정보
    TextView guideIDView;
    TextView placeView;
    TextView tourNameView;
    TextView tourDetailView;

    //가이드 정보
    TextView guideNameView;
    TextView guideEmailView;
    //ImageView guidePictureView
    //String guidePictureURL
    //TextView guidePhoneView;

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

        guideEmailView = (TextView)findViewById(R.id.guideEmail);
        guideNameView = (TextView)findViewById(R.id.guideName);


        table = FirebaseDatabase.getInstance().getReference("tours");
        Bundle bundle = getIntent().getExtras();//MainActivity에서 item 클릭시 intent로 온 UserID, place, guideName 받음
        if(bundle != null){
            ID = bundle.getString("userID");
            place = bundle.getString("place");
            guideID = bundle.getString("guideID");
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


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
