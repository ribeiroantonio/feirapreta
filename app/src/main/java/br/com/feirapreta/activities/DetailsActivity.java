package br.com.feirapreta.activities;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;

import br.com.feirapreta.R;
import br.com.feirapreta.model.Post;
import br.com.feirapreta.model.RetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailsActivity extends AppCompatActivity {

    private String postId;
    private String titlePost;
    private ImageView backButton;
    private SimpleDraweeView postImage;
    private TextView postTitle;
    private TextView postCaption;
    private TextView postPersonName;
    private TextView postPersonUser;
    private TextView postpersonPhone;
    private Post post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        getPostId();
        initViews();
        if(postId != null){
            loadPost();
        }
    }

    private void getPostId(){
        Bundle bundle = getIntent().getExtras();
        if (bundle.get("post_id") != null) {
            postId = bundle.getString("post_id");
        }
        if (bundle.get("post_title") != null) {
            titlePost = bundle.getString("post_id");
        }
    }

    private void initViews(){

        postImage = findViewById(R.id.details_post_image);
        postTitle = findViewById(R.id.details_post_title);
        postCaption = findViewById(R.id.details_post_caption);
        postPersonName = findViewById(R.id.details_user_name);
        postPersonUser = findViewById(R.id.details_user_nickname);
        postpersonPhone = findViewById(R.id.details_user_phone);
        backButton = findViewById(R.id.details_back_button);
        
    }

    private void loadPost(){
        if(isNetworkAvailable()){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(RetrofitService.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            RetrofitService request = retrofit.create(RetrofitService.class);
            Call<Post> call = request.readPost(postId);
            call.enqueue(new Callback<Post>() {
                @Override
                public void onResponse(Call<Post> call, Response<Post> response) {
                    Log.e("TAG", "foi");
                    Log.e("TAG", "foi: " + response.body().getPerson().getUsernameInstagram());
                    post = response.body();
                    Log.e("TAG", "" + post.getId());
                    loadDetails();
                }

                @Override
                public void onFailure(Call<Post> call, Throwable t) {
                    Log.e("TAG", "" + t.getMessage());
                    Log.e("TAG", "" + post.getId());
                }
            });
        }
    }

    private void loadDetails(){
        postImage.setImageURI(Uri.parse(post.getImageLowResolution()));
        postTitle.setText(titlePost);
        postCaption.setText(post.getSubtitle());
        postPersonName.setText(post.getPerson().getFullNameInstagram());
        postPersonUser.setText(post.getPerson().getUsernameInstagram());
        postpersonPhone.setText(post.getPerson().getPhoneNumber());
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
