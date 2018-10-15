package com.example.newsfeed.newsassignment.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class News {

    public Items getItem() {
        return item;
    }

    public void setItem(Items item) {
        this.item = item;
    }

    @SerializedName("items")
    @Expose
    private Items item;
}
