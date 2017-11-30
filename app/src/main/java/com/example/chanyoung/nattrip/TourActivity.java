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

import static android.R.attr.id;

public class TourActivity extends AppCompatActivity {

    String ID;
    String name;
    String u_language;
    DatabaseReference table;

    TextView userIDView;
    TextView userNameView;
    TextView languageView;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_home:
                    Intent main_page = new Intent(TourActivity.this,MainActivity.class);
                    main_page.putExtra("userID",id);
                    startActivity(main_page);
                    break;
                case R.id.action_MyTour:
                    Intent chat_page = new Intent(TourActivity.this,SignIn.class);
                    chat_page.putExtra("userID",id);
                    startActivity(chat_page);
                    break;
                case R.id.action_Messenger:
                    Intent msg_page = new Intent(TourActivity.this,ChatList.class);
                    msg_page.putExtra("userID",id);
                    startActivity(msg_page);
                    break;
                case R.id.action_setting:
                    Intent setting_page = new Intent(TourActivity.this,ChatList.class);
                     setting_page.putExtra("userID",id);
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

        userIDView = (TextView)findViewById(R.id.a_userID);
        userNameView = (TextView)findViewById(R.id.a_userName);
        languageView = (TextView)findViewById(R.id.a_language);

        table = FirebaseDatabase.getInstance().getReference("users");

        Bundle bundle = getIntent().getExtras();//클릭시 intent로 온 UserID 받음
        if(bundle != null){
            ID = bundle.getString("userID");
            userIDView.setText(ID);    //bundle.getString(key)
            userNameView.setText(bundle.getString("userName"));    //bundle.getString( key )
            languageView.setText(bundle.getString("userLanguage"));    //bundle.getString( key )
        }

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
