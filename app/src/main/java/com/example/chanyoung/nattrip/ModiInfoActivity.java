package com.example.chanyoung.nattrip;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.R.attr.id;

public class ModiInfoActivity extends AppCompatActivity {

    EditText change_na, change_pw;
    DatabaseReference table;
    String userID;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_home:
                    Intent main_page = new Intent(ModiInfoActivity.this,SearchActivity.class);
                    main_page.putExtra("userID",id);
                    startActivity(main_page);
                    break;
                case R.id.action_MyTour:
                    Intent chat_page = new Intent(ModiInfoActivity.this,MainActivity.class);
                    chat_page.putExtra("userID",id);
                    startActivity(chat_page);
                    break;
                case R.id.action_Messenger:
                    Intent msg_page = new Intent(ModiInfoActivity.this,ChatList.class);
                    msg_page.putExtra("userID",id);
                    startActivity(msg_page);
                    break;
                case R.id.action_setting:
                    Intent setting_page = new Intent(ModiInfoActivity.this,SettingActivity.class);
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
        setContentView(R.layout.activity_modi_info);

        table = FirebaseDatabase.getInstance().getReference("users");

        Intent intent = getIntent();
        userID=intent.getExtras().getString("id");
        change_na = (EditText)findViewById(R.id.editText7);
        change_pw=(EditText)findViewById(R.id.editText9);
        change_na.setText("");
        change_pw.setText("");

        Button change_info = (Button)findViewById(R.id.button6);
        change_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //GO_back.putExtra("ch_na",change_na.getText().toString());
                //GO_back.putExtra("ch_pw",change_pw.getText().toString());
                if(!change_na.getText().toString().equals("")){
                    table.child(userID).child("name").setValue(change_na.getText().toString());
                }
                if(!change_pw.getText().toString().equals("")){
                    table.child(userID).child("pw").setValue(change_pw.getText().toString());
                }
                change_na.setText("");
                change_pw.setText("");

                Toast.makeText(getApplicationContext(),"수정되었습니다.",Toast.LENGTH_SHORT).show();
                finish();

            }
        });

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
