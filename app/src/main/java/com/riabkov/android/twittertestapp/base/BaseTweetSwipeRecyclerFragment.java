package com.riabkov.android.twittertestapp.base;

import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ImageView;

import com.riabkov.android.twittertestapp.R;
import com.riabkov.android.twittertestapp.utils.QueryUtils;
import com.riabkov.android.twittertestapp.utils.RxSearchObservable;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

public abstract class BaseTweetSwipeRecyclerFragment extends BaseTweetRecyclerFragment {

    protected SearchView mSearchView;

    @BindView(R.id.swipe_layout)
    protected SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected int onRequestLayout() {
        return R.layout.fragment_swipe_recycler;
    }

    @Override
    protected void onViewReady() {
        super.onViewReady();
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onRefreshSwiped();
            }
        });
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);
        mSearchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        mSearchView.setFocusable(false);
        mSearchView.setIconifiedByDefault(false);

        SearchView.SearchAutoComplete searchAutoComplete =
                mSearchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchAutoComplete.setHintTextColor(Color.BLACK);
        searchAutoComplete.setTextColor(Color.BLACK);

        ImageView searchIcon = mSearchView.findViewById(android.support.v7.appcompat.R.id.search_mag_icon);
        searchIcon.setImageResource(R.drawable.ic_menu_search);
        ImageView clearIcon = mSearchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
        clearIcon.setImageResource(R.drawable.ic_close_small);

        mSearchView.setQuery(QueryUtils.getQuery(mSearchView), false);

        RxSearchObservable.fromView(mSearchView)
                .debounce(300, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(String s) throws Exception {
                        return s.length()>2;
                    }
                })
                .switchMap(new Function<String, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(String s) {
                        hasLoadedAll = false;
                        onSearchQueryChanged(s);
                        return Observable.just(s);
                    }

                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    protected void onSearchQueryChanged(String query){}

    protected abstract void onRefreshSwiped();
}
