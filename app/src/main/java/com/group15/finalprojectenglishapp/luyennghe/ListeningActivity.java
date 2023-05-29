package com.group15.finalprojectenglishapp.luyennghe;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.group15.finalprojectenglishapp.R;
import com.group15.finalprojectenglishapp.database.Database;
import com.group15.finalprojectenglishapp.luyennoi.Speaking;
import com.group15.finalprojectenglishapp.luyennoi.SpeakingActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

public class ListeningActivity extends AppCompatActivity {
    final  String DATABASE_NAME = "HocNgonNgu.db";
    SQLiteDatabase database;
    private String string;
    private ListeningAdapter listeningAdapter;
    private RecyclerView recyclerViewListening;
    private TextToSpeech tts;
    private Boolean isRepeating = false;
    private ArrayList<Listening> listeningList = new ArrayList<>();
    private RecyclerView recyclerViewTopic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listening);
        ImageView btn_back = findViewById(R.id.toolbar_back);
        ImageView imageView = findViewById(R.id.image);
        ImageView imageViewplay = findViewById(R.id.imageView_play);
        ImageView imageRepeate = findViewById(R.id.image_repeate);
        TextView title = findViewById(R.id.title);
        TextView topic = findViewById(R.id.tv_topic);
        View viewSpace = findViewById(R.id.viewSpace);
        CoordinatorLayout  coordinatorLayout = findViewById(R.id.coordinator);
        recyclerViewTopic = findViewById(R.id.topic_recycleview);
        recyclerViewListening = findViewById (R.id.listen_recycleview);

        AddArrayTV();
        TopicListeningAdapter topicListeningAdapter = new TopicListeningAdapter(listeningList, new IClickItemListening() {
            @Override
            public void onClickItemTopicListening(Listening listening) {
                recyclerViewTopic.setVisibility(View.GONE);
                topic.setVisibility(View.GONE);
                recyclerViewListening.setVisibility(View.VISIBLE);
                title.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.VISIBLE);
                viewSpace.setVisibility(View.VISIBLE);
                coordinatorLayout.setVisibility(View.VISIBLE);
                Picasso.get()
                        .load(listening.getImage())
                        .placeholder(R.mipmap.ic_launcher)
                        .fit()
                        .centerCrop()
                        .into(imageView);
                title.setText(listening.getTopic());
                string = listening.getSentence();
                ArrayList<String> arrayList = splitIntoParagraphs(string, 30);
                listeningAdapter = new ListeningAdapter(arrayList, ListeningActivity.this, tts);
                LinearLayoutManager layoutManager = new LinearLayoutManager(ListeningActivity.this);
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerViewListening.setLayoutManager(layoutManager);
                recyclerViewListening.setFocusable(false);
                recyclerViewListening.setNestedScrollingEnabled(false);
                recyclerViewListening.setAdapter(listeningAdapter);
            }
        });
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerViewTopic.setLayoutManager(gridLayoutManager);
        recyclerViewTopic.setFocusable(false);
        recyclerViewTopic.setNestedScrollingEnabled(false);
        recyclerViewTopic.setAdapter(topicListeningAdapter);

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR){
                    tts.setLanguage(Locale.US);
                }
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListeningActivity.super.onBackPressed();
            }
        });
        imageViewplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRepeating) {
                    tts.stop();
                    isRepeating = false;
                } else {
                    tts.speak(string, TextToSpeech.QUEUE_FLUSH, null);
                    isRepeating = true;
                }
            }
        });
        imageRepeate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRepeating) {
                    // Đang trong trạng thái phát lại, dừng text-to-speech
                    tts.stop();
                    isRepeating = false;
                    imageRepeate.setBackgroundColor(Color.WHITE);
                }
                else{
                    // Không trong trạng thái phát lại, bắt đầu text-to-speech
                    tts.speak(string, TextToSpeech.QUEUE_FLUSH, null);
                    isRepeating = true;
                    imageRepeate.setBackgroundColor(Color.LTGRAY);
                }
            }
        });
    }

    private ArrayList<String> splitIntoParagraphs(String text, int wordsPerParagraph) {
        ArrayList<String> paragraphs = new ArrayList<>();
        String[] words = text.split("\\s");
        int totalWords = words.length;
        int totalParagraphs = (int) Math.ceil((double) totalWords / wordsPerParagraph);
        for (int i = 0; i < totalParagraphs; i++) {
            StringBuilder paragraph = new StringBuilder();

            int startIndex = i * wordsPerParagraph;
            int endIndex = Math.min(startIndex + wordsPerParagraph, totalWords);

            for (int j = startIndex; j < endIndex; j++) {
                paragraph.append(words[j]).append(" ");
            }

            paragraphs.add(paragraph.toString().trim());
        }

        return paragraphs;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
    }
    private void AddArrayTV(){
        database = Database.initDatabase(ListeningActivity.this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("SELECT * FROM LuyenNoi",null);
        listeningList.clear();

        for (int i = 0; i < cursor.getCount(); i++){
            cursor.moveToPosition(i);
            int id = cursor.getInt(0);
            String topic = cursor.getString(1);
            String image = cursor.getString(2);
            String sentence = cursor.getString(3);
            listeningList.add(new Listening(id,topic,image,sentence));
        }
    }


}

