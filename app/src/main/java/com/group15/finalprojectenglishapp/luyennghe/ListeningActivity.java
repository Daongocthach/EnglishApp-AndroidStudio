package com.group15.finalprojectenglishapp.luyennghe;

import android.graphics.Color;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.group15.finalprojectenglishapp.R;
import java.util.ArrayList;
import java.util.Locale;

public class ListeningActivity extends AppCompatActivity {
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
        recyclerViewTopic = findViewById(R.id.topic_recycleview);
        recyclerViewListening = findViewById (R.id.listen_recycleview);
        listeningList = getTopicList();
        TopicListeningAdapter topicListeningAdapter = new TopicListeningAdapter(listeningList, new IClickItemListening() {
            @Override
            public void onClickItemTopicListening(Listening listening) {
                recyclerViewTopic.setVisibility(View.GONE);
                topic.setVisibility(View.GONE);
                recyclerViewListening.setVisibility(View.VISIBLE);
                title.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageResource(listening.getImage());
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

    public ArrayList<Listening> getTopicList() {
        listeningList.add(new Listening("Inflationary", R.drawable.lamphat, "The demand-pull and cost-push inflation will put pressure on the country's efforts to control inflation amid surging demand and strengthening of the US dollar which yields increased import prices.\n" +
                "It will not be easy to keep inflation at 4.5% this year as targeted, economic expert Nguyen Bich Lam, former General Director of the General Statistics Office (GSO), has said.\n" +
                "\n" +
                "Inflationary pressure for Vietnam's economy in 2023 is \"huge\" and comes from many factors, he said in an interview with the Vietnam News Agency (VNA).\n" +
                "\n" +
                "The demand-pull and cost-push inflation will put pressure on the country's efforts to control inflation amid surging demand and strengthening of the US dollar which yields increased import prices.\n" +
                "\n" +
                "Demand for petrol and electricity – the two important commodities for production and consumption – will increase in 2023. The domestic electricity price has been kept unchanged for the past few years, while the price of coal and gas used in the production of electricity has increased, he said, noting that thermal and gas power account for a large proportion of the total generated electricity. Therefore, it is forecasted that the Government may raise the price of electricity this year"));
        listeningList.add(new Listening("War", R.drawable.chientranh, "US President Joe Biden will meet his Ukrainian counterpart at the G7 summit in Japan, a top advisor said Saturday. President Volodymyr Zelensky will join the global leaders after taking part in the Arab League summit in Saudi Arabia."));
        listeningList.add(new Listening("Food", R.drawable.luongthuc, "Canning your own tomatoes at home will bring garden-fresh flavor to all kinds of dishes year-round. Learn how to safely preserve them with our simple guide."));
        listeningList.add(new Listening("Weather", R.drawable.khihau, "According to the National Centre for Hydro- Metreologogical Forecasting (NCHMF), hot weather is set to scorch northern and northern-central localities on May 18, with the highest temperature ranging from 37 to 40 degrees Celsius."));
        listeningList.add(new Listening("Technology", R.drawable.congnghe, "According to the National Centre for Hydro- Metreologogical Forecasting (NCHMF), hot weather is set to scorch northern and northern-central localities on May 18, with the highest temperature ranging from 37 to 40 degrees Celsius."));
        listeningList.add(new Listening("Security", R.drawable.anninh, "According to the National Centre for Hydro- Metreologogical Forecasting (NCHMF), hot weather is set to scorch northern and northern-central localities on May 18, with the highest temperature ranging from 37 to 40 degrees Celsius."));
        listeningList.add(new Listening("Economy", R.drawable.kinhte, "According to the National Centre for Hydro- Metreologogical Forecasting (NCHMF), hot weather is set to scorch northern and northern-central localities on May 18, with the highest temperature ranging from 37 to 40 degrees Celsius."));
        listeningList.add(new Listening("Education", R.drawable.hoctap, "According to the National Centre for Hydro- Metreologogical Forecasting (NCHMF), hot weather is set to scorch northern and northern-central localities on May 18, with the highest temperature ranging from 37 to 40 degrees Celsius."));
        return listeningList;
    }
}

