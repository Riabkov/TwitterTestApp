package com.riabkov.android.twittertestapp.network;

import com.twitter.sdk.android.core.services.params.Geocode;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TwitterApi {
    @GET("1.1/search/tweets.json")
    Call<TweetsResponse> searchTweets(@Query("q") String query, @Query("max_id") long max_id, @Query("since_id") long since_id, @Query("count") int count);



    @GET("1.1/search/tweets.json")
    Call<TweetsResponse> searchNearbyTweets(@Query("q") String query, @Query("geocode") Geocode geocode, @Query("max_id") long max_id, @Query("since_id") long since_id, @Query("count") int count);
}
