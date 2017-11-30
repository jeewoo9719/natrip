package com.example.chanyoung.nattrip;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;

public class RegisterTourActivity extends AppCompatActivity {

    public  String id;
    DatabaseReference table;
    ArrayList<String> spinnerlist = new ArrayList<String>();
    EditText tour_name, tour_thing;
    String tour_place;
    int year_s,month_s,day_s,day_e,year_e,month_e;
    Button startbutton,endbutton;
    static  final int DILOG_S=0;
    static  final int DILOG_E=0;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_home:
                    Intent main_page = new Intent(RegisterTourActivity.this,SearchActivity.class);
                    main_page.putExtra("userID",id);
                    startActivity(main_page);
                    break;
                case R.id.action_MyTour:
                    Intent chat_page = new Intent(RegisterTourActivity.this,MainActivity.class);
                    chat_page.putExtra("userID",id);
                    startActivity(chat_page);
                    break;

                case R.id.action_Messenger:
                    Intent msg_page = new Intent(RegisterTourActivity.this,ChatList.class);
                    msg_page.putExtra("userID",id);
                    startActivity(msg_page);
                    break;
                case R.id.action_setting:
                    Intent setting_page = new Intent(RegisterTourActivity.this,SettingActivity.class);
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
        setContentView(R.layout.activity_register_tour);

        table = FirebaseDatabase.getInstance().getReference("Tours");

        Intent intent  =  getIntent();
        id =  intent.getExtras().getString("id");

        final Calendar cal = Calendar.getInstance();
        year_s=cal.get(Calendar.YEAR);
        month_s=cal.get(Calendar.MONTH);
        day_s=cal.get(Calendar.DAY_OF_MONTH);
        year_e=cal.get(Calendar.YEAR);
        month_e=cal.get(Calendar.MONTH);
        day_e=cal.get(Calendar.DAY_OF_MONTH);

        // 날짜초기화
        tour_thing=  (EditText)findViewById(R.id.tourDetail);
        tour_name=  (EditText)findViewById(R.id.tourName);


        showDialog();

        startbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(DILOG_S);
            }
        });
        endbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(DILOG_E);
            }
        });


        Spinner spinner  = (Spinner) findViewById(R.id.spinner);

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
                tour_place = spinnerlist.get(i);
                //textView.setText(number); 장소 받아오기
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Button regist  =(Button) findViewById(R.id.btnRegiTour);
        regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                regiTour();
                finish();
            }
        });


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }
    public void showDialog(){
        startbutton = (Button) findViewById(R.id.btnStart);
        endbutton =(Button) findViewById(R.id.btnEnd);
        //final DatePicker dp=(DatePicker) findViewById(R.id.dp);
        startbutton.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v1){
                        showDialog(DILOG_S);
                    }

                }
        );
        endbutton.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v2){
                        showDialog(DILOG_E);
                    }

                }
        );
    }
    protected Dialog onCreateDialog(int id1){
        if(id1==DILOG_S) {
            return new DatePickerDialog(this, dpickerListner, year_s, month_s,day_s);
        }
        return null;
    }
    protected Dialog onCreateDialog2(int id){
        if(id==DILOG_E) {
            return new DatePickerDialog(this, dpickerListner2, year_e, month_e,day_e);
        }
        return null;
    }
    private DatePickerDialog.OnDateSetListener dpickerListner
            =new DatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker view1, int year, int monthOfYear , int day){
            year_s=year;
            month_s=monthOfYear+1;
            day_s=day;
        }
    };

    private DatePickerDialog.OnDateSetListener dpickerListner2
            =new DatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker view2, int year,int monthOfYear,  int day){
            year_e=year;
            month_e=monthOfYear+1;
            day_e=day;
        }
    };



    public void regiTour(){
        table= FirebaseDatabase.getInstance().getReference("tours");
        Tour newtour = new Tour(id,tour_name.getText().toString(),tour_thing.getText().toString(),
                tour_place, year_s, month_s, year_e, month_e);
        table.child(tour_place).setValue(newtour);
        tour_name.setText("");
        tour_thing.setText("");
        tour_place="";
        //사진이랑날짜

    }

}
