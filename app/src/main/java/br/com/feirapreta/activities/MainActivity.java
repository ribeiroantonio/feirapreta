package br.com.feirapreta.activities;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.ArrayList;
import java.util.Arrays;

import br.com.feirapreta.R;
import br.com.feirapreta.adapter.HighlightsAdapter;
import br.com.feirapreta.model.HighlightService;
import br.com.feirapreta.model.Person;
import br.com.feirapreta.model.Post;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<Post> highlights;
    private RecyclerView.Adapter highlightsAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;

    //TAG DO ERRO
    public static final String TAG = "DEU ERRO";

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

    private void initViews(){
        recyclerView = findViewById(R.id.recyclerview);

        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        highlights = new ArrayList<>();
        highlights.add(new Post("", "", true, "", "", "", "", new Person("", "carregando...", "", "")));
        highlights.add(highlights.get(0));
        highlights.add(highlights.get(0));
        highlights.add(highlights.get(0));
        highlights.add(highlights.get(0));
        highlights.add(highlights.get(0));
        highlightsAdapter = new HighlightsAdapter(highlights);
        recyclerView.setAdapter(highlightsAdapter);
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

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(HighlightService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        HighlightService request = retrofit.create(HighlightService.class);
        Call<ArrayList<Post>> call = request.listHighlight();
        call.enqueue(new Callback<ArrayList<Post>>() {
            @Override
            public void onResponse(Call<ArrayList<Post>> call, Response<ArrayList<Post>> response) {

                highlights = response.body();
                highlightsAdapter = new HighlightsAdapter(highlights);
                recyclerView.setAdapter(highlightsAdapter);
                swipeRefreshLayout.setRefreshing(false);

            }

            @Override
            public void onFailure(Call<ArrayList<Post>> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(MainActivity.this, "Houve um erro ao tentar se conectar com os nossos servidores!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
