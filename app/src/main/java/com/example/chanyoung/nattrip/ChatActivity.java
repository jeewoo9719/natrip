package com.example.chanyoung.nattrip;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ChatActivity extends AppCompatActivity {

    SimpleDateFormat mSimpleDateFormat;
    EditText userid;
    EditText message;
    MyAdapter adapter;
    ListView msgListView;
    ArrayList<ChatMsg> msgList;
    String userName;
    String nameSearch;
    DatabaseReference table; //데이터베이스 레퍼런스 객체 선언

    String messageDB;
    String ID;
    String chatSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Intent intent = getIntent();
        nameSearch = (String) intent.getStringExtra("data");//추가
        ID = (String) intent.getStringExtra("userID");//추가
        chatSearch = (String) intent.getStringExtra("chatSearch");//추가

        init();
    }

    public void init() { //초기화작업
        message = (EditText) findViewById(R.id.message);
        msgListView = (ListView) findViewById(R.id.msgListView);
        msgList = new ArrayList<>();
        adapter = new MyAdapter(this, R.layout.chat_row, msgList);
        msgListView.setAdapter(adapter);
        mSimpleDateFormat = new SimpleDateFormat("a h:mm", Locale.getDefault());
        table = FirebaseDatabase.getInstance().getReference("users");
        userName=ID;//오전오후 몇시 몇분, 포맷을 맞춰주는 정의함수
        initDB();
        Bundle bundle = getIntent().getExtras();//클릭시 intent로 온 UserID 받음

    }

    public void initDB() {//데이터베스 연결
        //if (userName.compareTo(nameSearch) < 0) {//대화 상대에 따라 다른 데이터 베이스 생성
        //    messageDB = userName + nameSearch;
        //} else {
        //    messageDB = nameSearch + userName;
        //}

        table = FirebaseDatabase.getInstance().getReference("messageDB").child(chatSearch);
        table.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                msgList.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    //들어온 메세지들을 일단 다 받겠다
                    ChatMsg chatMsg = data.getValue(ChatMsg.class);
                    //이형태로 데이터를 만들어서 넘겨준다.
                    msgList.add(chatMsg);//리스트에 추가하는것
                }
                adapter.notifyDataSetChanged();//데이터변경알림
                msgListView.setSelection(adapter.getCount() - 1);
                //리스트 뷰의 셀렉트를 현재 선택된 것 중 가장 마지막에 보여줌.
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void btnSend(View view) {
        String msg = message.getText().toString();
        String time = mSimpleDateFormat.format(System.currentTimeMillis());
        //DB에 저장 구문
        ChatMsg chatMsg = new ChatMsg(userName, msg, time);
        table.push().setValue(chatMsg);
        //자동으로 아이디가 부여되는, 아이디안만들고 걍 만들때 사용하는 함수
        message.setText("");
    }

    //public void btnSet(View view) { //set버튼 눌렀을때 수행하는 작업
        //userName = ((TextView) findViewById(R.id.userName)).getText().toString();
        //((LinearLayout) findViewById(R.id.userContainer)).setVisibility(View.GONE);
        //레이아웃 컨테이너에, 위치를 없애는 함수 GONE사용
    //}

    public class MyAdapter extends ArrayAdapter<ChatMsg> { //arrayList담을 MyAdapter 클래스 상속
        ArrayList<ChatMsg> arrayList;

        public MyAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<ChatMsg> objects) {
            super(context, resource, objects);
            arrayList = objects;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View v = convertView;
            if (v == null) { //convertView가 null일 경우에만 새로 만들기.
                LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inflater.inflate(R.layout.chat_row, parent, false);
            }
            //position 0~listsize() 까지 증가?
            //if (nameSearch.equals(arrayList.get(position).getUserName()) ||
            //        userName.equals(arrayList.get(position).getUserName())) {

            // TextView에 Message 출력하는 기능 구현
            ChatMsg chatMsg = arrayList.get(position);
            //현재 행에 해당하는 chat 정보를 가져옴, position
            TextView msgText = (TextView) v.findViewById(R.id.msg);
            TextView msgID = (TextView) v.findViewById(R.id.msgID);
            TextView msgTime = (TextView) v.findViewById(R.id.msgTime);
            msgText.setText(chatMsg.getMessage());//매핑작업(메시지, ID, 시간순으로 보여줌)
            msgID.setText(chatMsg.getUserName());
            msgTime.setText(chatMsg.getTime());

            LinearLayout msgContainer = (LinearLayout) v.findViewById(R.id.msgContainers);

            if (userName.equals(chatMsg.getUserName())) {
                msgContainer.setGravity(Gravity.RIGHT);//ID가 같으면 오른쪽
                msgText.setBackgroundResource(R.drawable.ic_action_name);
            } else {
                msgContainer.setGravity(Gravity.LEFT);//ID가 다르면 왼쪽
                msgText.setBackgroundResource(R.drawable.ic_action_name);
            }
            //  }
            return v;

        }
    }
}
