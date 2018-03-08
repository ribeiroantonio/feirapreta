package br.com.feirapreta.adapter;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import br.com.feirapreta.R;
import br.com.feirapreta.model.Post;

/**
 * Created by WEB on 07/03/2018.
 */

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.PostsViewHolder>{

    private static ArrayList<Post> posts;
    private static Intent intent = new Intent(Intent.ACTION_VIEW);

    public PostsAdapter(ArrayList<Post> posts) {
        this.posts = posts;
    }

    @Override
    public PostsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_post, parent, false);
        PostsViewHolder vh = new PostsViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(PostsViewHolder holder, int position) {
        Uri image = Uri.parse(posts.get(position).getImageLowResolution());
        holder.imageView.setImageURI(image);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public static class PostsViewHolder extends RecyclerView.ViewHolder{

        public View view;
        public SimpleDraweeView imageView;

        public PostsViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.card_post_image);

            view = itemView;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(view.getContext(), "REDIRECIONAR AO INSTAGRAM", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
