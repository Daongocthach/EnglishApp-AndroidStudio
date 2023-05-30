package com.group15.finalprojectenglishapp.admin.bohoctap;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;



import java.util.ArrayList;
import com.group15.finalprojectenglishapp.R;
import com.group15.finalprojectenglishapp.admin.AdminActivity;
import com.group15.finalprojectenglishapp.bohoctap.BoHocTap;
import com.group15.finalprojectenglishapp.database.Database;

public class AdminBoHocTapActivity extends AppCompatActivity {
    ArrayList<BoHocTap> listBHT;
    AdminBoHocTapAdapter adapter;
    ImageView imgBack, imgAdd;
    final  String DATABASE_NAME = "HocNgonNgu.db";
    SQLiteDatabase database;
    ListView listViewBHT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_bohoctap);
        listViewBHT = (ListView) findViewById(R.id.listviewAdminBHT);
        imgBack = (ImageView) findViewById(R.id.imgBackAdminBHT);
        imgAdd = (ImageView) findViewById(R.id.imgAddBHT);
        getBoHocTap();
        adapter = new AdminBoHocTapAdapter(AdminBoHocTapActivity.this, listBHT);
        listViewBHT.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminBoHocTapActivity.this, AdminActivity.class));
            }
        });
        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminBoHocTapActivity.this, AddBoHocTapActivity.class));
            }
        });
    }

    private void getBoHocTap() {
        database = Database.initDatabase(AdminBoHocTapActivity.this, DATABASE_NAME);
        listBHT = new ArrayList<>();
        listBHT.clear();
        Cursor cursor = database.rawQuery("SELECT * FROM BoCauHoi", null);
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            int idbo = cursor.getInt(0);
            int  stt = cursor.getInt(1);
            String tenbo = cursor.getString(2);
            listBHT.add(new BoHocTap(idbo, stt, tenbo));
        }
    }

}
