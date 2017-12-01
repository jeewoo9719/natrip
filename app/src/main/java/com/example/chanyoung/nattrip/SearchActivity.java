package com.example.chanyoung.nattrip;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Calendar;

public class SearchActivity extends AppCompatActivity implements TextWatcher{

    Button btn,bt;
    int year_x, month_x,day_x;  //투어 시작일 받는 인자
    int year_y, month_y,day_y;  //투어

    // 종료일 받는 인자
    static final int DILOG_ID=0; //투어 시작일용
    static final int DILOG_ID2=0;  //투어  종료일용
    ListView listView2;  //나라 리스트
    EditText editText;   //검색 단어용
    ArrayAdapter<String> arrayAdapter; //나라 리스트들 저장
    //MyDBHandler dbHandler;

    public String ID =null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Bundle bundle = getIntent().getExtras();//클릭시 intent로 온 UserID 받음
        if(bundle != null){
            ID = bundle.getString("userID");
        }

            final Calendar cal=Calendar.getInstance();

            year_x=cal.get(Calendar.YEAR);
            month_x=cal.get(Calendar.MONTH);
            day_x=cal.get(Calendar.DAY_OF_MONTH);

            year_y=cal.get(Calendar.YEAR);
            month_y=cal.get(Calendar.MONTH);
            day_y=cal.get(Calendar.DAY_OF_MONTH);

            showDialog();

            //나라 리스트 보여주고 검색
            listView2 = (ListView)findViewById(R.id.listView2);
            editText = (EditText)findViewById(R.id.editText);
            // dbHandler=new MyDBHandler(this,"test.db",null,1);
            arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
            arrayAdapter.add("A_list");
            arrayAdapter.add("B_list");
            arrayAdapter.add("C_list");
            listView2.setAdapter(arrayAdapter);
            listView2.setTextFilterEnabled(true);
            editText.addTextChangedListener(this);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_home:
                    Intent main_page = new Intent(SearchActivity.this,SearchActivity.class);
                    main_page.putExtra("userID",ID);
                    startActivity(main_page);
                    break;
                case R.id.action_MyTour:
                    Intent chat_page = new Intent(SearchActivity.this,MainActivity.class);
                    chat_page.putExtra("userID",ID);
                    startActivity(chat_page);
                    break;

                case R.id.action_Messenger:
                    Intent msg_page = new Intent(SearchActivity.this,ChatList.class);
                    msg_page.putExtra("userID",ID);
                    startActivity(msg_page);
                    break;
                case R.id.action_setting:
                    Intent setting_page = new Intent(SearchActivity.this,SettingActivity.class);
                    setting_page.putExtra("userID",ID);
                    startActivity(setting_page);
                    break;
            }
            return false;
        }

    };
    public void showDialog() {
        btn = (Button) findViewById(R.id.date_picker);
        bt = (Button) findViewById(R.id.date_picker2);
        //final DatePicker dp=(DatePicker) findViewById(R.id.dp);
        btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v1) {
                        showDialog(DILOG_ID);
                    }
                }
        );
        bt.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v2) {
                        showDialog(DILOG_ID2);
                    }
                }
        );
    }
        protected Dialog onCreateDialog(int id1){
            if(id1==DILOG_ID) {
                return new DatePickerDialog(this, dpickerListner, year_x, month_x, day_x);
            }
            return null;
        }
        protected Dialog onCreateDialog2(int id){
            if(id==DILOG_ID2) {
                return new DatePickerDialog(this, dpickerListner2, year_y, month_y, day_y);
            }
            return null;
        }
        private DatePickerDialog.OnDateSetListener dpickerListner
                =new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker view1, int year,int monthOfYear, int dayOfMonth){
                year_x=year;
                month_x=monthOfYear;
                day_x=dayOfMonth;
                Toast.makeText(SearchActivity.this,year_x+"/"+(month_x+1)+"/"+day_x,Toast.LENGTH_LONG).show();
            }
        };

        private DatePickerDialog.OnDateSetListener dpickerListner2
                =new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker view2, int year, int monthOfYear, int dayOfMonth){
                year_y=year;
                month_y=monthOfYear;
                day_y=dayOfMonth;
                Toast.makeText(SearchActivity.this,year_y+"/"+(month_y+1)+"/"+day_y,Toast.LENGTH_LONG).show();
            }
        };

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        listView2.setFilterText(editText.getText().toString());
    }

    @Override
    public void afterTextChanged(Editable e) {
        if(editText.getText().length()==0){
            listView2.clearTextFilter();
        }
    }


}
