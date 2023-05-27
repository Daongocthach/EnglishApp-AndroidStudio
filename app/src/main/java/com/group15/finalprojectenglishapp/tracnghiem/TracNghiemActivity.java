package com.group15.finalprojectenglishapp.tracnghiem;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.group15.finalprojectenglishapp.MainActivity;
import com.group15.finalprojectenglishapp.R;
import com.group15.finalprojectenglishapp.bohoctap.BoHocTap;
import com.group15.finalprojectenglishapp.bohoctap.BoHocTapAdapter;
import com.group15.finalprojectenglishapp.database.Database;

import java.util.ArrayList;

public class TracNghiemActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<BoHocTap> boHocTapArrayList;
    BoHocTapAdapter boHocTapAdapter;
    ImageView imgback;
    final  String DATABASE_NAME = "HocNgonNgu.db";
    SQLiteDatabase database;
    int idbocauhoi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trac_nghiem);

        listView= findViewById(R.id.lvtracnghiem);
        imgback = findViewById(R.id.imgVBackTN);
        boHocTapArrayList =new ArrayList<>();
        AddArrayBTN();
        boHocTapAdapter =new BoHocTapAdapter(TracNghiemActivity.this,R.layout.row_botracnghiem, boHocTapArrayList);
        listView.setAdapter(boHocTapAdapter);
        boHocTapAdapter.notifyDataSetChanged();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                database= Database.initDatabase(TracNghiemActivity.this,DATABASE_NAME);
                String a=null;
                Cursor cursor=database.rawQuery("SELECT * FROM BoCauHoi",null);
                for(int i=position;i<cursor.getCount();i++){
                    cursor.moveToPosition(i);
                    int idbo=cursor.getInt(0);
                    int stt=cursor.getInt(1);
                    String tenbo=cursor.getString(2);
                    a=tenbo;
                    idbocauhoi=idbo;
                    break;
                }
                Intent quiz= new Intent(TracNghiemActivity.this,QuizActivity.class);
                quiz.putExtra("Bo",idbocauhoi);

                startActivity(quiz);
            }
        });

        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent
                        = new Intent(TracNghiemActivity.this,
                        MainActivity.class);
                intent.putExtras(getIntent());
                startActivity(intent);
                finish();
            }
        });
    }
    private void AddArrayBTN(){
        database= Database.initDatabase(TracNghiemActivity.this,DATABASE_NAME);
        Cursor cursor=database.rawQuery("SELECT * FROM BoCauHoi",null);
        boHocTapArrayList.clear();
        for (int i = 0; i < cursor.getCount(); i++){
            cursor.moveToPosition(i);
            int idbo = cursor.getInt(0);
            int  stt = cursor.getInt(1);
            String tenbo = cursor.getString(2);
            boHocTapArrayList.add(new BoHocTap(idbo,stt,tenbo));
        }
    }
}