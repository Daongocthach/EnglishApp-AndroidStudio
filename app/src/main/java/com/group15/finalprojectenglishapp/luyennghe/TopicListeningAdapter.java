package com.group15.finalprojectenglishapp.luyennghe;

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

import com.group15.finalprojectenglishapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TopicListeningAdapter extends RecyclerView.Adapter<TopicListeningAdapter.TopicViewHolder> {
    private ArrayList<Listening> listeningList;
    private IClickItemListening iClickItemListening;
    public TopicListeningAdapter(ArrayList<Listening> listeningList, IClickItemListening iClickItemListening) {
        this.listeningList = listeningList;
        this.iClickItemListening = iClickItemListening;
    }

    @NonNull
    @Override
    public TopicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.topic_item, parent,false);
        return new TopicViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull TopicViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Listening listening = listeningList.get(position);
        if(listening ==null){
            return;
        }
        holder.name.setText(listening.getTopic());
        Picasso.get()
                .load(listening.getImage())
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()
                .into(holder.image);

        holder.topicLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickItemListening.onClickItemTopicListening(listening);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(listeningList !=null){
            return listeningList.size();
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