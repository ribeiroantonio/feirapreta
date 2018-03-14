package br.com.feirapreta.activities;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
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

public class MainActivity extends AppCompatActivity{

    private RecyclerView recyclerView;
    private ArrayList<Post> highlights;
    private RecyclerView.Adapter highlightsAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private EditText editTextSearch;
    private String searchedText;
    private Menu optionsMenu;

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

    @Override
    public void onAttachedToWindow() {
        openOptionsMenu();
    }

    private void initViews(){
        editTextSearch = findViewById(R.id.editText_search);
        recyclerView = findViewById(R.id.recyclerview);

        editTextSearch.setText("");
        /*editTextSearch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if(motionEvent.getRawX() >= (editTextSearch.getRight() - editTextSearch.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        Toast.makeText(MainActivity.this, "OPEN MENU", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                }
                return false;
            }
        });*/

        loadSearchBar();
        loadRVHighLights();
        loadHighlights();

        swipeRefreshLayout = findViewById(R.id.swipeRefreshHomeScreen);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadHighlights();
            }
        });
    }

    private void loadHighlights(){
        
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

                    if(response.code() == 200){
                        highlights = response.body();
                        if(highlights != null){
                            highlightsAdapter = new HighlightsAdapter(highlights);
                            recyclerView.setAdapter(highlightsAdapter);
                            if(highlights.isEmpty()){
                                Toast.makeText(MainActivity.this, "Não há destaques cadastrados", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }else{
                        Toast.makeText(MainActivity.this, "Houve um erro em nossos servidores, estamos tentando resolver esse problema", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<ArrayList<Post>> call, Throwable t) {
                    Log.e("TAG", "" + t.getCause());
                    if(t.getMessage() != null && t.getMessage().contains("Expected BEGIN_ARRAY")){
                        Toast.makeText(MainActivity.this, "Desculpe, não há destaques cadastrados", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(MainActivity.this, R.string.server_error_message, Toast.LENGTH_SHORT).show();
                    }

                }
            });

        }else{
            Toast.makeText(this, R.string.connection_error_message, Toast.LENGTH_SHORT).show();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, optionsMenu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.settings_menu_item:
                Intent settings = new Intent(MainActivity.this, PreferenceActivity.class);
                startActivity(settings);
                return true;
            case R.id.about_menu_item:
                Toast.makeText(this, "SOBRE", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
