package com.riabkov.android.twittertestapp.repo;

import com.riabkov.android.twittertestapp.database.TweetShort;
import com.riabkov.android.twittertestapp.database.TweetsDatabase;
import com.riabkov.android.twittertestapp.network.TweetsResponse;
import com.riabkov.android.twittertestapp.network.TwitterApi;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;

import java.util.List;

import javax.inject.Inject;

public class TwitterRepository {

    private TwitterApi mTwitterApi;
    private TweetsDatabase mTweetsDatabase;

    public interface OnDataPassCallback{
        void passError(Exception e);
        void passData(List<TweetShort> data);
    }

    @Inject
    public TwitterRepository(TwitterApi twitterApi, TweetsDatabase tweetsDatabase) {
        mTwitterApi = twitterApi;
        mTweetsDatabase = tweetsDatabase;
    }

    public List<TweetShort> getFavoriteTweets(){

        return mTweetsDatabase.tweetsDao().getFavoriteTweets().getValue();
    }

    public void getTweets(OnDataPassCallback callback, String query, long max_id, long since_id){
        mTwitterApi.searchTweets(query, max_id, since_id, 25).enqueue(new Callback<TweetsResponse>() {
            @Override
            public void success(Result<TweetsResponse> result) {
                callback.passData(result.data.getTweets());
            }

            @Override
            public void failure(TwitterException exception) {
                exception.printStackTrace();
            }
        });
    }

    public void getNearbyTweets(OnDataPassCallback callback, String query, long latitude, long longitude, long max_id, long since_id){
        String geo = latitude+","+longitude+",10km";

        mTwitterApi.searchNearbyTweets(query, geo, max_id, since_id, 25).enqueue(new Callback<TweetsResponse>() {
            @Override
            public void success(Result<TweetsResponse> result) {
                callback.passData(result.data.getTweets());
            }

            @Override
            public void failure(TwitterException exception) {
                exception.printStackTrace();
                callback.passError(exception);
            }
        });
    }

}
