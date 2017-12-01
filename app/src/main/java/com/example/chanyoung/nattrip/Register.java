package com.example.chanyoung.nattrip;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {
    DatabaseReference table; //데이터베이스 레퍼런스 객체 선언

    EditText ed_id;
    EditText ed_pw;
    EditText ed_re_pw;
    EditText ed_name;
    EditText ed_email;

    boolean check_id = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ed_id = (EditText) findViewById(R.id.ed_id);
        ed_pw = (EditText) findViewById(R.id.ed_pw);
        ed_re_pw = (EditText) findViewById(R.id.ed_re_pw);
        ed_name = (EditText) findViewById(R.id.ed_name);
        ed_email = (EditText) findViewById(R.id.ed_email);

        Button id_check = (Button) findViewById(R.id.btn_id_check);
        id_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check_id=true; //중복확인
                if(check_id==false){
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(Register.this);
                    alertDialog.setTitle("아이디 중복 확인").setMessage("이미 등록된 아이디입니다.");
                    alertDialog.setPositiveButton("확인", null);
                    alertDialog.show();
                }else if(check_id==true){
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(Register.this);
                    alertDialog.setTitle("아이디 중복 확인").setMessage("확인되었습니다.");
                    alertDialog.setPositiveButton("확인", null);
                    alertDialog.show();
                }

            }
        });

        final Button regist_button = (Button) findViewById(R.id.btn_regi);
        regist_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(ed_pw.getText().toString().equals(ed_re_pw.getText().toString())//비밀번호 확인
                        && check_id==true  ){
                    regiUser();
                    /*Intent sign_in_page = new Intent(Register.this,SignIn.class);
                    startActivity(sign_in_page);*/
                    finish();
                }else if(check_id!=true){
                    Toast.makeText(getApplicationContext(),"아이디를 다시 확인해주세요",Toast.LENGTH_LONG).show();
                }else if(ed_pw.getText().toString()!=ed_re_pw.getText().toString()){
                    Toast.makeText(getApplicationContext(),"비밀번호를 다시 확인해주세요",Toast.LENGTH_LONG).show();
                }

            }
        });
    }
    public void regiUser() {//데이터베스 연결
        table = FirebaseDatabase.getInstance().getReference("users");
        User regiUser = new User(ed_id.getText().toString(),
                ed_pw.getText().toString(),
                ed_name.getText().toString(),
                ed_email.getText().toString());
        table.child(ed_id.getText().toString()).setValue(regiUser);
        ed_id.setText("");
        ed_pw.setText("");
        ed_name.setText("");
        ed_email.setText("");
    }

}
