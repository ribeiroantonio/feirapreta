package br.com.feirapreta.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import br.com.feirapreta.R;

/**
 * Created by WEB on 28/02/2018.
 */

public class HighlightsAdapter extends RecyclerView.Adapter<HighlightsAdapter.ViewHolder> {

    private String[] dataSet;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public View view;
        public TextView userIdTextView;

        public ViewHolder(View v) {
            super(v);
            userIdTextView = v.findViewById(R.id.card_highlights_userId);
            view = v;
        }
    }

    public HighlightsAdapter(String[] dataSet){
        this.dataSet = dataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_highlights, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String userId = dataSet[position];
        holder.userIdTextView.setText(userId);
    }

    @Override
    public int getItemCount() {
        return dataSet.length;
    }


}
