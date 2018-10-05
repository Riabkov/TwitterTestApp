package com.riabkov.android.twittertestapp.network;

import com.google.gson.annotations.SerializedName;
import com.riabkov.android.twittertestapp.database.TweetShort;

import java.util.List;

public class TweetsResponse {
    @SerializedName("statuses")
    private List<TweetShort> mTweets;

    public List<TweetShort> getTweets() {
        return mTweets;
    }

    public void setTweets(List<TweetShort> tweets) {
        mTweets = tweets;
    }
}
