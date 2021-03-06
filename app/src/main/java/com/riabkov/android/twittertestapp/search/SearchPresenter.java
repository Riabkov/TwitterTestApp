package com.riabkov.android.twittertestapp.search;

import com.riabkov.android.twittertestapp.database.TweetShort;
import com.riabkov.android.twittertestapp.repo.TwitterRepository;

import java.util.List;

import javax.inject.Inject;

public class SearchPresenter implements SearchContract.Presenter{

    private SearchContract.View mView;
    private TwitterRepository mTwitterRepository;

    @Inject
    SearchPresenter(SearchContract.View view,TwitterRepository twitterRepository){
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
    public void searchTweets(String query) {
        mTwitterRepository.getTweets(new TwitterRepository.OnDataPassCallback() {
            @Override
            public void passError(Exception e) {
                mView.showMessage(e.getMessage());

            }

            @Override
            public void passData(List<TweetShort> data) {
                mView.showTweets(data);
            }
        }, query, 0, 0);
    }

    @Override
    public void getFreshTweets(String query, long since_id) {
        mTwitterRepository.getTweets(new TwitterRepository.OnDataPassCallback() {
            @Override
            public void passError(Exception e) {
                mView.showMessage(e.getMessage());

            }

            @Override
            public void passData(List<TweetShort> data) {
                mView.showFreshTweets(data);
            }
        }, query, 0, since_id);
    }

    @Override
    public void getNextTweets(String query, long max_id) {
        mTwitterRepository.getTweets(new TwitterRepository.OnDataPassCallback() {
            @Override
            public void passError(Exception e) {
                mView.showMessage(e.getMessage());

            }

            @Override
            public void passData(List<TweetShort> data) {
                mView.showNextTweets(data);
            }
        }, query, max_id, 0);
    }

    @Override
    public void addFavorite(TweetShort tweetShort) {
        mTwitterRepository.addFavorite(tweetShort);
    }

    @Override
    public void removeFavorite(TweetShort tweetShort) {
        mTwitterRepository.removeFavorite(tweetShort);
    }

    @Override
    public int getTweetsPerPage() {
        return mTwitterRepository.getTweetsPerPageCount();
    }

}
