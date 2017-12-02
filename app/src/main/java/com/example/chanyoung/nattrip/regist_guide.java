package com.example.chanyoung.nattrip;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Random;

public class regist_guide extends AppCompatActivity {
    ArrayList<String> spinnerlist  = new ArrayList<String>();
    public String  guide_place;
    RadioGroup rg;
    public String guide_language;
    public TextView photo;
    public String ID;
    DatabaseReference table;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist_guide);

        Bundle bundle = getIntent().getExtras();//클릭시 intent로 온 UserID 받음
        if(bundle != null){
            ID = bundle.getString("userID");
        }
        table = FirebaseDatabase.getInstance().getReference("users");

        Spinner spinner  =  (Spinner)findViewById(R.id.spinner2);
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
                guide_place = spinnerlist.get(i);
                //textView.setText(number); 장소 받아오기
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        rg = (RadioGroup)findViewById(R.id.rg);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.radioButton){
                    guide_language = "Korean";
                }else if(checkedId==R.id.radioButton2){
                    guide_language = "English";
                }else if(checkedId==R.id.radioButton3){
                    guide_language =  "Both";
                }
            }
        });
        photo = (TextView) findViewById(R.id.textView15);

        Random rand = new Random();
        int  num = rand.nextInt()%3;
        switch(num){
            case 0:
                photo.setText("O표시");
                break;
            case 1:
                 photo.setText("X표시");
                break;
            case 2:
                photo.setText("V표시");
                break;
        }


        //table.child(ID).child("guide").setValue(1);
        //table.child(ID).child("userLanguage").setValue(guide_language);
        //table.child(ID).child("userPlace").setValue(guide_place);

        Button registGuide  = (Button) findViewById(R.id.button4);
        registGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                table.child(ID).child("guide").setValue(1);
                table.child(ID).child("userLanguage").setValue(guide_language);
                table.child(ID).child("userPlace").setValue(guide_place);
                finish();
            }
        });

    }
}
