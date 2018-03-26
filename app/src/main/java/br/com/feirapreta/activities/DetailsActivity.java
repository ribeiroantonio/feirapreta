package br.com.feirapreta.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class DetailsActivity extends AppCompatActivity{

    private String postId;
    private String titlePost;
    private ImageView backButton;
    private SimpleDraweeView postImage;
    private ImageView instagramImage;
    private TextView postTitle;
    private TextView postCaption;
    private TextView postPersonName;
    private TextView postPersonUser;
    private TextView postpersonPhone;
    private Post post;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        bundle = getIntent().getExtras();
        getPostId();
        initViews();
        if(postId != null){
            loadPost();
        }
    }

    private void getPostId(){
        if (bundle.get("post_id") != null) {
            postId = bundle.getString("post_id");
        }
        if (bundle.get("post_title") != null) {
            titlePost = bundle.getString("post_title");
        }else{
            titlePost = "false";
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

        if(bundle.get("post") != null){
            post = (Post) bundle.getSerializable("post");
            loadDetails();
        }

    }

    private void loadDetails(){

        Log.e("TAG", "TESTE:" + post.getSubtitle().replace("?", ""));

        if(post.getSubtitle().replace("?", "").equals("")){
            postCaption.setHeight(0);
        }else{
            postCaption.setMaxLines(4);
        }

        if(titlePost.equals("false")){
            titlePost = ("Post - " + post.getPerson().getFullNameInstagram());
        }

        postImage.setImageURI(Uri.parse(post.getImageLowResolution()));
        postImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(newInstagramPostIntent(getPackageManager(), post.getLink()));
            }
        });
        postTitle.setText(titlePost);
        postCaption.setText(post.getSubtitle().replace("?", ""));
        postPersonName.setText(post.getPerson().getFullNameInstagram());
        postPersonUser.setText("@" + post.getPerson().getUsernameInstagram());
        postpersonPhone.setText(post.getPerson().getPhoneNumber());

        postPersonUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(newInstagramPostIntent(getPackageManager(), ("https://www.instagram.com/" + post.getPerson().getUsernameInstagram())));
            }
        });

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static Intent newInstagramPostIntent(PackageManager pm, String url){

        Intent intent = new Intent(Intent.ACTION_VIEW);

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

    public void goBackDetails(View view){
        onBackPressed();
        this.finish();
    }

    public void addContact(View view){
        String phone = "";
        String name = "";
        if(post.getPerson().getPhoneNumber() != null){
            phone = post.getPerson().getPhoneNumber();
        }

        if(post.getPerson().getFullNameInstagram() != null){
            name = post.getPerson().getFullNameInstagram();
        }

        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setType(ContactsContract.Contacts.CONTENT_TYPE);

        intent.putExtra(ContactsContract.Intents.Insert.NAME, name);
        intent.putExtra(ContactsContract.Intents.Insert.PHONE, phone);

        startActivity(intent);
    }

    /*if(isNetworkAvailable()){
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
        }*/
}
