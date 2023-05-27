package com.group15.finalprojectenglishapp.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.group15.finalprojectenglishapp.Interface.IClickItemWord;
import com.group15.finalprojectenglishapp.Model.Word;
import com.group15.finalprojectenglishapp.R;


import java.util.ArrayList;

public class WordAdapter extends RecyclerView.Adapter<WordAdapter.WordViewHolder> implements Filterable {
    private ArrayList<Word> listWord;
    private ArrayList<Word> listWordFull;
    private Context context;
    private IClickItemWord iClickItemWord;
    public WordAdapter(ArrayList<Word> listWord, Context context, IClickItemWord iClickItemWord) {
        this.listWord = listWord;
        this.context = context;
        this.iClickItemWord = iClickItemWord;
        listWordFull = new ArrayList<>(listWord);
    }

    @NonNull
    @Override
    public WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item, parent, false);
        return new WordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WordViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Word word = listWord.get(position);
        holder.word.setText(word.getWord());
        holder.word.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickItemWord.onClickItemWord(word);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (listWord != null) {
            return listWord.size();
        }
        return 0;
    }

    public class WordViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout wordItem;
        private TextView word;

        public WordViewHolder(@NonNull View itemView) {
            super(itemView);
            wordItem = itemView.findViewById(R.id.word_item);
            word = itemView.findViewById(R.id.tv_word);
        }
    }
    @Override
    public Filter getFilter() {
        return wordFilter;
    }
    private Filter wordFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Word> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(listWordFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Word item : listWordFull) {
                    if (item.getWord().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            listWord.clear();
            listWord.addAll((ArrayList) results.values);
            notifyDataSetChanged();
        }
    };

}