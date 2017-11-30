package com.example.chanyoung.nattrip;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.R.attr.id;

public class MainActivity extends AppCompatActivity {

    MyAdapter adapter;
    ListView listView;
    BottomNavigationView bottomNavigationView;
    ArrayList<User> userList;
    DatabaseReference table; //데이터베이스 레퍼런스 객체 선언
    TextView userID;
    TextView userName;
    TextView language;
    String ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    public void init() {
        listView = (ListView) findViewById(R.id.listView);
        userList = new ArrayList<>();
        adapter = new MyAdapter(this, R.layout.row, userList);
        //customAdapter = new MyAdapter(this,R.layout.tour,userList);
        listView.setAdapter(adapter);
        initDB();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                User user = (User) listView.getItemAtPosition(pos);
                Intent intent = new Intent(MainActivity.this, TourActivity.class);
                intent.putExtra("userID", user.getUserID());
                startActivity(intent);
            }
        });

        Bundle bundle = getIntent().getExtras();//클릭시 intent로 온 UserID 받음
        if(bundle != null){
            ID = bundle.getString("userID");
        }
    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_home:
                    Intent main_page = new Intent(MainActivity.this,MainActivity.class);
                    main_page.putExtra("userID",id);
                    startActivity(main_page);
                    break;
                case R.id.action_MyTour:
                    Intent chat_page = new Intent(MainActivity.this,Register.class);
                    chat_page.putExtra("userID",id);
                    startActivity(chat_page);
                    break;

                case R.id.action_Messenger:
                    Intent msg_page = new Intent(MainActivity.this,ChatList.class);
                    msg_page.putExtra("userID",id);
                    startActivity(msg_page);
                    break;
                case R.id.action_setting:
                    Intent setting_page = new Intent(MainActivity.this,ChatList.class);
                    setting_page.putExtra("userID",id);
                    startActivity(setting_page);
                    break;
            }
            return false;
        }

    };
    public void initDB() {//데이터베스 연결
        table = FirebaseDatabase.getInstance().getReference("users");
        //makeData();
        table.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userList.clear();
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    //들어온 메세지들을 일단 다 받겠다
                    User user = data.getValue(User.class);
                    //이형태로 데이터를 만들어서 넘겨준다.
                    userList.add(user);
                }
                adapter.notifyDataSetChanged();//데이터변경알림
                listView.setSelection(adapter.getCount()-1);
                //리스트 뷰의 셀렉트를 현재 선택된 것 중 가장 마지막에 보여줌.
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
    class MyAdapter extends ArrayAdapter<User> { //arrayList담을 MyAdapter 클래스 상속
        ArrayList<User> arrayList;

        public MyAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<User> objects) {
            super(context, resource, objects);
            arrayList = objects;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View v = convertView;
            if (v == null) { //convertView가 null일 경우에만 새로 만들기.
                LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inflater.inflate(R.layout.row, parent, false);
            }

            // TextView에 Message 출력하는 기능 구현
            User user = arrayList.get(position);
            //현재 행에 해당하는 chat 정보를 가져옴, position
            TextView userName = (TextView) v.findViewById(R.id.userName);
            TextView userID = (TextView) v.findViewById(R.id.userID);
            TextView language = (TextView) v.findViewById(R.id.language);
            userName.setText(user.getUserName());//매핑작업(메시지, ID, 시간순으로 보여줌)
            userID.setText(user.getUserID());
            language.setText(user.getUserLanguage());
            return v;
        }
    }