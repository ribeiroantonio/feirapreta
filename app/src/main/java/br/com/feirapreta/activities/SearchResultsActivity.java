package br.com.feirapreta.activities;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import br.com.feirapreta.R;
import br.com.feirapreta.adapter.PostsAdapter;
import br.com.feirapreta.model.Post;
import br.com.feirapreta.model.RetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchResultsActivity extends AppCompatActivity {

    private ArrayList<Post> posts;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter postsAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private EditText editTextSearch;
    private String searchedText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        Fresco.initialize(this);

        initViews();

    }

    private void initViews(){
        editTextSearch = findViewById(R.id.editText_searchScreen);
        Bundle bundle = getIntent().getExtras();
        if(bundle.get("searchedText") != null){
            searchedText = bundle.getString("searchedText");
            editTextSearch.setText(searchedText);
        }

        recyclerView = findViewById(R.id.rvSearchResults);
        
        loadSearchBar();
        loadRVPosts();
        loadPosts();

        swipeRefreshLayout = findViewById(R.id.swipeRefreshSearchScreen);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadPosts();
            }
        });

    }
    
    private void loadSearchBar(){
        editTextSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    searchedText = editTextSearch.getText().toString();
                    loadPosts();
                    return true;
                }
                return false;
            }
        });
    }

    private void loadRVPosts(){
        recyclerView.setHasFixedSize(true);

        layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);

        posts = new ArrayList<>();

        postsAdapter = new PostsAdapter(posts);
        recyclerView.setAdapter(postsAdapter);
    }

    private void loadPosts(){
        /*final Post post = new Post("https://scontent.cdninstagram.com/vp/46262dc93aae3c922a74d88776f62760/5B30C520/t51.2885-15/e35/p320x320/24838741_383209202121068_6888092107773313024_n.jpg", "https://www.instagram.com/p/BcXJ7-whIi-/");
        Post post1 = new Post("https://scontent.cdninstagram.com/vp/e5c226b97d04c9421b3cc404f0570b7e/5B43A51A/t51.2885-15/e35/p320x320/24126586_192408241337439_1892377936535748608_n.jpg", "https://www.instagram.com/p/BcDlpH2BB5a/");
        Post post2 = new Post("https://scontent.cdninstagram.com/vp/6f9803b2e13763cc0e9b996b461ec37e/5B27940C/t51.2885-15/e35/p320x320/23823410_1488491477900574_1307807199350751232_n.jpg", "https://www.instagram.com/p/BbsgpryB0fv/");
        Random rand = new Random();
        posts.add(post);
        posts.add(post1);
        posts.add(post2);

        for (int i = 0; i < 18; i++){
            posts.add(posts.get(rand.nextInt(3 - 0) + 0));
        }*/

        if(isNetworkAvailable()){
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(RetrofitService.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            RetrofitService request = retrofit.create(RetrofitService.class);
            Call<ArrayList<Post>> call = request.searchByTag(searchedText);

            call.enqueue(new Callback<ArrayList<Post>>(){
                @Override
                public void onResponse(Call<ArrayList<Post>> call, Response<ArrayList<Post>> response) {

                    if (response.code() == 200){
                        posts = response.body();
                        if(posts != null){
                            postsAdapter = new PostsAdapter(posts);
                            recyclerView.setAdapter(postsAdapter);
                            if(posts.isEmpty()){
                                Toast.makeText(SearchResultsActivity.this, "Desculpe, não há resultados que correspondem a sua pesquisa", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }else{
                        Toast.makeText(SearchResultsActivity.this, "" + response.code(), Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<ArrayList<Post>> call, Throwable t) {
                    if(t.getMessage() != null && t.getMessage().contains("Expected BEGIN_ARRAY")){
                        Toast.makeText(SearchResultsActivity.this, "Desculpe, não há resultados para sua pesquisa", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(SearchResultsActivity.this, R.string.server_error_message, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else{
            Toast.makeText(this, R.string.connection_error_message, Toast.LENGTH_SHORT).show();
        }

        if (swipeRefreshLayout != null){
            swipeRefreshLayout.setRefreshing(false);
        }

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void goback(View view){
        onBackPressed();
        finish();
    }
}
