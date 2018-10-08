package com.riabkov.android.twittertestapp.favourite;

import android.os.Bundle;


import com.riabkov.android.twittertestapp.R;
import com.riabkov.android.twittertestapp.base.BaseTweetRecyclerFragment;
import com.riabkov.android.twittertestapp.dagger.DaggerViewComponent;
import com.riabkov.android.twittertestapp.dagger.ViewModule;
import com.riabkov.android.twittertestapp.database.TweetShort;
import com.riabkov.android.twittertestapp.search.SearchFragment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


public class FavoriteFragment extends BaseTweetRecyclerFragment implements FavoriteContract.View {

    private List<TweetShort> mFavoriteTweets = new ArrayList<>();

    @Inject
    FavoriteContract.Presenter mPresenter;

    public static FavoriteFragment newInstance() {
        FavoriteFragment fragment = new FavoriteFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void onViewReady() {
        getActivity().setTitle(R.string.title_favorite);
        hasLoadedAll = true;
        super.onViewReady();
        mPresenter.getFavoriteTweets();
    }

    @Override
    protected void onRecyclerBottomReached() {

    }

    @Override
    protected void onFavoriteClicked(TweetShort tweet) {
        mPresenter.removeFavoriteTweet(tweet);
        mFavoriteTweets.remove(tweet);
        mAdapter.updateData(mFavoriteTweets);
    }

    @Override
    protected void onTextEntityClicked(String text) {
        getFragmentManager().beginTransaction()
                .replace(R.id.content, SearchFragment.newInstance(text))
                .commit();
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
    public void showFavoriteTweets(List<TweetShort> tweets) {
        mFavoriteTweets.addAll(tweets);
        mAdapter.setData(tweets);
    }
}
