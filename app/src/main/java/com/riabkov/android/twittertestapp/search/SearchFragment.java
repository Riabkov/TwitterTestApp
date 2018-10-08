package com.riabkov.android.twittertestapp.search;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Toast;

import com.riabkov.android.twittertestapp.R;
import com.riabkov.android.twittertestapp.base.BaseTweetSwipeRecyclerFragment;
import com.riabkov.android.twittertestapp.dagger.DaggerViewComponent;
import com.riabkov.android.twittertestapp.dagger.ViewModule;
import com.riabkov.android.twittertestapp.database.TweetShort;
import com.riabkov.android.twittertestapp.utils.QueryUtils;


import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


public class SearchFragment extends BaseTweetSwipeRecyclerFragment implements SearchContract.View {
    private final static String ARG_QUERY = "ARG_QUERY";

    @Inject
    SearchContract.Presenter mPresenter;

    private List<TweetShort> mTweets = new ArrayList<>();
    private String mSavedQuery;

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    public static SearchFragment newInstance(String query) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_QUERY, query);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void onViewReady() {
        super.onViewReady();
        getActivity().setTitle(R.string.title_search);
        setHasOptionsMenu(true);
        if(getArguments()!= null) {
            mSavedQuery = getArguments().getString(ARG_QUERY);
        }
        String query = mSavedQuery != null? mSavedQuery :QueryUtils.getQuery(mSearchView);
        mPresenter.searchTweets(query);
    }

    @Override
    protected void onRecyclerBottomReached() {
        mPresenter.getNextTweets(QueryUtils.getQuery(mSearchView), mTweets.get(mTweets.size() - 1).getId());
        isLoadingProgress = true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if(mSavedQuery != null) {
            mSearchView.setQuery(mSavedQuery, false);
        }
    }

    @Override
    protected void onFavoriteClicked(TweetShort tweet) {
        if (tweet.isFavorite()) {
            mPresenter.addFavorite(tweet);
        } else {
            mPresenter.removeFavorite(tweet);
        }
    }

    @Override
    protected void onTextEntityClicked(String text) {
        if (mSearchView != null) {
            mSearchView.setQuery(text, false);
        }
    }

    @Override
    protected void onRefreshSwiped() {
        isLoadingProgress = true;
        hasLoadedAll = false;
        mPresenter.getFreshTweets(QueryUtils.getQuery(mSearchView), mTweets.get(0).getId());
    }

    @Override
    protected void onSearchQueryChanged(String query) {
        super.onSearchQueryChanged(query);
        hasLoadedAll = false;
        mPresenter.searchTweets(QueryUtils.getQuery(mSearchView));
    }

    @Override
    protected void onInitializeInjection() {
        DaggerViewComponent.builder()
                .applicationComponent(getApplicationComponent())
                .viewModule(new ViewModule(this))
                .build()
                .inject(this);
    }


    @Override
    public void showFreshTweets(List<TweetShort> tweets) {
        mTweets.addAll(0, tweets);
        mAdapter.updateData(mTweets);
        mSwipeRefreshLayout.setRefreshing(false);
        mRecyclerView.smoothScrollToPosition(0);
        isLoadingProgress = false;
    }

    @Override
    public void showNextTweets(List<TweetShort> tweets) {
        for (TweetShort tweetShort : tweets) {
            if (!mTweets.contains(tweetShort)) {
                mTweets.add(tweetShort);
            }
        }
        if (tweets.size()<3) {
            hasLoadedAll = true;
        }
        mAdapter.updateData(mTweets);
        isLoadingProgress = false;
    }

    @Override
    public void showTweets(List<TweetShort> tweets) {
        mTweets.clear();
        mTweets.addAll(tweets);
        if(tweets.size()<3){
            hasLoadedAll = true;
        }
        mAdapter.setData(mTweets);
        isLoadingProgress = false;
    }

    @Override
    public void showMessage(String text) {
        isLoadingProgress = false;
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }
}
