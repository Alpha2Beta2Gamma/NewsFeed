package com.example.newsfeed.newsassignment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class DetailedItemFragment extends Fragment {

    public static final String FRAGMENT_TAG = "DetailedItemFragment";
    private View thisView;
    public static final String Url = "URI";
    private WebView wv;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        thisView = getLayoutInflater().inflate(R.layout.detailed_view, container, false);
        String url = getArguments().getString("URI");

        //Call<Response> res = NewsRestAPI.getClient().create(NewsService.class).getDetailedItem(url);
        wv = (WebView) thisView.findViewById(R.id.detailed_web_view);

        wv.setWebViewClient(new MyBrowser());
        wv.getSettings().setLoadsImagesAutomatically(true);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        if (url != null) {
            wv.loadUrl(url);
        }
        return thisView;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private static class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        wv.clearHistory();
        wv.clearFormData();
        wv = null;

    }
}
