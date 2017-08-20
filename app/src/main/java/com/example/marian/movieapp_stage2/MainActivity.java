package com.example.marian.movieapp_stage2;

import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.marian.movieapp_stage2.adapter.FavoriteAdapter;
import com.example.marian.movieapp_stage2.adapter.MoviesAdapter;
import com.example.marian.movieapp_stage2.api.ApiClient;
import com.example.marian.movieapp_stage2.api.ApiInterface;
import com.example.marian.movieapp_stage2.model.Movie;
import com.example.marian.movieapp_stage2.model.MoviesResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.marian.movieapp_stage2.data.MoviesContract.CONTENT_URI;
/**
 * Created by  Marian on 3/10/2017.
 */


public class MainActivity extends AppCompatActivity
{

    private RecyclerView recyclerView;
    ProgressDialog pd;
    private SwipeRefreshLayout swipeContainer;
    Toolbar toolbar;
    RecyclerView.LayoutManager manager;

    public static final String LOG_TAG = MoviesAdapter.class.getSimpleName();
    private final static String API_KEY = BuildConfig.API_KEY;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.main_content);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh() {
                iniViews();
                Toast.makeText(MainActivity.this, "Movies Refreshed", Toast.LENGTH_SHORT).show();
            }
        });
        iniViews();
    }

    private void iniViews()
    {
        pd = new ProgressDialog(this);
        pd.setMessage("Loading Movies");
        pd.setCancelable(false);
        pd.show();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            manager =new GridLayoutManager(this,2);
            recyclerView.setLayoutManager(manager);

        }
        else
            {
                manager=new GridLayoutManager(this,4);
                recyclerView.setLayoutManager(manager);
            }

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        if (API_KEY.isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Please obtain your API KEY ", Toast.LENGTH_LONG).show();
            return;
        }

        LoadMostPopular();
    }

    private void LoadMostPopular() {

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<MoviesResponse> call = apiService.getMostPopularMovies(API_KEY);
        call.enqueue(new Callback<MoviesResponse>()
        {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response)
            {
                Toast.makeText(MainActivity.this, "loading JSON", Toast.LENGTH_SHORT).show();
                int statusCode = response.code();

                List<Movie> movies = response.body().getResults();
                Toast.makeText(MainActivity.this, " " + movies.get(0).getOriginalTitle(), Toast.LENGTH_SHORT).show();


                recyclerView.setAdapter(new MoviesAdapter(getApplicationContext(), movies));
                recyclerView.smoothScrollToPosition(0);
                if (swipeContainer.isRefreshing())
                {
                    swipeContainer.setRefreshing(false);
                }
                pd.dismiss();
            }


            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {

                Log.e("Error", t.toString());
                Toast.makeText(MainActivity.this, "Error Fetching Data", Toast.LENGTH_SHORT).show();
            }

        });


    }
    public void LoadTopRated() {

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<MoviesResponse> call= apiService.getTopRatedMovies(API_KEY);
        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response)
            {
                Toast.makeText(MainActivity.this,"loading JSON",Toast.LENGTH_SHORT).show();
                int statusCode = response.code();
                List<Movie>  movies = response.body().getResults();
                Toast.makeText(MainActivity.this," "+movies.get(0).getOriginalTitle(),Toast.LENGTH_SHORT).show();
                recyclerView.setAdapter(new MoviesAdapter( getApplicationContext(),movies));
                recyclerView.smoothScrollToPosition(0);
                if (swipeContainer.isRefreshing())
                {
                    swipeContainer.setRefreshing(false);
                }
                pd.dismiss();
            }


            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t)
            {
                Log.e("Error", t.toString());
                Toast.makeText(MainActivity.this, "Error Fetching Data", Toast.LENGTH_SHORT).show();
            }

        });

    }
    public void LoadFavoriteMovies()
    {

        Cursor cursor=getContentResolver().query(CONTENT_URI,
                null,
                null,
                null,
                null);


        recyclerView.setAdapter(new FavoriteAdapter( getApplicationContext(),cursor));
        recyclerView.smoothScrollToPosition(0);
        if (swipeContainer.isRefreshing())
        {
            swipeContainer.setRefreshing(false);
        }

        pd.dismiss();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.menu_popular:

                LoadMostPopular();
                break;
            case R.id.menu_top_rated:
                LoadTopRated();
                break;
            case R.id.menu_favorite:
                LoadFavoriteMovies();

                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        //CheckSortOrder(menu);

    }


}

