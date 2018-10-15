package com.example.newsfeed.newsassignment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.newsfeed.newsassignment.app.ConnectivityReceiver;
import com.example.newsfeed.newsassignment.app.NewsFeedApplication;
import com.example.newsfeed.newsassignment.model.News;
import com.example.newsfeed.newsassignment.model.Result;

import java.util.List;

import com.example.newsfeed.newsassignment.api.NewsService;
import com.example.newsfeed.newsassignment.app.ConnectivityReceiver;

import okhttp3.Cache;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * This is the main fragment that displays the news items in the list.
 */
public class MainFragment extends Fragment implements RecyclerViewItemClickListener, ConnectivityReceiver.ConnectivityReceiverListener {

    public static final String FRAGMENT_TAG = "MainFragment";

    RecyclerViewAdapter adapter;
    LinearLayoutManager linearLayoutManager;

    RecyclerView rv;
    ProgressBar progressBar;

    private List<Result> displayResults;

    /**
     * The below two flags will determine if the calls are finished or still in process . That way the failure cases can be handled.
     */
    private boolean finishedLoadingItems;
    private boolean isLoading;

    TextView connectivityStatus;


    /**
     * Time interval to re-query the response in case of a failure.
     */
    static final int RETRY_TIME = 120000; // 2 mins

    private NewsService newsService;

    /**
     * This listener is to communicate with the activity.
     */
    private showDetailsListener listener;


    interface showDetailsListener {

        void showDetails(Bundle arguments);

        void showTitle(boolean show);
    }

    @Override
    public void onAttach(Context context) {

        super.onAttach(context);
        listener = (showDetailsListener) (context);

    }

    private View thisView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new RecyclerViewAdapter(getActivity().getApplicationContext(), this);
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        newsService = NewsFeedApplication.getClient().create(NewsService.class);

        /**
         *Display results are checked here to handle screen rotations. Since the fragment is retained over screen rotation, these
         * variables will also be retained on screen rotation.
         */
        if (displayResults == null || displayResults.size() == 0) {

            finishedLoadingItems = false;
            loadNewsItems();

        }


    }


    @Override
    public void onResume() {
        super.onResume();

        if (!isLoading && !finishedLoadingItems) {

            loadNewsItems();

        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        thisView = getLayoutInflater().inflate(R.layout.fragment_main, container, false);

        rv = (RecyclerView) thisView.findViewById(R.id.main_recycler);
        progressBar = (ProgressBar) thisView.findViewById(R.id.main_progress);

        setRetainInstance(true);

        rv.setLayoutManager(linearLayoutManager);

        rv.setItemAnimator(new DefaultItemAnimator());

        rv.setAdapter(adapter);


        /**
         * Check for existing results because they will be still existing in case of a screen rotation(OnCreateView will be caled but
         * OnCreate will not be called)
         */
        if (displayResults == null || displayResults.size() == 0) {
            loadNewsItems();
        } else {

            adapter.addAll(displayResults);
        }

        getActivity().setTitle(getString(R.string.all_stories_title));
        listener.showTitle(true);
        connectivityStatus = thisView.findViewById(R.id.connectivity_status);

        if (finishedLoadingItems) {
            progressBar.setVisibility(View.GONE);
        }
        return thisView;
    }

    /**
     * Requery API response in case of failure.
     */
    private void retry()

    {

        if (isResumed()) {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Do something after 100ms
                    if (!isLoading && !finishedLoadingItems) {
                        loadNewsItems();
                    }
                }
            }, RETRY_TIME);

        }

    }

    /**
     * load Items from server.
     * TODO Improve my implemneting OKhttp cache as well as persisiten cache like database.
     *
     */
    private void loadNewsItems() {
        Log.d(FRAGMENT_TAG, "loadFirstPage: ");

        if (isNetworkAvailable()) {
            isLoading = true;
            newsService.getNewsItems().enqueue(new Callback<News>() {
                @Override
                public void onResponse(Call<News> call, Response<News> response) {
                    // Got data. Send it to adapter

                    if (response.isSuccessful()) {
                        if (response.code() == 200) {
                            List<Result> results = response.body().getItem().getResults();
                            progressBar.setVisibility(View.GONE);
                            displayResults = results;
                            adapter.clear();
                            adapter.addAll(displayResults);
                            adapter.notifyDataSetChanged();
                            finishedLoadingItems = true;
                        } else {


                            finishedLoadingItems = false;
                            retry();
                        }
                        isLoading = false;
                    }
                }

                @Override
                public void onFailure(Call<News> call, Throwable t) {
                    t.printStackTrace();
                    isLoading = false;
                    finishedLoadingItems = false;
                    retry();

                }
            });

        } else {

            if (connectivityStatus != null) {
                connectivityStatus.setVisibility(View.VISIBLE);
            }
            isLoading = false;
            finishedLoadingItems = false;
        }

    }


    @Override
    public void onPause() {

        super.onPause();

    }

    @Override
    public void onDetach() {

        adapter.clear();
        rv.setLayoutManager(null);

        rv.setItemAnimator(null);

        rv.setAdapter(null);

        super.onDetach();

    }

    @Override
    public void onDestroyOptionsMenu() {
        super.onDestroyOptionsMenu();
        adapter.clear();
        rv.setLayoutManager(null);

        rv.setItemAnimator(null);

        rv.setAdapter(null);
        adapter = null;
        rv = null;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /***
     * This takes care of the click events on recycler view items.
     * @param view
     * @param position
     */
    @Override
    public void onItemClick(View view, int position) {

        displayResults.get(position);
        Bundle args = new Bundle();
        args.putString(DetailedItemFragment.Url, displayResults.get(position).getContent().getUrl());
        listener.showDetails(args);
    }

    /**
     * The below APIs wil help detect network availability changes.
     * @return
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }



    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {

        try {


            connectivityStatus.setVisibility(View.GONE);
            if (isNetworkAvailable()) {
                if (!finishedLoadingItems && !isLoading) {
                    loadNewsItems();

                }
            } else {

                if (!finishedLoadingItems) {

                    connectivityStatus.setVisibility(View.VISIBLE);
                }
            }
        } catch (Exception e) {

        }
    }

}
