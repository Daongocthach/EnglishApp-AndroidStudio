package com.group15.finalprojectenglishapp.ui.home;

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
import com.group15.finalprojectenglishapp.R;
import com.group15.finalprojectenglishapp.hoctuvung.TuVung;
import java.util.ArrayList;

public class SearchTuVungAdapter extends RecyclerView.Adapter<SearchTuVungAdapter.TuVungViewHolder> implements Filterable {
    private ArrayList<TuVung> listTuVung;
    private ArrayList<TuVung> listTuVungFull;
    private Context context;
    private IClickItemTuVung iClickItemTuVung;
    public SearchTuVungAdapter(ArrayList<TuVung> listTuVung, Context context, IClickItemTuVung iClickItemTuVung) {
        this.listTuVung = listTuVung;
        this.context = context;
        this.iClickItemTuVung = iClickItemTuVung;
        listTuVungFull = new ArrayList<>(listTuVung);
    }

    @NonNull
    @Override
    public TuVungViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item, parent, false);
        return new TuVungViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TuVungViewHolder holder, @SuppressLint("RecyclerView") int position) {
        TuVung TuVung = listTuVung.get(position);
        holder.TuVung.setText(TuVung.getDapan());
        holder.TuVung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickItemTuVung.onClickItemTuVung(TuVung);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (listTuVung != null) {
            return listTuVung.size();
        }
        return 0;
    }

    public class TuVungViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout TuVungItem;
        private TextView TuVung;

        public TuVungViewHolder(@NonNull View itemView) {
            super(itemView);
            TuVungItem = itemView.findViewById(R.id.word_item);
            TuVung = itemView.findViewById(R.id.tv_word);
        }
    }
    @Override
    public Filter getFilter() {
        return TuVungFilter;
    }
    private Filter TuVungFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<TuVung> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(listTuVungFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (TuVung item : listTuVungFull) {
                    if (item.getDapan().toLowerCase().contains(filterPattern)) {
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
            listTuVung.clear();
            listTuVung.addAll((ArrayList) results.values);
            notifyDataSetChanged();
        }
    };

}