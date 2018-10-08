package com.riabkov.android.twittertestapp.repo;

import com.riabkov.android.twittertestapp.database.SharedPrefController;
import com.riabkov.android.twittertestapp.database.TweetShort;
import com.riabkov.android.twittertestapp.database.TweetsDatabase;
import com.riabkov.android.twittertestapp.network.TweetsResponse;
import com.riabkov.android.twittertestapp.network.TwitterApi;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.services.params.Geocode;

import java.util.List;
import java.util.concurrent.Callable;


import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class TwitterRepository {

    private TwitterApi mTwitterApi;
    private TweetsDatabase mTweetsDatabase;

    private List<TweetShort> mFavoriteTweets;
    private SharedPrefController mSharedPref;

    public interface OnDataPassCallback {
        void passError(Exception e);

        void passData(List<TweetShort> data);
    }

    @Inject
    public TwitterRepository(TwitterApi twitterApi, TweetsDatabase tweetsDatabase, SharedPrefController sharedPrefController) {
        mTwitterApi = twitterApi;
        mTweetsDatabase = tweetsDatabase;
        mSharedPref = sharedPrefController;

        Flowable<List<TweetShort>> databaseFlowable = mTweetsDatabase.tweetsDao().getFavoriteTweetsFlowable();
        databaseFlowable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<TweetShort>>() {
                    @Override
                    public void accept(List<TweetShort> tweetShorts) {
                        mFavoriteTweets = tweetShorts;
                    }
                });
    }

    public List<TweetShort> getFavoriteTweets() {
        return mFavoriteTweets;
    }


    public void getTweets(OnDataPassCallback callback, String query, long max_id, long since_id) {

            mTwitterApi.searchTweets(query, max_id-1, since_id, getTweetsPerPageCount()).enqueue(new Callback<TweetsResponse>() {
                @Override
                public void success(Result<TweetsResponse> result) {
                    compareTweetLists(result.data.getTweets());
                    callback.passData(result.data.getTweets());
                }

                @Override
                public void failure(TwitterException exception) {
                    exception.printStackTrace();
                }
            });
    }

    public void getNearbyTweets(OnDataPassCallback callback, String query, double latitude, double longitude, long max_id, long since_id) {
        Geocode geocode = new Geocode(latitude, longitude, getNearbyRange(), Geocode.Distance.KILOMETERS);
        mTwitterApi.searchNearbyTweets(query, geocode, max_id, since_id, getTweetsPerPageCount()).enqueue(new Callback<TweetsResponse>() {
            @Override
            public void success(Result<TweetsResponse> result) {
                compareTweetLists(result.data.getTweets());
                callback.passData(result.data.getTweets());
            }

            @Override
            public void failure(TwitterException exception) {
                exception.printStackTrace();
                callback.passError(exception);
            }
        });
    }

    public void addFavorite(TweetShort tweet) {
        Observable.fromCallable(new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                return mTweetsDatabase.tweetsDao().insertTweet(tweet);
            }
        })
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public void removeFavorite(TweetShort tweet) {
        Observable.fromCallable(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return mTweetsDatabase.tweetsDao().deleteTweet(tweet);
            }
        })
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public int getTweetsPerPageCount(){
        return mSharedPref.getTweetsPerPage();
    }

    private int getNearbyRange(){
        return mSharedPref.getNearbyRange();
    }

    private void compareTweetLists(List<TweetShort> data) {
        if (mFavoriteTweets != null && !mFavoriteTweets.isEmpty()) {
            for (TweetShort tweetShort : data) {
                if (mFavoriteTweets.contains(tweetShort)) {
                    tweetShort.setFavorite();
                }
            }
        }
    }
}
