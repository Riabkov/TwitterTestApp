package com.riabkov.android.twittertestapp.search;

import com.riabkov.android.twittertestapp.base.IPresenter;
import com.riabkov.android.twittertestapp.base.IView;
import com.riabkov.android.twittertestapp.database.TweetShort;

import java.util.List;

public class SearchContract {
    public interface View extends IView {
        void showFreshTweets(List<TweetShort> tweets);
        void showNextTweets(List<TweetShort> tweets);
        void showTweets(List<TweetShort> tweets);
        void showMessage(String text);
    }

    public interface Presenter extends IPresenter {
        void searchTweets(String query);

        void getFreshTweets(String query, long since_id);

        void getNextTweets(String query, long max_id);

        void addFavorite(TweetShort tweetShort);

        void removeFavorite(TweetShort tweetShort);

        int getTweetsPerPage();
    }
}
