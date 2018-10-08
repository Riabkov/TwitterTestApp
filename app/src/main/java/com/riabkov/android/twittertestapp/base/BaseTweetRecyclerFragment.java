package com.riabkov.android.twittertestapp.base;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.paginate.Paginate;
import com.riabkov.android.twittertestapp.R;
import com.riabkov.android.twittertestapp.database.TweetShort;
import com.riabkov.android.twittertestapp.recycler.tweetShort.TweetShortAdapter;
import com.riabkov.android.twittertestapp.recycler.tweetShort.TweetShortHolder;

import butterknife.BindView;

public abstract class BaseTweetRecyclerFragment extends BaseFragment {

    @BindView(R.id.recycler_view)
    protected RecyclerView mRecyclerView;
    protected TweetShortAdapter mAdapter;

    protected boolean isLoadingProgress = true;
    protected boolean hasLoadedAll = false;

    @Override
    protected int onRequestLayout() {
        return R.layout.fragment_recycler;
    }

    @Override
    protected void onViewReady() {
        mAdapter = new TweetShortAdapter(new TweetShortHolder.OnClickableEntityClicked() {
            @Override
            public void onClicked(String text) {
                onTextEntityClicked(text);
            }
        }, new TweetShortHolder.OnFavoriteClicked() {
            @Override
            public void favoriteClicked(TweetShort tweet) {
                onFavoriteClicked(tweet);
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);

        Paginate.Callbacks callbacks = new Paginate.Callbacks() {
            @Override
            public void onLoadMore() {
                onRecyclerBottomReached();
            }

            @Override
            public boolean isLoading() {
                return isLoadingProgress;
            }

            @Override
            public boolean hasLoadedAllItems() {
                return hasLoadedAll;
            }
        };

        Paginate.with(mRecyclerView, callbacks)
              //  .setLoadingTriggerThreshold(4)
                .build();
    }

    protected abstract void onRecyclerBottomReached();
    protected abstract void onFavoriteClicked(TweetShort tweet);
    protected abstract void onTextEntityClicked(String text);
}
