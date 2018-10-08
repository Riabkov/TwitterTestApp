package com.riabkov.android.twittertestapp.nearby;

import android.Manifest;
import android.annotation.SuppressLint;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import com.intentfilter.androidpermissions.PermissionManager;
import com.riabkov.android.twittertestapp.R;
import com.riabkov.android.twittertestapp.base.BaseTweetSwipeRecyclerFragment;
import com.riabkov.android.twittertestapp.dagger.DaggerViewComponent;
import com.riabkov.android.twittertestapp.dagger.ViewModule;
import com.riabkov.android.twittertestapp.database.TweetShort;
import com.riabkov.android.twittertestapp.utils.QueryUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import static android.content.Context.LOCATION_SERVICE;


public class NearbyFragment extends BaseTweetSwipeRecyclerFragment implements NearbyContract.View {

    @Inject
    NearbyContract.Presenter mPresenter;

    private List<TweetShort> mTweets = new ArrayList<>();
    private Location mUserLocation;
    private LocationManager mLocationManager;

    public static NearbyFragment newInstance() {
        NearbyFragment fragment = new NearbyFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void onViewReady() {
        super.onViewReady();

        getActivity().setTitle(R.string.title_nearby);
        setHasOptionsMenu(true);
        getLastKnownLocation();
    }


    @Override
    protected void onSearchQueryChanged(String query) {
        super.onSearchQueryChanged(query);
        mPresenter.searchTweets(QueryUtils.getQuery(mSearchView), mUserLocation.getLatitude(), mUserLocation.getLongitude());
    }

    @Override
    protected void onRecyclerBottomReached() {
        super.onRecyclerBottomReached();
        mPresenter.getNextTweets(QueryUtils.getQuery(mSearchView), mTweets.get(mTweets.size() - 1).getId(), mUserLocation.getLatitude(), mUserLocation.getLongitude());
        isLoadingProgress = true;
    }

    @Override
    protected void onFavoriteClicked(TweetShort tweet) {
        if(tweet.isFavorite()){
            mPresenter.addFavorite(tweet);
        }else{
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
        mPresenter.getFreshTweets(QueryUtils.getQuery(mSearchView), mTweets.get(0).getId(), mUserLocation.getLatitude(), mUserLocation.getLongitude());
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
        mSwipeRefreshLayout.setRefreshing(false);
        mAdapter.updateData(mTweets);
        isLoadingProgress = false;
    }

    @Override
    public void showTweets(List<TweetShort> tweets) {
        mTweets.clear();
        mTweets.addAll(tweets);
        if (tweets.size()<3) {
            hasLoadedAll = true;
        }
        mSwipeRefreshLayout.setRefreshing(false);
        mAdapter.setData(mTweets);
        isLoadingProgress = false;
    }

    @Override
    public void showMessage(String text) {
        isLoadingProgress = false;
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("MissingPermission")
    private void getLastKnownLocation() {
        PermissionManager manager = PermissionManager.getInstance(getContext());
        manager.checkPermissions(Arrays.asList(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), new PermissionManager.PermissionRequestListener() {
            @Override
            public void onPermissionGranted() {
                mLocationManager = (LocationManager) getApplicationComponent().getContext().getSystemService(LOCATION_SERVICE);
                List<String> providers = mLocationManager.getProviders(true);
                Location bestLocation = null;

                for (String provider : providers) {
                    Location l = mLocationManager.getLastKnownLocation(provider);
                    if (l == null) {
                        continue;
                    }
                    if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                        // Found best last known location: %s", l);
                        bestLocation = l;
                    }
                }
                mUserLocation = bestLocation;
                double latitude = 0;
                double longitude = 0;
                if(mUserLocation != null){
                    latitude = mUserLocation.getLatitude();
                    longitude = mUserLocation.getLongitude();
                }
                mSwipeRefreshLayout.setRefreshing(true);
                mPresenter.searchTweets(QueryUtils.getQuery(mSearchView),latitude,longitude);
            }

            @Override
            public void onPermissionDenied() {
                showMessage("Location permission required");
            }
        });
    }
}
