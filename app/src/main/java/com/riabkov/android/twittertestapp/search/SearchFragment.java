package com.riabkov.android.twittertestapp.search;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.paginate.Paginate;
import com.riabkov.android.twittertestapp.R;
import com.riabkov.android.twittertestapp.base.BaseFragment;
import com.riabkov.android.twittertestapp.dagger.DaggerViewComponent;
import com.riabkov.android.twittertestapp.dagger.ViewModule;
import com.riabkov.android.twittertestapp.database.TweetShort;
import com.riabkov.android.twittertestapp.recycler.tweetShort.TweetShortAdapter;
import com.riabkov.android.twittertestapp.recycler.tweetShort.TweetShortHolder;
import com.riabkov.android.twittertestapp.utils.OnRecyclerScrolledListener;
import com.riabkov.android.twittertestapp.utils.RxSearchObservable;
import com.riabkov.android.twittertestapp.utils.QueryUtils;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;


public class SearchFragment extends BaseFragment implements SearchContract.View {


    @BindView(R.id.swipe_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    SearchView mSearchView;

    @Inject
    SearchContract.Presenter mPresenter;

    private TweetShortAdapter mAdapter;
    private List<TweetShort> mTweets = new ArrayList<>();

    private boolean isLoadingInProgress = false;

    public SearchFragment() {
    }

    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int onRequestLayout() {
        return R.layout.fragment_swipe_recycler;
    }

    @Override
    protected void onViewReady() {
        setHasOptionsMenu(true);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isLoadingInProgress = true;
                // hasLoadedAllItems = false;
                mPresenter.getFreshTweets("radio", mTweets.get(0).getId());
            }
        });
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        mAdapter = new TweetShortAdapter(new TweetShortHolder.OnClickableEntityClicked() {
            @Override
            public void onClicked(String text) {
                if (mSearchView != null) {
                    //mSearchView.requestFocus();
                    mSearchView.setQuery(text, false);

                }
            }
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnScrollListener(new OnRecyclerScrolledListener((LinearLayoutManager) mRecyclerView.getLayoutManager()) {
            @Override
            protected void loadMoreItems() {
                mPresenter.getNextTweets(QueryUtils.getQuery(mSearchView), mTweets.get(mTweets.size() - 1).getId());
                isLoadingInProgress = true;
            }

            @Override
            public int getTotalPageCount() {
                return 0;
            }

            @Override
            public boolean isLastPage() {
                return false;
            }

            @Override
            public boolean isLoading() {
                return isLoadingInProgress;
            }
        });

        mPresenter.searchTweets(QueryUtils.getQuery(mSearchView));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        mSearchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        mSearchView.setFocusable(false);
        mSearchView.setIconifiedByDefault(false);

        RxSearchObservable.fromView(mSearchView)
                .debounce(300, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .switchMap(new Function<String, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(String s) throws Exception {
                        mPresenter.searchTweets(QueryUtils.getQuery(mSearchView));
                        return Observable.just(s);
                    }

                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
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
        isLoadingInProgress = false;
    }

    @Override
    public void showNextTweets(List<TweetShort> tweets) {
        for (TweetShort tweetShort : tweets) {
            if (!mTweets.contains(tweetShort)) {
                mTweets.add(tweetShort);
            }
        }
        mAdapter.updateData(mTweets);
        isLoadingInProgress = false;
    }

    @Override
    public void showTweets(List<TweetShort> tweets) {

        mTweets.clear();
        mTweets.addAll(tweets);
        mAdapter.setData(mTweets);
        isLoadingInProgress = false;
    }

    @Override
    public void showMessage(String text) {
        isLoadingInProgress = false;
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }
}
