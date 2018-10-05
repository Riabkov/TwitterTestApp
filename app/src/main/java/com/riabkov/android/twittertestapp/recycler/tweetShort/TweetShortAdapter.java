package com.riabkov.android.twittertestapp.recycler.tweetShort;

import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.riabkov.android.twittertestapp.R;
import com.riabkov.android.twittertestapp.database.TweetShort;
import com.twitter.sdk.android.core.models.Tweet;

import java.util.ArrayList;
import java.util.List;

public class TweetShortAdapter extends RecyclerView.Adapter<TweetShortHolder> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;

    private List<TweetShort> mTweets = new ArrayList<>();
    private boolean isLoadingAdded = false;
    private TweetShortHolder.OnClickableEntityClicked mOnClickableEntityClicked;

    public TweetShortAdapter(TweetShortHolder.OnClickableEntityClicked entityClicked){
        mOnClickableEntityClicked = entityClicked;
    }

    @NonNull
    @Override
    public TweetShortHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tweet, parent, false);
        return new TweetShortHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull TweetShortHolder holder, int position) {
        holder.bindTweet(mTweets.get(position), mOnClickableEntityClicked);
    }

    @Override
    public int getItemCount() {
        return mTweets.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == mTweets.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
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
