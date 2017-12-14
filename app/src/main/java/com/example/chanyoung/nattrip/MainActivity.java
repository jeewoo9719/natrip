package com.example.chanyoung.nattrip;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    MyAdapter adapter;
    ListView listView;
    TextView placeNameView;
    ImageView placeImage;
    BottomNavigationView bottomNavigationView;
    ArrayList<Tour> tourList;
    DatabaseReference table; //데이터베이스 레퍼런스 객체 선언
    int startTourDateY, startTourDateM,startTourDateD;  //투어 시작일 받는 인자
    int endTourDateY, endTourDateM,endTourDateD;  //투어 종료일
    String ID = null;
    String tourPlace; //null로 바꾸기
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    public void init() {
        listView = (ListView) findViewById(R.id.listView);
        placeNameView = (TextView) findViewById(R.id.placeName);
        placeImage = (ImageView)findViewById(R.id.placeImage);
        tourList = new ArrayList<>();
        adapter = new MyAdapter(this, R.layout.row, tourList);
        listView.setAdapter(adapter);

        Bundle bundle = getIntent().getExtras();//클릭시 searchActivity에서 intent로 온 UserID, 지역 받음
        if(bundle != null){
            ID = bundle.getString("userID"); //search에서 가져온 ID
            tourPlace = bundle.getString("tourPlace");
            startTourDateY = bundle.getInt("stratTourDateY");
            startTourDateM = bundle.getInt("stratTourDateM");
            startTourDateD = bundle.getInt("stratTourDateD");
            endTourDateY = bundle.getInt("endTourDateY");
            endTourDateM = bundle.getInt("endTourDateM");
            endTourDateD = bundle.getInt("endTourDateD");
        }

        initDB();
        placeNameView.setText(tourPlace); //선택 지역 표시
        //placeImage.setImageDrawable(); storage에서 이미지 가져오기
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                Tour tour=(Tour)adapterView.getAdapter().getItem(pos);
                Intent tourActivity = new Intent(MainActivity.this, TourActivity.class);
                tourActivity.putExtra("userID",ID);
                tourActivity.putExtra("tourPlace",tourPlace);
                tourActivity.putExtra("guideID",tour.getGuideID());
                tourActivity.putExtra("regist","1");
                startActivity(tourActivity);

            }
        });


    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_home:
                    Intent main_page = new Intent(MainActivity.this,SearchActivity.class);
                    main_page.putExtra("userID",ID);
                    startActivity(main_page);
                    break;
                case R.id.action_MyTour:
                    Intent chat_page = new Intent(MainActivity.this,MainActivity.class);
                    chat_page.putExtra("userID",ID);
                    startActivity(chat_page);
                    break;

                case R.id.action_Messenger:
                    Intent msg_page = new Intent(MainActivity.this,ChatList.class);
                    msg_page.putExtra("userID",ID);
                    startActivity(msg_page);
                    break;
                case R.id.action_setting:
                    Intent setting_page = new Intent(MainActivity.this,SettingActivity.class);
                    setting_page.putExtra("userID",ID);
                    startActivity(setting_page);
                    break;
            }
            return false;
        }

    };
    public void initDB() {//데이터베스 연결
        table = FirebaseDatabase.getInstance().getReference("tours");
        table.child(tourPlace).addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tourList.clear();
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    //들어온 메세지들을 일단 다 받겠다
                    Tour tour = data.getValue(Tour.class);
                    //이형태로 데이터를 만들어서 넘겨준다.
                    tourList.add(tour);
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
    class MyAdapter extends ArrayAdapter<Tour> { //arrayList담을 MyAdapter 클래스 상속
        ArrayList<Tour> arrayList;

        public MyAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<Tour> objects) {
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
            Tour tour = arrayList.get(position);
            //현재 행에 해당하는 chat 정보를 가져옴, position
            TextView guideID = (TextView) v.findViewById(R.id.guideID);
            TextView tourName = (TextView) v.findViewById(R.id.tourName);
            TextView tourPlace = (TextView) v.findViewById(R.id.tourPlace);
            TextView tourDetail = (TextView) v.findViewById(R.id.tourDetail);
            ImageView tourimageView = (ImageView)v.findViewById(R.id.tourImage);

            //일단 투어 이미지 했는데 가이드 얼굴로 바꾸기
            Uri tourImageUri = Uri.parse(tour.getImage1FilePath().toString());
            Glide.with(v.getContext()).load(tourImageUri).into(tourimageView);

            guideID.setText(tour.getGuideID());//매핑작업(메시지, ID, 시간순으로 보여줌)
            tourName.setText(tour.getTourName());
            tourPlace.setText(tour.getplace());
            tourDetail.setText("");
            return v;
        }
    }