package com.example.ash.d_couple;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DesignerBoard extends AppCompatActivity {

    //상수 정의
    private static final String TAG_RESULTS = "result";
    private static final String TAG_image = "Profile_image";
    private static final String TAG_ID = "UserID";
    private static final String TAG_Area = "Area1";

    String myJSON;
    JSONArray peoples = null;
    ArrayList<DesignerData> mDatas;
    LinearLayoutManager mLayoutManager;
    RecyclerViewAdapter adapter;
    public RequestManager mGlideRequestManager;

    //xml묶기
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.addBtn)
    Button buttonAdd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_designer_board);
        ButterKnife.bind(this);

        //각 item의 크기가 일정할 경우 고정
        recyclerView.setHasFixedSize(true);

        // layoutManager 설정
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);

        mDatas = new ArrayList<DesignerData>();
        getData("http://dcouple.dothome.co.kr/po_list.php");  //디비 연동
        // mDatas = dbadd();

    }
    //다시 본 액티비티로 돌아왔을 때
    //아직 구현이 덜 된 부분
   /*
    @Override
    protected void onRestart() {
        super.onRestart();
        mDatas.clear();
        mDatas=dbadd();
        adapter.seta
    }*/

    //등록 버튼을 눌렀을 경우
    @OnClick(R.id.addBtn)
    public void registerCleaner() {
        Intent intent = new Intent(getApplicationContext(), RegistDesigner.class);
        startActivity(intent);
    }

    //해당 item을 클릭 하였을 경우
    //클릭시 상세화면으로 넘어가기
    //추가 구현 사항
    public View.OnClickListener clickEvent = new View.OnClickListener() {
        public void onClick(View v) {

            // int itemPosition = recyclerView.getChildPosition(v);
            //String temp = mDatas.get(itemPosition).ID;
            //Intent intent = new Intent(getApplicationContext(),DetailCleanerActivity.class);
            //intent.putExtra("cleanerID",temp);
            //startActivity(intent);
        }
    };

    //디비에서 받아 리스트에 뿌려주는 메소드
    protected void showList() {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            peoples = jsonObj.getJSONArray(TAG_RESULTS);
            //반복문을 이용해 ArrayList에 담는다.
            for (int i = 0; i < peoples.length(); i++) {
                JSONObject c = peoples.getJSONObject(i);
                String imageURL = c.getString(TAG_image);
                String ID = c.getString(TAG_ID);
                String Area = c.getString(TAG_Area);

                DesignerData designerData = new DesignerData();
                designerData.setImageURL(imageURL);
                designerData.setID(ID);
                designerData.setInterest(Area);
                mDatas.add(designerData);
            }
            mGlideRequestManager = Glide.with(this);  //glide요청을 위한 파라메터 생성
            adapter = new RecyclerViewAdapter(mDatas, clickEvent, mGlideRequestManager); //어뎁터 생성
        } catch (JSONException e) {
            e.printStackTrace();
        }
        recyclerView.setAdapter(adapter); //어뎁터 연결
    }

    //해당 주소와 연결하여 데이터 베이스 접근
    public void getData(String url) {
        class GetDataJSON extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {

                String uri = params[0];

                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json;
                    //웹페이지 읽기
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }

                    return sb.toString().trim();

                } catch (Exception e) {
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String result) {
                myJSON = result;  //웹페이지 Json파일로 읽어 결과 전달
                showList();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(url);
    }
    //DBManager를 이용해 구현하려 했던 코드
    //오류가 생격 다른코드로 바꾸었다.
    /*
    public  ArrayList<DesignerData> dbadd(){
        ArrayList<DesignerData> tempArray = new ArrayList<DesignerData>();
        DesignerData tempItemData = new DesignerData();
        DBManager dbManager = new DBManager();
        dbManager.db_connect("http://dcouple.dothome.co.kr/po_list.php");
        do{
            tempItemData.setImageURL(dbManager.getResult("Profile_image"));
            tempItemData.setID(dbManager.getResult("UserID"));
            tempItemData.setInterest(dbManager.getResult("Area1"));
            tempArray.add(tempItemData);
        }while(dbManager.setNextData());

        return tempArray;
    }*/
}


