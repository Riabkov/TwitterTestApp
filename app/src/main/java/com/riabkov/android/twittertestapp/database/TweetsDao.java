package com.riabkov.android.twittertestapp.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface TweetsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTweet(TweetShort tweetShort);

    @Update
    void updateTweet(TweetShort tweetShort);

    @Delete
    void deleteTweet(TweetShort tweetShort);

    @Query("SELECT * FROM tweetshort")
    LiveData<List<TweetShort>> getFavoriteTweets();
}
