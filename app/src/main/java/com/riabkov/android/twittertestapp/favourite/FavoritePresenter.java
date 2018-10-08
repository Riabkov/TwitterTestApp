package com.riabkov.android.twittertestapp.favourite;


import com.riabkov.android.twittertestapp.database.TweetShort;
import com.riabkov.android.twittertestapp.repo.TwitterRepository;

import javax.inject.Inject;

public class FavoritePresenter implements FavoriteContract.Presenter {


    private FavoriteContract.View mView;
    private TwitterRepository mTwitterRepository;

    @Inject
    FavoritePresenter(FavoriteContract.View view,TwitterRepository twitterRepository){
        mView = view;
        mTwitterRepository = twitterRepository;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void getFavoriteTweets() {
       mView.showFavoriteTweets(mTwitterRepository.getFavoriteTweets());
    }

    @Override
    public void removeFavoriteTweet(TweetShort tweetShort) {
        mTwitterRepository.removeFavorite(tweetShort);
    }
}
