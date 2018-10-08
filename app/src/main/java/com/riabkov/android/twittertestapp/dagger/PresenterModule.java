package com.riabkov.android.twittertestapp.dagger;

import com.riabkov.android.twittertestapp.favourite.FavoriteContract;
import com.riabkov.android.twittertestapp.favourite.FavoritePresenter;
import com.riabkov.android.twittertestapp.nearby.NearbyContract;
import com.riabkov.android.twittertestapp.nearby.NearbyPresenter;
import com.riabkov.android.twittertestapp.search.SearchContract;
import com.riabkov.android.twittertestapp.search.SearchPresenter;

import dagger.Module;
import dagger.Provides;

@Module
class PresenterModule {

    @Provides
    SearchContract.Presenter provideSearchPresenter(SearchPresenter presenter){
        return presenter;
    }

    @Provides
    FavoriteContract.Presenter provideFavoritePresenter(FavoritePresenter presenter){
        return presenter;
    }

    @Provides
    NearbyContract.Presenter provideNearbyPresenter(NearbyPresenter presenter){
        return presenter;
    }
}
