package com.example.newsfeed.newsassignment.app;

import android.app.Application;

import com.example.newsfeed.newsassignment.api.NewsService;

import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewsFeedApplication extends Application {

    private static NewsFeedApplication mInstance;
    static Cache cache;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
       cache  = new Cache(getCacheDir(), cacheSize);
    }

    public static synchronized NewsFeedApplication getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }


    private static Retrofit retrofit = null;
    private static Retrofit dynamicRetrofit = null;

    static int cacheSize = 10*1024*1024;


    public synchronized static Retrofit getClient() {

        if (retrofit == null) {

            OkHttpClient client = new OkHttpClient().newBuilder().cache(cache).
                    readTimeout(60, TimeUnit.SECONDS).writeTimeout(60, TimeUnit.SECONDS)
                    .build();
            retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .baseUrl("https://pubapps.github.io")
                    .build();
            retrofit.create(NewsService.class);
        }
        return retrofit;
    }
    
}
