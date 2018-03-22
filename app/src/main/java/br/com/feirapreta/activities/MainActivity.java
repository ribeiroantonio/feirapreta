package br.com.feirapreta.activities;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.ArrayList;

import br.com.feirapreta.R;
import br.com.feirapreta.adapter.HighlightsAdapter;
import br.com.feirapreta.model.RetrofitService;
import br.com.feirapreta.model.Post;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener{

    private RecyclerView recyclerView;
    private ArrayList<Post> highlights;
    private RecyclerView.Adapter highlightsAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private EditText editTextSearch;
    private String searchedText;
    private ProgressBar progressBar;
    private TextView emptyState;
    private TextView noConnection;
    private TextView connectionError;

    //TOKEN AUTENTICAÇÂO
    private static final String TOKEN = "OTOKENFICAAQUI";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Fresco.initialize(this);

        initViews();

        //SharedPreferences preferences = getSharedPreferences(getString(R.string.token), 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        editTextSearch.setText("");
        loadHighlights();
    }

    private void initViews(){
        progressBar = findViewById(R.id.progressbar_main);

        emptyState = findViewById(R.id.no_highlights);
        connectionError = findViewById(R.id.server_error_main);
        noConnection = findViewById(R.id.no_connection_main);

        hideEmptyStates();

        editTextSearch = findViewById(R.id.editText_search);
        recyclerView = findViewById(R.id.recyclerview);

        editTextSearch.setText("");
        editTextSearch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (editTextSearch.getRight() - editTextSearch.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        showMenu(v);

                        return true;
                    }
                }
                return false;
            }
        });

        loadSearchBar();
        loadRVHighLights();

        swipeRefreshLayout = findViewById(R.id.swipeRefreshHomeScreen);
        swipeRefreshLayout.setColorSchemeColors(Color.RED);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                progressBar.setVisibility(View.VISIBLE);
                hideEmptyStates();
                loadRVHighLights();
                loadHighlights();
            }
        });
    }

    private void hideEmptyStates(){
        emptyState.setVisibility(View.GONE);
        connectionError.setVisibility(View.GONE);
        noConnection.setVisibility(View.GONE);
    }

    private void loadHighlights(){
        hideEmptyStates();
        if(isNetworkAvailable()){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(RetrofitService.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            RetrofitService request = retrofit.create(RetrofitService.class);
            Call<ArrayList<Post>> call = request.listHighlight();
            call.enqueue(new Callback<ArrayList<Post>>() {
                @Override
                public void onResponse(Call<ArrayList<Post>> call, Response<ArrayList<Post>> response) {
                    progressBar.setVisibility(View.GONE);
                    if(response.code() == 200){
                        highlights = response.body();
                        if(highlights != null){
                            highlightsAdapter = new HighlightsAdapter(highlights);
                            recyclerView.setAdapter(highlightsAdapter);
                            if(highlights.isEmpty()){
                                emptyState.setVisibility(View.VISIBLE);
                            }
                        }
                    }else{
                        connectionError.setVisibility(View.VISIBLE);
                    }

                }

                @Override
                public void onFailure(Call<ArrayList<Post>> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Log.e("TAG", "" + t.getCause());
                    if(t.getMessage() != null && t.getMessage().contains("Expected BEGIN_ARRAY")){
                        emptyState.setVisibility(View.VISIBLE);
                    }else{
                        connectionError.setVisibility(View.VISIBLE);
                    }

                }
            });

        }else{
            progressBar.setVisibility(View.GONE);
            noConnection.setVisibility(View.VISIBLE);
        }

        if(swipeRefreshLayout != null){
            swipeRefreshLayout.setRefreshing(false);
        }

        
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void loadSearchBar(){

        editTextSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    searchedText = editTextSearch.getText().toString();
                    Intent intent = new Intent(MainActivity.this, SearchResultsActivity.class);
                    intent.putExtra("searchedText", searchedText);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });
    }

    private void loadRVHighLights(){

        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);

        highlights = new ArrayList<>();

        highlightsAdapter = new HighlightsAdapter(highlights);
        recyclerView.setAdapter(highlightsAdapter);
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void showMenu(View view){

        PopupMenu menu = new PopupMenu(this, view, Gravity.RIGHT);

        menu.setOnMenuItemClickListener(this);
        menu.inflate(R.menu.actions_menu);
        menu.show();
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.settings_menu_item:
                Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(settingsIntent);
                return true;
            case R.id.about_menu_item:
                Intent aboutIntent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(aboutIntent);
                return true;
            default:
                return false;
        }
    }
}
