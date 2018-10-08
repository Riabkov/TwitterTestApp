package com.riabkov.android.twittertestapp.favourite;

import com.riabkov.android.twittertestapp.base.IPresenter;
import com.riabkov.android.twittertestapp.base.IView;
import com.riabkov.android.twittertestapp.database.TweetShort;

import java.util.List;

public class FavoriteContract {
    public interface View extends IView {
        void showFavoriteTweets(List<TweetShort> tweets);
    }

    public interface Presenter extends IPresenter {
        void getFavoriteTweets();
        void removeFavoriteTweet(TweetShort tweetShort);
    }
}
