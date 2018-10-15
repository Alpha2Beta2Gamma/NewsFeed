package com.example.newsfeed.newsassignment;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.newsfeed.newsassignment.model.Content;
import com.example.newsfeed.newsassignment.model.Result;
import com.example.newsfeed.newsassignment.model.Result;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements RecyclerViewItemClickListener {

    private static final int NEWS_ITEM = 0;
    public static final String TAG = "RecyclerViewAdapter";

    private List<Result> newsResult;
    private Context context;

    private RecyclerViewItemClickListener itemClickListener;

    public RecyclerViewAdapter(Context context, RecyclerViewItemClickListener itemClickListener) {
        this.context = context;
        this.itemClickListener = itemClickListener;
        newsResult = new ArrayList<>();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        viewHolder = getViewHolder(parent, inflater);

        return viewHolder;
    }

    @NonNull
    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        RecyclerView.ViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.list_row, parent, false);
        viewHolder = new NewsViewHolder(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        try {
            Result result = newsResult.get(position); // Movie

            final NewsViewHolder newsViewHolder = (NewsViewHolder) holder;

            Content content = result.getContent();

            newsViewHolder.mnewsTitle.setText(content.getTitle());


            /**
             * Using Glide to handle image loading.
             * GLide will automaticaly cache necessary sized images.
             */
            Glide
                    .with(context)
                    .load(content.getImages().get(0).getOriginalUrl())
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            // TODO: 08/11/16 handle failure
                            newsViewHolder.mProgress.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            // image ready, hide progress now
                            newsViewHolder.mProgress.setVisibility(View.GONE);
                            return false;   // return false if you want Glide to handle everything else.
                        }
                    })
                    .diskCacheStrategy(DiskCacheStrategy.ALL)   // cache both original & resized image
                    .centerCrop()
                    .crossFade()
                    .into(newsViewHolder.mnewsImg);

        }catch (Exception e){

            Log.e(TAG, "Error while parsing response.");
        }
    }

    @Override
    public int getItemCount() {
        return newsResult == null ? 0 : newsResult.size();
    }

    @Override
    public int getItemViewType(int position) {
        return NEWS_ITEM;
    }


    public void addAll(List<Result> newsResults) {

        newsResult.addAll(newsResults);
    }

    private void remove(Result r) {
        int position = newsResult.indexOf(r);
        if (position > -1) {
            newsResult.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public Result getItem(int position) {
        return newsResult.get(position);
    }


    protected class NewsViewHolder extends RecyclerView.ViewHolder {
        private TextView mnewsTitle;
        private ImageView mnewsImg;
        private ProgressBar mProgress;

        public NewsViewHolder(View itemView) {
            super(itemView);

            mnewsTitle = (TextView) itemView.findViewById(R.id.news_title);
            mnewsImg = (ImageView) itemView.findViewById(R.id.news_image);
            mProgress = (ProgressBar) itemView.findViewById(R.id.movie_progress);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClickListener.onItemClick(view, getAdapterPosition());
                }
            });
        }
    }

    @Override
    public void onItemClick(View view, int position) {

    }


}
