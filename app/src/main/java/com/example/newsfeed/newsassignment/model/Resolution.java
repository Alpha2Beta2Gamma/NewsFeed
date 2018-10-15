package com.example.newsfeed.newsassignment.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Resolution {

    @SerializedName("url")
    @Expose
    private String url;

    @SerializedName("height")
    @Expose
    private Float height;

    @SerializedName("width")
    @Expose
    private Float width;

    @SerializedName("tag")
    @Expose
    private String tag;


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Float getHeight() {
        return height;
    }

    public void setHeight(Float height) {
        this.height = height;
    }

    public Float getWidth() {
        return width;
    }

    public void setWidth(Float width) {
        this.width = width;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

}
