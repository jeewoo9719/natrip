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
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import static android.R.attr.id;

public class Register extends AppCompatActivity {
    DatabaseReference table; //데이터베이스 레퍼런스 객체 선언
    ArrayList<User> userList;

    String _id; String _pw; String _name; String _language;

    EditText re_id;
    EditText re_pw;
    //EditText re_pwpw = (EditText) findViewById(R.id.ed_pwpw;
    EditText re_name;
    //EditText re_email = (EditText) findViewById(R.id.editText8);
    EditText re_language;

    Button regist_button;
    Button main_button;
    Button chat_button;
    Button image_button;

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_home:
                    Intent main_page = new Intent(Register.this,MainActivity.class);
                    main_page.putExtra("userID",id);
                    startActivity(main_page);
                    break;
                case R.id.action_MyTour:
                    Intent chat_page = new Intent(Register.this,SignIn.class);
                    chat_page.putExtra("userID",id);
                    startActivity(chat_page);
                    break;
                case R.id.action_Messenger:
                    Intent msg_page = new Intent(Register.this,ChatList.class);
                    msg_page.putExtra("userID",id);
                    startActivity(msg_page);
                    break;
                case R.id.action_setting:
                    Intent setting_page = new Intent(Register.this,ChatList.class);
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
        setContentView(R.layout.activity_register);

        re_id = (EditText) findViewById(R.id.ed_id);
        re_pw = (EditText) findViewById(R.id.ed_pw);
        re_name = (EditText) findViewById(R.id.ed_name);
        re_language = (EditText) findViewById(R.id.ed_language);

        regist_button = (Button) findViewById(R.id.regiButton);
        regist_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                regiUser();
                Intent main_page = new Intent(Register.this,MainActivity.class);
                main_page.putExtra("userID",id);
                startActivity(main_page);
                //User regiUser = new User(_id,_pw,_name,_language);
                // DatabaseReference newPerson = table.child(_id);
                // newPerson.child(_id).setValue(regiUser);
            }
        });
        main_button = (Button)findViewById(R.id.mainButton);
        main_button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent main_page = new Intent(Register.this,MainActivity.class);
                startActivity(main_page);
            }
        });
        chat_button = (Button)findViewById(R.id.chatButton);
        chat_button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent main_page = new Intent(Register.this,ChatList.class);
                startActivity(main_page);
            }
        });
        image_button = (Button)findViewById(R.id.imageButton);
        image_button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent image_page = new Intent(Register.this,InsertImage.class);
                startActivity(image_page);
            }
        });

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }
    public void regiUser() {//데이터베스 연결
        table = FirebaseDatabase.getInstance().getReference("users");
        User regiUser = new User(re_id.getText().toString(),
                re_pw.getText().toString(),
                re_name.getText().toString(),
                re_language.getText().toString());
        table.child(re_id.getText().toString()).setValue(regiUser);
        re_id.setText("");
        re_pw.setText("");
        re_name.setText("");
        re_language.setText("");
    }

}
