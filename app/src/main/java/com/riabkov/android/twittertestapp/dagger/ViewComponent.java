package com.riabkov.android.twittertestapp.dagger;

import com.riabkov.android.twittertestapp.dagger.scope.ActivityScope;
import com.riabkov.android.twittertestapp.favourite.FavoriteFragment;
import com.riabkov.android.twittertestapp.nearby.NearbyFragment;
import com.riabkov.android.twittertestapp.search.SearchFragment;

import dagger.Component;


@ActivityScope
@Component(
        dependencies = ApplicationComponent.class,
        modules = {
                ViewModule.class,
                PresenterModule.class
        })
public interface ViewComponent {
        void inject(SearchFragment fragment);

        void inject(NearbyFragment fragment);

        void inject(FavoriteFragment fragment);
}
