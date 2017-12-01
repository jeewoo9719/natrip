package com.example.chanyoung.nattrip;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TourActivity extends AppCompatActivity {

    String ID;
    DatabaseReference table;

    TextView guideIDView;
    TextView placeView;
    TextView tourNameView;
    TextView tourDetailView;

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

        table = FirebaseDatabase.getInstance().getReference("tours");

        Bundle bundle = getIntent().getExtras();//클릭시 intent로 온 UserID 받음
        if(bundle != null){
            ID = bundle.getString("userID");
        }
       //table.child()

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
