package br.com.feirapreta.adapter;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import br.com.feirapreta.R;
import br.com.feirapreta.activities.DetailsActivity;
import br.com.feirapreta.model.Post;

/**
 * Created by Antonio Ribeiro on 28/02/2018.
 */

public class HighlightsAdapter extends RecyclerView.Adapter<HighlightsAdapter.ViewHolder> {

    private static ArrayList<Post> highlights;
    private static Intent intent = new Intent(Intent.ACTION_VIEW);

    public HighlightsAdapter(ArrayList<Post> highlights){
        this.highlights = highlights;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_highlights, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String userId = "@" + highlights.get(position).getPerson().getUsernameInstagram();
        holder.userIdTextView.setText(userId);
        Uri image = Uri.parse(highlights.get(position).getImageLowResolution());
        holder.thumbImageView.setImageURI(image);
    }

    @Override
    public int getItemCount() {
        return highlights.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public View view;
        public TextView userIdTextView;
        SimpleDraweeView thumbImageView;

        public ViewHolder(View v) {
            super(v);
            userIdTextView = v.findViewById(R.id.card_highlights_userId);
            thumbImageView = v.findViewById(R.id.card_highlights_image);
            view = v;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent details = new Intent(view.getContext(), DetailsActivity.class);
                    details.putExtra("post_id", highlights.get(getAdapterPosition()).getId());
                    details.putExtra("post_title", "Destaque");
                    details.putExtra("post", highlights.get(getAdapterPosition()));
                    view.getContext().startActivity(details);
                }
            });
        }
    }

    public static Intent newInstagramPostIntent(PackageManager pm, String url){

        try{
            if(pm.getPackageInfo("com.instagram.android", 0) != null){
                if(url.endsWith("/")){
                    url = url.substring(0, url.length() - 1);
                }
                intent.setData(Uri.parse(url));
                intent.setPackage("com.instagram.android");
            }
            return intent;
        }catch(PackageManager.NameNotFoundException ignored){
        }
        intent.setData(Uri.parse(url));
        return intent;
    }

}
