package com.group15.finalprojectenglishapp.Adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.group15.finalprojectenglishapp.Interface.IClickItemSpeaking;
import com.group15.finalprojectenglishapp.Model.Speaking;
import com.group15.finalprojectenglishapp.R;


import java.util.ArrayList;

public class TopicSpeakingAdapter extends RecyclerView.Adapter<TopicSpeakingAdapter.TopicViewHolder> {
    private ArrayList<Speaking> speakingList;
    private IClickItemSpeaking iClickItemSpeaking;

    public TopicSpeakingAdapter(ArrayList<Speaking> speakingList, IClickItemSpeaking iClickItemSpeaking) {
        this.speakingList = speakingList;
        this.iClickItemSpeaking = iClickItemSpeaking;
    }

    @NonNull
    @Override
    public TopicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.topic_item, parent,false);
        return new TopicViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull TopicViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Speaking speaking = speakingList.get(position);
        holder.name.setText(speaking.getTopic());
        holder.image.setImageResource(speaking.getImage());

        holder.topicLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickItemSpeaking.onClickItemTopicSpeaking(speaking);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(speakingList !=null){
            return speakingList.size();
        }
        return 0;
    }

    public class TopicViewHolder extends RecyclerView.ViewHolder{
        private CardView topicLayout;
        private TextView name;
        private ImageView image;
        public TopicViewHolder(@NonNull View itemView) {
            super(itemView);
            topicLayout = itemView.findViewById(R.id.topic_item);
            name = itemView.findViewById(R.id.name_topic);
            image = itemView.findViewById(R.id.image_topic);
            image.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
        }
    }
}