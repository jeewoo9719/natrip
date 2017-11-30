package com.example.chanyoung.nattrip;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignIn extends AppCompatActivity {

    DatabaseReference table;
    String id;
    String pw;
    String correctpw = null;
    String correctpw2 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        table = FirebaseDatabase.getInstance().getReference("users");

        Button login_btn = (Button) findViewById(R.id.btnLogin);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText idText = (EditText) findViewById(R.id.editID);
                EditText pwText = (EditText) findViewById(R.id.editPW);

                id = idText.getText().toString();
                pw = pwText.getText().toString();

                table.child(id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //User user = dataSnapshot.getValue(User.class);
                        correctpw = dataSnapshot.child("userPW").getValue().toString();

                        if(pw.equals(correctpw)){
                            Toast.makeText(getApplicationContext(),"로그인 되었습니다.",Toast.LENGTH_SHORT).show();
                            Intent new_page = new Intent(SignIn.this,SearchActivity.class);
                            new_page.putExtra("userID",id);
                            startActivity(new_page);
                        }else{
                            Toast.makeText(getApplicationContext(),"다시 입력해 주세요.",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        Button register_btn = (Button) findViewById(R.id.btnRegi);
        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent register_page = new Intent(SignIn.this, Register.class);
                register_page.putExtra("userID",id);
                startActivity(register_page);
            }
        });
    }
}