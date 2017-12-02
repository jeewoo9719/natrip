package com.example.chanyoung.nattrip;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SettingActivity extends AppCompatActivity {

    DatabaseReference table;
    public String ID;
    public String user_name;
    public int user_guide = 1; //1이면 가이드
    public String Guide = "NULL";
    public String name;
    public TextView tv_name;
    public  TextView tv_guide;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_home:
                    Intent main_page = new Intent(SettingActivity.this,SearchActivity.class);
                    main_page.putExtra("userID",ID);
                    startActivity(main_page);
                    break;
                case R.id.action_MyTour:
                    Intent chat_page = new Intent(SettingActivity.this,MainActivity.class);
                    chat_page.putExtra("userID",ID);
                    startActivity(chat_page);
                    break;

                case R.id.action_Messenger:
                    Intent msg_page = new Intent(SettingActivity.this,ChatList.class);
                    msg_page.putExtra("userID",ID);
                    startActivity(msg_page);
                    break;
                case R.id.action_setting:
                    Intent setting_page = new Intent(SettingActivity.this,SettingActivity.class);
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
        setContentView(R.layout.activity_setting);

        Bundle bundle = getIntent().getExtras();//클릭시 intent로 온 UserID 받음
        if(bundle != null){
            ID = bundle.getString("userID");
        }
        table = FirebaseDatabase.getInstance().getReference("users");
        tv_name = (TextView) findViewById(R.id.textView);
        tv_guide = (TextView) findViewById(R.id.textView2);

        //Toast.makeText(getApplicationContext(),user_ID,Toast.LENGTH_SHORT).show(); ㅇㅇ

        table.child(ID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //가이드정보, 이름 가져오기
                User user = dataSnapshot.getValue(User.class);
                user_name = dataSnapshot.child("userName").getValue().toString();
                String s = dataSnapshot.child("guide").getValue().toString();
                user_guide = Integer.parseInt(s);
                if(user_guide==0)
                    Guide = "NotGuide";
                else
                    Guide = "Guide";
                tv_name.setText(user_name); //User_name 바꿔주기
                tv_guide.setText(Guide);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        }); //가져오기

        Button button1 = (Button) findViewById(R.id.button);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"사용자 정보 수정",Toast.LENGTH_SHORT).show();
                Intent new_page = new Intent(SettingActivity.this,ModiInfoActivity.class);
                new_page.putExtra("userID",ID);
                startActivity(new_page);
            }
        });
        Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"가이드 신청",Toast.LENGTH_SHORT).show();

                if(user_guide==1) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(SettingActivity.this);
                    alertDialog.setTitle("가이드 신청").setMessage("이미 가이드입니다.");
                    alertDialog.setPositiveButton("확인", null);
                    alertDialog.show();
                }
                else{
                    //여기 바꿔주기
                    Intent new_page = new Intent(SettingActivity.this,regist_guide.class);
                    new_page.putExtra("userID",ID);
                    startActivity(new_page);
                }
            }
        });
        Button button3 = (Button) findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"투어 등록",Toast.LENGTH_SHORT).show();
                if(user_guide==0) { //1로 고쳐야함
                    Intent new_page = new Intent(SettingActivity.this, RegisterTourActivity.class);
                    new_page.putExtra("userID",ID);
                    startActivity(new_page);
                }
                else{
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(SettingActivity.this);
                    alertDialog.setMessage("가이드가 아닙니다.");
                    alertDialog.setPositiveButton("확인", null);
                    alertDialog.show();
                }
            }
        });

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
