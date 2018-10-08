package com.riabkov.android.twittertestapp.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface TweetsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertTweet(TweetShort tweetShort);

    @Update
    int updateTweet(TweetShort tweetShort);

    @Delete
    int deleteTweet(TweetShort tweetShort);

    @Query("SELECT * FROM tweetshort")
    Single<List<TweetShort>> getFavoriteTweets();

    @Query("SELECT * FROM tweetshort")
    Flowable<List<TweetShort>> getFavoriteTweetsFlowable();
}
