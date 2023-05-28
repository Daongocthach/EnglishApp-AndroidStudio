package com.group15.finalprojectenglishapp.luyennghe;

import android.annotation.SuppressLint;
import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;
import com.group15.finalprojectenglishapp.R;

import java.util.ArrayList;

public class ListeningAdapter extends RecyclerView.Adapter<ListeningAdapter.ListenViewHolder> {
    private ArrayList<String> strings;
    private Context context;
    private TextToSpeech tts;
    private Translator translatorVietnam;
    private Boolean isTranslated = false;
    private Boolean isRepeating = false;

    public ListeningAdapter(ArrayList<String> strings, Context context, TextToSpeech tts) {
        this.strings = strings;
        this.context = context;
        this.tts = tts;
    }


    @NonNull
    @Override
    public ListenViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listening_item, parent, false);
        Translate();
        return new ListenViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListenViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String string = strings.get(position);
        holder.text.setText(string);
        holder.listen.setOnClickListener(new View.OnClickListener() {
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
        holder.dich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isTranslated) {
                    translatorVietnam.translate(string)
                            .addOnSuccessListener(new OnSuccessListener<String>() {
                                @Override
                                public void onSuccess(String s) {
                                    holder.text.setText(s);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    holder.text.setText(e.toString());
                                }
                            });
                    isTranslated = true;
                }else {
                    holder.text.setText(string);
                    isTranslated = false;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (strings != null) {
            return strings.size();
        }
        return 0;
    }

    public class ListenViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout listenLayout;
        private TextView text;
        private ImageView dich;
        private ImageView listen;

        public ListenViewHolder(@NonNull View itemView) {
            super(itemView);
            listenLayout = itemView.findViewById(R.id.listen_item);
            text = itemView.findViewById(R.id.text);
            dich = itemView.findViewById(R.id.dich);
            listen = itemView.findViewById(R.id.listen);

        }
    }

    public void Translate() {
        TranslatorOptions options =
                new TranslatorOptions.Builder()
                        .setSourceLanguage(TranslateLanguage.ENGLISH)
                        .setTargetLanguage(TranslateLanguage.VIETNAMESE)
                        .build();

        translatorVietnam = Translation.getClient(options);
        DownloadConditions conditions = new DownloadConditions.Builder()
                .requireWifi()
                .build();
        translatorVietnam.downloadModelIfNeeded(conditions);
    }
}