package com.riabkov.android.twittertestapp.recycler.tweetShort;

import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.riabkov.android.twittertestapp.R;
import com.riabkov.android.twittertestapp.database.TweetShort;

import java.util.ArrayList;
import java.util.List;

public class TweetShortAdapter extends RecyclerView.Adapter<TweetShortHolder> {


    private List<TweetShort> mTweets = new ArrayList<>();

    private TweetShortHolder.OnClickableEntityClicked mOnClickableEntityClicked;
    private TweetShortHolder.OnFavoriteClicked mOnFavoriteClicked;

    public TweetShortAdapter(TweetShortHolder.OnClickableEntityClicked entityClicked, TweetShortHolder.OnFavoriteClicked onFavoriteClicked){
        mOnClickableEntityClicked = entityClicked;
        mOnFavoriteClicked = onFavoriteClicked;
    }

    @NonNull
    @Override
    public TweetShortHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tweet, parent, false);
        return new TweetShortHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull TweetShortHolder holder, int position) {
        holder.bindTweet(mTweets.get(position), mOnClickableEntityClicked, mOnFavoriteClicked);
    }

    @Override
    public int getItemCount() {
        return mTweets.size();
    }


    public void updateData(List<TweetShort> tweets){
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new TweetShortResultDiff(mTweets, tweets));
        mTweets.clear();
        mTweets.addAll(tweets);

        diffResult.dispatchUpdatesTo(this);
    }

    public void setData(List<TweetShort> tweets){
        mTweets.clear();
        mTweets.addAll(tweets);
        notifyDataSetChanged();
    }
}
