package com.example.newsfeed.newsassignment.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Image {

    @SerializedName("originalUrl")
    @Expose
    private String originalUrl;

    @SerializedName("originalWidth")
    @Expose
    private Float originalHeight;

    @SerializedName("originalHeight")
    @Expose
    private Float originalWidth;

    @SerializedName("resolutions")
    @Expose
    private List<Resolution> resolutions;



    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public Float getOriginalHeight() {
        return originalHeight;
    }

    public void setOriginalHeight(Float originalHeight) {
        this.originalHeight = originalHeight;
    }

    public Float getOriginalWidth() {
        return originalWidth;
    }

    public void setOriginalWidth(Float originalWidth) {
        this.originalWidth = originalWidth;
    }

    public List<Resolution> getResolutions() {
        return resolutions;
    }

    public void setResolutions(List<Resolution> resolutions) {
        this.resolutions = resolutions;
    }
}
