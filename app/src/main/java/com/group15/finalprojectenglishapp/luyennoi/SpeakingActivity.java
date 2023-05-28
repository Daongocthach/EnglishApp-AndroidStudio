package com.group15.finalprojectenglishapp.luyennoi;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.group15.finalprojectenglishapp.R;
import java.util.ArrayList;
import java.util.Locale;

public class SpeakingActivity extends AppCompatActivity {
    private ArrayList<Speaking> speakingList = new ArrayList<>();

    private String string;
    private TextToSpeech tts;
    private Boolean isRepeating = false;
    private Integer i = 0;
    private String speaker;
    private TextView paragraph;
    private ArrayList<String> arrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speaking);
        ImageView btn_micro = findViewById(R.id.image_micro);
        ImageView btn_listen = findViewById(R.id.image_listen);
        TextView title = findViewById(R.id.title);
        paragraph = findViewById(R.id.tv_paragraph);
        ImageView imageView = findViewById(R.id.image);
        TextView btn_continue = findViewById(R.id.tv_continue);
        TextView topic = findViewById(R.id.tv_topic);
        LinearLayout linearLayout = findViewById(R.id.linear2);
        RecyclerView recyclerViewTopic = findViewById(R.id.topic_recycleview);
        speakingList = getTopicList();

        TopicSpeakingAdapter topicSpeakingAdapter = new TopicSpeakingAdapter(speakingList, new IClickItemSpeaking() {
            @Override
            public void onClickItemTopicSpeaking(Speaking speaking) {
                recyclerViewTopic.setVisibility(View.GONE);
                topic.setVisibility(View.GONE);
                title.setVisibility(View.VISIBLE);
                paragraph.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.VISIBLE);
                imageView.setImageResource(speaking.getImage());
                title.setText(speaking.getTopic());
                string = speaking.getSentence();
                arrayList = splitIntoParagraphs(string, 10);
                paragraph.setText(arrayList.get(i));
                i = i + 1;
            }
        });

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerViewTopic.setLayoutManager(gridLayoutManager);
        recyclerViewTopic.setFocusable(false);
        recyclerViewTopic.setNestedScrollingEnabled(false);
        recyclerViewTopic.setAdapter(topicSpeakingAdapter);

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.US);
                }
            }
        });


        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float similarity = calculateStringSimilarity(speaker, paragraph.getText().toString());
                if (similarity > 0.5) {
                    showAlertDialog("Correct Answer");
                    if(i < arrayList.size()) {
                        paragraph.setText(arrayList.get(i));
                        i = i + 1;
                    }
                } else {
                    showAlertDialog("Wrong Answer");
                    if(i < arrayList.size()) {
                        paragraph.setText(arrayList.get(i));
                        i = i + 1;
                    }
                }
            }
        });

        btn_micro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speak();
            }
        });
        btn_listen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRepeating) {
                    tts.stop();
                    isRepeating = false;
                } else {
                    tts.speak(paragraph.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
                    isRepeating = true;
                }
            }
        });
    }
    private void showAlertDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Xử lý khi người dùng nhấn nút OK
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void speak() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "start speaking");
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            speaker = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0).toString();
        }
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
    private static float calculateStringSimilarity(String string1, String string2) {
        int distance = calculateLevenshteinDistance(string1, string2);
        int maxLength = Math.max(string1.length(), string2.length());

        return 1 - ((float) distance / maxLength);
    }

    private static int calculateLevenshteinDistance(String string1, String string2) {
        int[][] dp = new int[string1.length() + 1][string2.length() + 1];

        for (int i = 0; i <= string1.length(); i++) {
            dp[i][0] = i;
        }

        for (int j = 0; j <= string2.length(); j++) {
            dp[0][j] = j;
        }

        for (int i = 1; i <= string1.length(); i++) {
            for (int j = 1; j <= string2.length(); j++) {
                if (string1.charAt(i - 1) == string2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = 1 + Math.min(dp[i - 1][j - 1], Math.min(dp[i][j - 1], dp[i - 1][j]));
                }
            }
        }

        return dp[string1.length()][string2.length()];
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
    }

    public ArrayList<Speaking> getTopicList() {
        speakingList.add(new Speaking("Inflationary", R.drawable.lamphat, "The demand-pull and cost-push inflation will put pressure on the country's efforts to control inflation amid surging demand and strengthening of the US dollar which yields increased import prices.\n" +
                "It will not be easy to keep inflation at 4.5% this year as targeted, economic expert Nguyen Bich Lam, former General Director of the General Statistics Office (GSO), has said.\n" +
                "\n" +
                "Inflationary pressure for Vietnam's economy in 2023 is \"huge\" and comes from many factors, he said in an interview with the Vietnam News Agency (VNA).\n" +
                "\n" +
                "The demand-pull and cost-push inflation will put pressure on the country's efforts to control inflation amid surging demand and strengthening of the US dollar which yields increased import prices.\n" +
                "\n" +
                "Demand for petrol and electricity – the two important commodities for production and consumption – will increase in 2023. The domestic electricity price has been kept unchanged for the past few years, while the price of coal and gas used in the production of electricity has increased, he said, noting that thermal and gas power account for a large proportion of the total generated electricity. Therefore, it is forecasted that the Government may raise the price of electricity this year"));
        speakingList.add(new Speaking("War", R.drawable.chientranh, "US President Joe Biden will meet his Ukrainian counterpart at the G7 summit in Japan, a top advisor said Saturday. President Volodymyr Zelensky will join the global leaders after taking part in the Arab League summit in Saudi Arabia."));
        speakingList.add(new Speaking("Food", R.drawable.luongthuc, "Canning your own tomatoes at home will bring garden-fresh flavor to all kinds of dishes year-round. Learn how to safely preserve them with our simple guide."));
        speakingList.add(new Speaking("Weather", R.drawable.khihau, "According to the National Centre for Hydro- Metreologogical Forecasting (NCHMF), hot weather is set to scorch northern and northern-central localities on May 18, with the highest temperature ranging from 37 to 40 degrees Celsius."));
        speakingList.add(new Speaking("Technology", R.drawable.congnghe, "According to the National Centre for Hydro- Metreologogical Forecasting (NCHMF), hot weather is set to scorch northern and northern-central localities on May 18, with the highest temperature ranging from 37 to 40 degrees Celsius."));
        speakingList.add(new Speaking("Security", R.drawable.anninh, "According to the National Centre for Hydro- Metreologogical Forecasting (NCHMF), hot weather is set to scorch northern and northern-central localities on May 18, with the highest temperature ranging from 37 to 40 degrees Celsius."));
        speakingList.add(new Speaking("Economy", R.drawable.kinhte, "According to the National Centre for Hydro- Metreologogical Forecasting (NCHMF), hot weather is set to scorch northern and northern-central localities on May 18, with the highest temperature ranging from 37 to 40 degrees Celsius."));
        speakingList.add(new Speaking("Education", R.drawable.hoctap, "Hello, How are you?"));
        return speakingList;
    }

}