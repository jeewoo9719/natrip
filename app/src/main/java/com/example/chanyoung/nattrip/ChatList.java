package com.example.chanyoung.nattrip;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChatList extends AppCompatActivity {

    ListView listView;
    DatabaseReference table;
    ArrayAdapter<String> adapter;
    String ID = null;
    int selectedID = 2131493114;

    String nameSearch[]=new String[10];//텍스트생성
    int count=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list2);
        Bundle bundle = getIntent().getExtras();//클릭시 intent로 온 UserID 받음
        if(bundle != null){
            ID = bundle.getString("userID");
        }
        init();
        initDB();
        initLV();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Menu menu = navigation.getMenu();
        for (int i = 0, size = menu.size(); i < size; i++) {
            MenuItem item = menu.getItem(i);
            item.setChecked(item.getItemId() == selectedID);
        }
    }
    public void init(){
        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);
        listView=(ListView)findViewById(R.id.listChat);

    }
    public void initDB(){
        table= FirebaseDatabase.getInstance().getReference("messageDB");
        table.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    String chatKey=data.getKey();
                    nameSearch[count]=chatKey;
                    count++;
                    chatKey = chatKey.replaceFirst(ID," ");
                    adapter.add(chatKey);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    public void initLV(){
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                intent.putExtra("data",nameSearch);
                intent.putExtra("userID",ID);
                intent.putExtra("chatSearch",nameSearch[i]);
                startActivity(intent);
            }
        });
    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_home:
                    Intent main_page = new Intent(ChatList.this,SearchActivity.class);
                    main_page.putExtra("userID",ID);
                    item.setChecked(false);
                    startActivity(main_page);
                    break;
                case R.id.action_MyTour:
                    Intent chat_page = new Intent(ChatList.this,register_list.class);
                    chat_page.putExtra("userID",ID);
                    item.setChecked(false);
                    startActivity(chat_page);
                    break;
                case R.id.action_Messenger:
                    Intent msg_page = new Intent(ChatList.this,ChatList.class);
                    msg_page.putExtra("userID",ID);
                    item.setChecked(false);
                    startActivity(msg_page);
                    break;
                case R.id.action_setting:
                    Intent setting_page = new Intent(ChatList.this,SettingActivity.class);
                    setting_page.putExtra("userID",ID);
                    item.setChecked(false);
                    startActivity(setting_page);
                    break;
            }
            return false;
        }

    };

}
