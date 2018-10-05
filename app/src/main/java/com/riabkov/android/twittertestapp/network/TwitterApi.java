package com.riabkov.android.twittertestapp.network;

import com.riabkov.android.twittertestapp.database.TweetShort;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.models.Tweet;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TwitterApi {
    @GET("1.1/search/tweets.json")
    Call<TweetsResponse> searchTweets(@Query("q") String query, @Query("max_id") long max_id, @Query("since_id") long since_id, @Query("count") int count);



    @GET("1.1/search/tweets.json")
    Call<TweetsResponse> searchNearbyTweets(@Query("q") String query, @Query("geocode") String geocode, @Query("max_id") long max_id, @Query("since_id") long since_id, @Query("count") int count);
}
