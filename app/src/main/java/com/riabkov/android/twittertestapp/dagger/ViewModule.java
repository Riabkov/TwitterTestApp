package com.riabkov.android.twittertestapp.dagger;

import com.riabkov.android.twittertestapp.base.IView;
import com.riabkov.android.twittertestapp.favourite.FavoriteContract;
import com.riabkov.android.twittertestapp.nearby.NearbyContract;
import com.riabkov.android.twittertestapp.search.SearchContract;

import dagger.Module;
import dagger.Provides;


@Module
public class ViewModule {
    private IView mView;

    public ViewModule(IView view){this.mView = view;}

    public ViewModule(){}

    @Provides
    SearchContract.View provideSearchView(){
        return ((SearchContract.View) mView);
    }

    @Provides
    NearbyContract.View provideNearbyView(){
        return ((NearbyContract.View) mView);
    }

    @Provides
    FavoriteContract.View provideFavoriteView(){
        return ((FavoriteContract.View) mView);
    }
}
