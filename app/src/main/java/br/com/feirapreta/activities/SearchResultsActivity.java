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

import java.util.ArrayList;
import java.util.List;

import br.com.feirapreta.R;
import br.com.feirapreta.Utils.PaginationScrollListener;
import br.com.feirapreta.adapter.PostsAdapter;
import br.com.feirapreta.model.Post;
import br.com.feirapreta.model.RetrofitService;
import br.com.feirapreta.model.search.PaginatedPosts;
import br.com.feirapreta.model.search.Result;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchResultsActivity extends AppCompatActivity {

    private boolean isDemand;
    private EditText editTextSearch;
    private String searchedText;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView emptyResults;
    private TextView noConnection;
    private TextView connectionError;
    private ImageView imageEmptyResults;
    private ImageView imageNoConnection;
    private ImageView imageServerError;

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
    private int TOTAL_PAGES;
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
        imageServerError = findViewById(R.id.image_server_error_search);

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
                adapter.clear();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadRV();
                    }
                }, 1000);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void hideEmptyStates() {
        emptyResults.setVisibility(View.GONE);
        noConnection.setVisibility(View.GONE);
        connectionError.setVisibility(View.GONE);
        imageNoConnection.setVisibility(View.GONE);
        imageEmptyResults.setVisibility(View.GONE);
        imageServerError.setVisibility(View.GONE);
    }

    private void loadSearchBar() {
        editTextSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(!editTextSearch.getText().toString().equals("")){
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
                        adapter.clear();
                        loadRV();
                        return true;
                    }
                }else {
                    View view = getCurrentFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                return false;
            }
        });
    }

    private ArrayList<Result> fetchResults(Response<PaginatedPosts> response) {
        PaginatedPosts paginatedPosts = response.body();
        return paginatedPosts != null ? paginatedPosts.getPublicacao().getResult() : new ArrayList<Result>();
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
            loadFirstPage();
        } else {

        }
    }

    private void loadFirstPage() {

        if (isNetworkAvailable()) {
            Retrofit retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(RetrofitService.BASE_URL)
                    .build();

            RetrofitService request = retrofit.create(RetrofitService.class);
            Call<PaginatedPosts> call = request.paginatedSearch(searchedText, currentPage);
            call.enqueue(new Callback<PaginatedPosts>() {
                @Override
                public void onResponse(Call<PaginatedPosts> call, Response<PaginatedPosts> response) {
                    TOTAL_PAGES = response.body().getPage().getTotalPages();
                    Log.e("TAG", "TOTALPAGES:" + TOTAL_PAGES);
                    ArrayList<Result> posts = fetchResults(response);
                    progressBar.setVisibility(View.GONE);
                    adapter.addAll(posts);

                    if (currentPage < TOTAL_PAGES) adapter.addLoadingFooter();
                    else isLastPage = true;
                }

                @Override
                public void onFailure(Call<PaginatedPosts> call, Throwable t) {

                }
            });
        } else {
            Toast.makeText(this, "erro1", Toast.LENGTH_SHORT).show();
        }

    }

    private void loadNextPage() {

        if (isNetworkAvailable()) {
            Retrofit retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(RetrofitService.BASE_URL)
                    .build();

            RetrofitService request = retrofit.create(RetrofitService.class);
            Call<PaginatedPosts> call = request.paginatedSearch(searchedText, currentPage);
            call.enqueue(new Callback<PaginatedPosts>() {
                @Override
                public void onResponse(Call<PaginatedPosts> call, Response<PaginatedPosts> response) {
                    adapter.removeLoadingFooter();
                    isLoading = false;

                    ArrayList<Result> posts = fetchResults(response);
                    adapter.addAll(posts);

                    if (currentPage < TOTAL_PAGES) adapter.addLoadingFooter();
                    else isLastPage = true;
                }

                @Override
                public void onFailure(Call<PaginatedPosts> call, Throwable t) {

                }
            });
        } else {
            Toast.makeText(this, "erro", Toast.LENGTH_SHORT).show();
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