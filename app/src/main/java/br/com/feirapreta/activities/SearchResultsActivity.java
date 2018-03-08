package br.com.feirapreta.activities;

import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.ArrayList;

import br.com.feirapreta.R;
import br.com.feirapreta.adapter.PostsAdapter;
import br.com.feirapreta.model.Post;

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
                    Toast.makeText(SearchResultsActivity.this, "TESTE", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        });
    }

    private void loadRVPosts(){
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);

        posts = new ArrayList<>();

        postsAdapter = new PostsAdapter(posts);
        recyclerView.setAdapter(postsAdapter);
    }

    private void loadPosts(){
        Post post = new Post("https://scontent.cdninstagram.com/vp/46262dc93aae3c922a74d88776f62760/5B30C520/t51.2885-15/e35/p320x320/24838741_383209202121068_6888092107773313024_n.jpg");
        for (int i = 0; i < 21; i++){
            posts.add(post);
        }
        if (swipeRefreshLayout != null){
            swipeRefreshLayout.setRefreshing(false);
        }
    }

}
