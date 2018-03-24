package br.com.feirapreta.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import br.com.feirapreta.R;
import br.com.feirapreta.activities.DetailsActivity;
import br.com.feirapreta.model.Post;

/**
 * Created by WEB on 07/03/2018.
 */

public class PostsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    protected static final int ITEM = 0;
    protected static final int LOADING = 1;

    private static Intent intent = new Intent(Intent.ACTION_VIEW);
    private ArrayList<Post> posts;
    private Context context;

    private boolean isLoadingAdded = false;

    public PostsAdapter(Context context) {
        this.context = context;
        posts = new ArrayList<>();
    }

    public ArrayList<Post> getPosts(){
        return posts;
    }

    public void setPosts(ArrayList<Post> posts){
        this.posts = posts;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType){
            case ITEM:
                viewHolder = getViewHolder(parent, inflater);
                break;
            case LOADING:
                View v2 = inflater.inflate(R.layout.item_loading, parent, false);
                viewHolder = new LoadingVH(v2);
                break;
        }
        return viewHolder;
    }

    @NonNull
    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater){
        RecyclerView.ViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.card_post, parent, false);
        viewHolder = new PostVH(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Post post = posts.get(position);

        switch (getItemViewType(position)){
            case ITEM:
                PostVH postVH = (PostVH) holder;

                if(posts.get(position).getImageLowResolution() != null){
                    Uri image = Uri.parse(posts.get(position).getImageLowResolution());
                    postVH.imageView.setImageURI(image);
                }

                break;
            case LOADING:
                //Do nothing
                break;
        }
    }

    @Override
    public int getItemCount() {
        return posts == null ? 0 : posts.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == posts.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    /*
    Helpers
     */

    public void add(Post pc){
        posts.add(pc);
        notifyItemInserted(posts.size() - 1);
    }

    public void addAll(ArrayList<Post> pcList){
        for(Post pc: pcList){
            add(pc);
        }
    }

    public void remove(Post post){
        int position = posts.indexOf(post);
        if(position > -1){
            posts.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear(){
        isLoadingAdded = false;
        while (getItemCount() > 0){
            remove(getItem(0));
        }
    }

    public boolean isEmpty(){
        return getItemCount() == 0;
    }

    public void addLoadingFooter(){
        isLoadingAdded = true;
        add(new Post());
    }

    public void removeLoadingFooter(){
        isLoadingAdded = false;

        int position = posts.size() - 1;
        Post item = getItem(position);

        if(item != null){
            posts.remove(position);
            notifyItemRemoved(position);
        }
    }

    public Post getItem(int position){
        return posts.get(position);
    }

    /*
    View Holders
     */

    protected class PostVH extends RecyclerView.ViewHolder{
        private SimpleDraweeView imageView;

        public PostVH(final View itemView) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.card_post_image);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent details = new Intent(v.getContext(), DetailsActivity.class);
                    details.putExtra("post_id", posts.get(getAdapterPosition()).getId());
                    details.putExtra("post", posts.get(getAdapterPosition()));
                    v.getContext().startActivity(details);
                }
            });
        }
    }

    protected class LoadingVH extends RecyclerView.ViewHolder{

        public LoadingVH(View itemView) {
            super(itemView);
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
