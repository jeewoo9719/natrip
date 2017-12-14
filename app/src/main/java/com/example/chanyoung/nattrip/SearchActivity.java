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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class SearchActivity extends AppCompatActivity implements TextWatcher{

    Button btn,bt;  //투어 시작일이랑 종료일용 버튼
    Button btSearch; //투어 검색용 버튼

    int year_x, month_x,day_x;  //투어 시작일 받는 인자
    int year_y, month_y,day_y;  //투어

    // 종료일 받는 인자
    static final int DILOG_ID=0; //투어 시작일용
    static final int DILOG_ID2=1;  //투어  종료일용
    ListView listView2;  //나라 리스트
    EditText editText;   //검색 단어용
    ArrayAdapter<String> arrayAdapter; //나라 리스트들 저장
    TextView textView1;
    TextView textView2;
    //MyDBHandler dbHandler;
    //DatabaseReference tabel;

    MyAdapter myAdapter;


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
            textView1=(TextView)findViewById(R.id.textViewStartDate);
            textView2=(TextView)findViewById(R.id.textViewEndDate);
            // dbHandler=new MyDBHandler(this,"test.db",null,1);
            arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
            /*arrayAdapter.add("A_list");
            arrayAdapter.add("B_list");
            arrayAdapter.add("C_list");*/
            listView2.setAdapter(arrayAdapter);
            listView2.setTextFilterEnabled(true);
            editText.addTextChangedListener(this);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        DatabaseReference table = FirebaseDatabase.getInstance().getReference("tours");
        table.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayAdapter.clear();
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    //들어온 메세지들을 일단 다 받겠다
                    //Tour tour = data.getValue(Tour.class);
                    //이형태로 데이터를 만들어서 넘겨준다.
                    arrayAdapter.add(data.getKey());
                }
                arrayAdapter.notifyDataSetChanged();//데이터변경알림
                listView2.setSelection(arrayAdapter.getCount()-1);
                //리스트 뷰의 셀렉트를 현재 선택된 것 중 가장 마지막에 보여줌.
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        searchTour();
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
            else {
                return new DatePickerDialog(this, dpickerListner2, year_y, month_y, day_y);
            }


        }

        private DatePickerDialog.OnDateSetListener dpickerListner
                =new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker view1, int year,int monthOfYear, int dayOfMonth){
                year_x=year;
                month_x=monthOfYear;
                day_x=dayOfMonth;
                textView1.setText(year_x+"/"+(month_x+1)+"/"+day_x);
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
                textView2.setText(year_y+"/"+(month_y+1)+"/"+day_y);
                Toast.makeText(SearchActivity.this,year_y+"/"+(month_y+1)+"/"+day_y,Toast.LENGTH_LONG).show();
            }
        };
   public void searchTour(){
       btSearch=(Button)findViewById(R.id.searchTour);
       btSearch.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View view){
               Toast.makeText(getApplicationContext(),"버튼",Toast.LENGTH_SHORT).show();
               Intent mainTourPage = new Intent(SearchActivity.this, MainActivity.class);
               mainTourPage.putExtra("userID",ID);
               mainTourPage.putExtra("tourPlace",editText.getText().toString());
               mainTourPage.putExtra("startTourDateY",year_x);
               mainTourPage.putExtra("startTourDateM",month_x+1);
               mainTourPage.putExtra("startTourDateD",day_x);
               mainTourPage.putExtra("endTourDateY",year_y);
               mainTourPage.putExtra("endTourDateM",month_y+1);
               mainTourPage.putExtra("endTourDateD",day_y);
               startActivity(mainTourPage);
           }
       });
   }

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
