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

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import br.com.feirapreta.R;
import br.com.feirapreta.activities.DetailsActivity;
import br.com.feirapreta.model.Post;
import br.com.feirapreta.model.search.Result;

/**
 * Created by WEB on 07/03/2018.
 */

public class PostsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    protected static final int ITEM = 0;
    protected static final int LOADING = 1;

    private static Intent intent = new Intent(Intent.ACTION_VIEW);
    private ArrayList<Result> postsResults;
    private Context context;

    private boolean isLoadingAdded = false;

    public PostsAdapter(Context context) {
        this.context = context;
        postsResults = new ArrayList<>();
    }

    public ArrayList<Result> getPostsResults(){
        return postsResults;
    }

    public void setPostsResults(ArrayList<Result> postsResults){
        this.postsResults = postsResults;
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
        Result post = postsResults.get(position);

        switch (getItemViewType(position)){
            case ITEM:
                PostVH postVH = (PostVH) holder;

                if(postsResults.get(position).getImageLowResolution() != null){
                    Uri image = Uri.parse(postsResults.get(position).getImageLowResolution());
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
        return postsResults == null ? 0 : postsResults.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == postsResults.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    /*
    Helpers
     */

    public void add(Result pc){
        postsResults.add(pc);
        notifyItemInserted(postsResults.size() - 1);
    }

    public void addAll(ArrayList<Result> pcList){
        for(Result pc: pcList){
            add(pc);
        }
    }

    public void remove(Result post){
        int position = postsResults.indexOf(post);
        if(position > -1){
            postsResults.remove(position);
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
        add(new Result());
    }

    public void removeLoadingFooter(){
        isLoadingAdded = false;

        int position = postsResults.size() - 1;
        Result item = getItem(position);

        if(item != null){
            postsResults.remove(position);
            notifyItemRemoved(position);
        }
    }

    public Result getItem(int position){
        return postsResults.get(position);
    }

    private Post fetchPost(Result result){
        Post post = new Post();
        if (result != null){
            if(result.getId() != null){
                post.setId(result.getId());
            }

            if(result.getLink() != null){
                post.setLink(result.getLink());
            }

            if(result.getImageLowResolution() != null){
                post.setImageLowResolution(result.getImageLowResolution());
            }

            if(result.getImageStandardResolution() != null){
                post.setImageStandardResolution(result.getImageStandardResolution());
            }

            if(result.getImageThumbnail() != null){
                post.setImageThumbnail(result.getImageThumbnail());
            }

            if(result.getPerson() != null){
                post.setPerson(result.getPerson());
            }

            if(result.getSubtitle() != null){
                post.setSubtitle(result.getSubtitle());
            }

            if (result.getIsHighlight() != null){
                post.setHighlight(result.getIsHighlight());
            }
        }
        return post;
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
                    details.putExtra("post_id", postsResults.get(getAdapterPosition()).getId());

                    Post post = fetchPost(postsResults.get(getAdapterPosition()));

                    details.putExtra("post", post);
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
