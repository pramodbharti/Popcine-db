package com.example.pramod.popcine.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Fade;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pramod.popcine.BuildConfig;
import com.example.pramod.popcine.R;
import com.example.pramod.popcine.adapter.PopcineAdapter;
import com.example.pramod.popcine.model.Popcine;
import com.example.pramod.popcine.model.PopcineResponse;
import com.example.pramod.popcine.network.PopcineApiClient;
import com.example.pramod.popcine.network.PopcineApiInterface;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PopcineMainActivity extends AppCompatActivity {

    private PopcineResponse popcineResponse;
    private List<Popcine> popcineList;
    private PopcineAdapter popcineAdapter;

    @BindView(R.id.popcine_recyclerview)
    RecyclerView recyclerView;

    @BindView(R.id.placeholder_logo)
    ImageView placeholder_logo;

    @BindView(R.id.no_internet)
    TextView no_internet;

    private final static String apiKey = BuildConfig.API_KEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_popcine);
        ButterKnife.bind(this);

        popcineList = new ArrayList<>();

        Fade fade = new Fade();
        View decor = getWindow().getDecorView();
        fade.excludeTarget(decor.findViewById(R.id.action_bar_container),true);
        fade.excludeTarget(android.R.id.statusBarBackground,true);
        fade.excludeTarget(android.R.id.navigationBarBackground,true);

        getWindow().setEnterTransition(fade);
        getWindow().setExitTransition(fade);

        //Loads popular movies on the launch of app
        loadPopularMovies();

        int spanCount = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 4 : 2;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), spanCount);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gridLayoutManager);
        popcineAdapter = new PopcineAdapter(popcineList, this, PopcineMainActivity.this);
        recyclerView.setAdapter(popcineAdapter);

    }


    //This method will check whether the API KEY is empty or not
    public boolean checkApiKey() {
        if (apiKey.isEmpty()) {
            no_internet.setText(R.string.insert_api);
            return false;
        } else {
            return true;
        }
    }

    // This method will check if there internet connectivity is available or not.
    public boolean checkInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            placeholder_logo.setVisibility(View.GONE);
            no_internet.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            return true;
        } else {
            placeholder_logo.setVisibility(View.VISIBLE);
            no_internet.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            return false;
        }

    }

    //This method will load Popular movies
    public void loadPopularMovies() {
        if (checkApiKey()) {
            if (checkInternet()) {
                setTitle(getResources().getString(R.string.popular_movies));
                PopcineApiInterface popcineApiInterface = PopcineApiClient.getClient().
                        create(PopcineApiInterface.class);
                Call<PopcineResponse> popcineResponseCall = popcineApiInterface.getPopularMovies(apiKey);
                makeApiRequest(popcineResponseCall);
            }
        }
    }

    //This method will load top rated movies
    public void loadTopRatedMovies() {
        if (checkApiKey()) {
            if (checkInternet()) {
                setTitle(getResources().getString(R.string.top_rated_movies));
                PopcineApiInterface popcineApiInterface = PopcineApiClient.getClient().
                        create(PopcineApiInterface.class);
                Call<PopcineResponse> popcineResponseCall = popcineApiInterface.getTopRatedMovies(apiKey);
                makeApiRequest(popcineResponseCall);
            }
        }
    }


    public void makeApiRequest(Call<PopcineResponse> call) {
        call.enqueue(new Callback<PopcineResponse>() {
            @Override
            public void onResponse(Call<PopcineResponse> call, Response<PopcineResponse> response) {
                popcineResponse = response.body();

                if (popcineList != null) {
                    popcineList.removeAll(popcineList);
                }

                if (popcineResponse != null) {
                    popcineList.addAll(popcineResponse.getResults());
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.data_not), Toast.LENGTH_LONG).show();
                }
                popcineAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<PopcineResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.failed_loading), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflating menu here
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.popular_movies:
                loadPopularMovies();
                return true;
            case R.id.toprated_movies:
                loadTopRatedMovies();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
