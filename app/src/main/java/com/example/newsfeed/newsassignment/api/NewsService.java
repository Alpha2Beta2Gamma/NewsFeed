package com.example.newsfeed.newsassignment.api;

import com.example.newsfeed.newsassignment.model.News;
import com.example.newsfeed.newsassignment.model.News;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface NewsService {

    @GET("newsfeed.json")
    Call<News> getNewsItems();

    @GET
    Call<Response> getDetailedItem(@Url String url);

}
