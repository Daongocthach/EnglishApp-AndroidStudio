package com.group15.finalprojectenglishapp.ui.home;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.group15.finalprojectenglishapp.Adapter.WordAdapter;
import com.group15.finalprojectenglishapp.Interface.IClickItemWord;
import com.group15.finalprojectenglishapp.Model.Word;
import com.group15.finalprojectenglishapp.R;
import com.group15.finalprojectenglishapp.dienkhuyet.DienKhuyetActivity;
import com.group15.finalprojectenglishapp.hoctuvung.HocTuVungActivity;
import com.group15.finalprojectenglishapp.luyennghetoeic.ListeningActivity;
import com.group15.finalprojectenglishapp.luyennghetoeic.LuyenNgheActivity;
import com.group15.finalprojectenglishapp.luyennoi.SpeakingActivity;
import com.group15.finalprojectenglishapp.sapxepcau.SapXepCauActivity;
import com.group15.finalprojectenglishapp.taikhoan.RankingActivity;
import com.group15.finalprojectenglishapp.tracnghiem.TracNghiemActivity;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    CardView cardViewHocTuVung, cardViewTracNghiem, cardViewSapXepCau, cardViewLuyenNghe,cardViewDienKhuyet,cardViewXepHang, cardViewLuyenNoi, cardViewListening ;
    final  String DATABASE_NAME = "HocNgonNgu.db";
    SQLiteDatabase database;
    private ArrayList<Word> listWord;
    WordAdapter wordAdapter;
    RecyclerView recyclerView;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        cardViewHocTuVung = root.findViewById(R.id.cardViewHocTuVung);
        cardViewDienKhuyet= root.findViewById(R.id.cardViewDienKhuyet);
        cardViewTracNghiem= root.findViewById(R.id.cardViewTracNghiem);
        cardViewSapXepCau = root.findViewById(R.id.cardViewSapXepCau);
        cardViewLuyenNghe = root.findViewById(R.id.cardViewLuyenNghe);
        cardViewLuyenNoi = root.findViewById(R.id.cardViewLuyenNoi);
        cardViewXepHang = root.findViewById(R.id.cardViewXepHang);
        cardViewListening = root.findViewById(R.id.cardViewListening);
        recyclerView = root.findViewById(R.id.recycleView_word);
        SearchView searchView = root.findViewById(R.id.search_bar);
        listWord = new ArrayList<>();
        listWord = getWordList();
        setUpRecyclerView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                wordAdapter.getFilter().filter(query);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    recyclerView.setVisibility(View.GONE);
                    return false;
                }
                recyclerView.setVisibility(View.VISIBLE);
                wordAdapter.getFilter().filter(newText);
                return false;
            }
        });
        cardViewHocTuVung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), HocTuVungActivity.class);
                startActivity(intent);
            }
        });
        cardViewDienKhuyet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), DienKhuyetActivity.class);
                startActivity(intent);
            }
        });
        cardViewTracNghiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), TracNghiemActivity.class);
                startActivity(intent);
            }
        });
        cardViewSapXepCau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), SapXepCauActivity.class);
                startActivity(intent);
            }
        });
        cardViewLuyenNghe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), LuyenNgheActivity.class);
                startActivity(intent);
            }
        });
        cardViewXepHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(getActivity(), RankingActivity.class);
                startActivity(intent);
            }
        });
        cardViewListening.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(getActivity(), ListeningActivity.class);
                startActivity(intent);
            }
        });
        cardViewLuyenNoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(getActivity(), SpeakingActivity.class);
                startActivity(intent);
            }
        });
        return root;
    }
    private void openDialog(Word word) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.search_dialog);
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowAttribute = window.getAttributes();
        windowAttribute.gravity = Gravity.BOTTOM;
        window.setAttributes(windowAttribute);
        dialog.setCancelable(true);
        TextView wordName = dialog.findViewById(R.id.word_name);
        TextView wordMeaning = dialog.findViewById(R.id.word_meaning);
        TextView wordType = dialog.findViewById(R.id.word_type);

        if (word != null) {
            wordName.setText(word.getWord());
            wordType.setText("(" + word.getType() + ")" + ":");
            wordMeaning.setText(word.getMeaning());
        } else return;
        dialog.show();
    }

    public ArrayList<Word> getWordList() {
        listWord.add(new Word("apple", "noun", "Quả táo"));
        listWord.add(new Word("application", "noun", "Ứng dụng"));
        listWord.add(new Word("blue", "noun", "Màu xanh biển"));
        listWord.add(new Word("bad", "adjective", "Tệ, xấu"));
        return listWord;
    }

    private void setUpRecyclerView() {
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        wordAdapter = new WordAdapter(listWord, getActivity(), new IClickItemWord() {
            @Override
            public void onClickItemWord(Word word) {
                openDialog(word);
            }
        });
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(wordAdapter);
    }
}