package br.com.feirapreta.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;

import org.w3c.dom.Text;

import java.util.ArrayList;

import br.com.feirapreta.R;
import br.com.feirapreta.Utils.PaginationScrollListener;
import br.com.feirapreta.adapter.PostsAdapter;
import br.com.feirapreta.model.Post;
import br.com.feirapreta.model.RetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchResultsActivity extends AppCompatActivity {

    private ArrayList<Post> allPosts = new ArrayList<>();
    private boolean isDemand;
    private EditText editTextSearch;
    private String searchedText;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView emptyResults;
    private TextView noConnection;
    private TextView connectionError;
    private ImageView imageEmptyResults;
    private ImageView imageNoConnection;

    PostsAdapter adapter;
    GridLayoutManager gridLayoutManager;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    // Index from which pagination should start (0 is 1st page in our case)
    private static final int PAGE_START = 1;
    // Indicates if footer ProgressBar is shown (i.e. next page is loading)
    private boolean isLoading = false;
    // If current page is the last page (Pagination will stop after this page load)
    private boolean isLastPage = false;
    // total no. of pages to load. Initial load is page 0, after which 2 more pages will load.
    private int TOTAL_PAGES = 3;
    // indicates the current page which Pagination is fetching.
    private int currentPage = PAGE_START;
    // indicates the amount of items
    private int TOTAL_COUNT = 0;
    // indicates the amount of item to be displayed on each page
    private int AMOUNT_BY_PAGE = 18;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        isDemand = preferences.getBoolean("search_demand", false);
        Fresco.initialize(this);

        initViews();

    }

    @Override
    public void onBackPressed() {
        hideEmptyStates();
        super.onBackPressed();
    }

    protected void initViews() {
        emptyResults = findViewById(R.id.no_search_results);
        noConnection = findViewById(R.id.no_connection_search);
        connectionError = findViewById(R.id.server_error_search);
        imageNoConnection = findViewById(R.id.image_no_connection_search);
        imageEmptyResults = findViewById(R.id.image_no_result_search);

        hideEmptyStates();

        swipeRefreshLayout = findViewById(R.id.swipeRefreshSearchScreen);
        progressBar = findViewById(R.id.search_progress);


        editTextSearch = findViewById(R.id.editText_searchScreen);
        Bundle bundle = getIntent().getExtras();
        if (bundle.get("searchedText") != null) {
            searchedText = bundle.getString("searchedText");
            editTextSearch.setText(searchedText);
        }

        loadSearchBar();
        loadRV();

        swipeRefreshLayout.setColorSchemeColors(Color.RED);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                hideEmptyStates();
                progressBar.setVisibility(View.VISIBLE);
                currentPage = PAGE_START;
                isLoading = false;
                isLastPage = false;
                if(!isDemand){
                    adapter.clear();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loadRV();
                        }
                    }, 1000);
                }else{
                    loadRV();
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void hideEmptyStates(){
        emptyResults.setVisibility(View.GONE);
        noConnection.setVisibility(View.GONE);
        connectionError.setVisibility(View.GONE);
        imageNoConnection.setVisibility(View.GONE);
        imageEmptyResults.setVisibility(View.GONE);
    }

    private void loadSearchBar() {
        editTextSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    View view = getCurrentFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    progressBar.setVisibility(View.VISIBLE);
                    searchedText = editTextSearch.getText().toString();
                    if (getIntent().getStringExtra("searchedText") != null) {
                        getIntent().removeExtra("searchedText");
                        getIntent().putExtra("searchedText", searchedText);
                    }
                    currentPage = PAGE_START;
                    isLoading = false;
                    isLastPage = false;
                    loadRV();
                    return true;
                }
                return false;
            }
        });
    }

    private void loadRV() {
        recyclerView = findViewById(R.id.rvSearchResults);

        adapter = new PostsAdapter(this);

        gridLayoutManager = new GridLayoutManager(this, 3);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (adapter.getItemViewType(position)) {
                    case 0:
                        return 1;
                    case 1:
                        return 3;
                    default:
                        return -1;
                }
            }
        });
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        if (isDemand) {
            recyclerView.addOnScrollListener(new PaginationScrollListener(gridLayoutManager) {
                @Override
                protected void loadMoreItems() {
                    isLoading = true;
                    currentPage += 1;
                    Log.e("SCROLL", "OnScroll nextPage()");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loadNextPage();
                        }
                    }, 1000);
                }

                @Override
                public int getTotalPageCount() {
                    return TOTAL_PAGES;
                }

                @Override
                public boolean isLastPage() {
                    return isLastPage;
                }

                @Override
                public boolean isLoading() {
                    return isLoading;
                }
            });

            loadAllPosts();
        } else {
            loadAllPosts();
        }
    }

    private void loadAllPosts() {
        hideEmptyStates();
        if(isNetworkAvailable()) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(RetrofitService.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                RetrofitService request = retrofit.create(RetrofitService.class);
                Call<ArrayList<Post>> call = request.searchByTag(searchedText);
                call.enqueue(new Callback<ArrayList<Post>>() {
                    @Override
                    public void onResponse(Call<ArrayList<Post>> call, Response<ArrayList<Post>> response) {
                        if (response.code() == 200) {
                            allPosts = response.body();
                            if (isDemand) {
                                TOTAL_COUNT = allPosts.size();
                                TOTAL_PAGES = (TOTAL_COUNT + AMOUNT_BY_PAGE - 1) / AMOUNT_BY_PAGE;
                                loadFirstPage();
                            } else {
                                progressBar.setVisibility(View.GONE);
                                adapter.addAll(allPosts);
                                if(allPosts.isEmpty()){
                                    emptyResults.setVisibility(View.VISIBLE);
                                    imageEmptyResults.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Post>> call, Throwable t) {
                        Log.e("TAG", "" + t.getCause());
                        if(t.getMessage() != null && t.getMessage().contains("Expected BEGIN_ARRAY")){
                            emptyResults.setVisibility(View.VISIBLE);
                            imageEmptyResults.setVisibility(View.VISIBLE);
                        }else{
                            Toast.makeText(SearchResultsActivity.this, R.string.server_error_message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        }else {
            progressBar.setVisibility(View.GONE);
            noConnection.setVisibility(View.VISIBLE);
            imageNoConnection.setVisibility(View.VISIBLE);
            Toast.makeText(this, R.string.connection_error_message, Toast.LENGTH_SHORT).show();
        }

    }

    private void loadFirstPage() {
        ArrayList<Post> firstPage = new ArrayList<>();
        if (!allPosts.isEmpty()) {
            if (AMOUNT_BY_PAGE <= allPosts.size()) {
                int to = currentPage * AMOUNT_BY_PAGE;
                int from = to - AMOUNT_BY_PAGE;
                for (int i = from; i < to; i++) {
                    firstPage.add(allPosts.get(i));
                }

                progressBar.setVisibility(View.GONE);
                adapter.addAll(firstPage);

                if (currentPage <= TOTAL_PAGES && AMOUNT_BY_PAGE != allPosts.size()){
                    adapter.addLoadingFooter();
                } else{
                    isLastPage = true;
                }
            } else {
                int to = allPosts.size();
                int from = 0;
                for (int i = from; i < to; i++) {
                    firstPage.add(allPosts.get(i));
                }
                isLastPage = true;
                progressBar.setVisibility(View.GONE);
                adapter.addAll(firstPage);
            }

        } else {
            progressBar.setVisibility(View.GONE);
            emptyResults.setVisibility(View.VISIBLE);
            imageEmptyResults.setVisibility(View.VISIBLE);
            Toast.makeText(this, "Desculpe, não há resultados que correspondem a sua pesquisa", Toast.LENGTH_SHORT).show();
        }

    }

    private void loadNextPage() {

        if (!isLastPage) {

            int from = adapter.getItemCount();
            int to;
            if (allPosts.size() - from >= AMOUNT_BY_PAGE) {
                to = currentPage * AMOUNT_BY_PAGE;
            } else {
                to = allPosts.size();
            }
            ArrayList<Post> nextPage = new ArrayList<>();
            for (int i = from; i < to; i++) {
                nextPage.add(allPosts.get(i));
            }

            adapter.removeLoadingFooter();
            isLoading = false;

            adapter.addAll(nextPage);

            if (currentPage != TOTAL_PAGES){
                adapter.addLoadingFooter();
            } else {
                isLastPage = true;
            }

        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void goBack(View view) {
        hideEmptyStates();
        onBackPressed();
        finish();
    }

}