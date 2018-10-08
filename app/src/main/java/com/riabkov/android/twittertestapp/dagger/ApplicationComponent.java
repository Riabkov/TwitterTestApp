package com.riabkov.android.twittertestapp.dagger;

import android.content.Context;

import com.riabkov.android.twittertestapp.TwitterTestApplication;
import com.riabkov.android.twittertestapp.dagger.scope.ApplicationContext;
import com.riabkov.android.twittertestapp.database.SharedPrefController;
import com.riabkov.android.twittertestapp.database.TweetsDatabase;
import com.riabkov.android.twittertestapp.network.TwitterApi;
import com.riabkov.android.twittertestapp.repo.TwitterRepository;

import javax.inject.Singleton;

import dagger.Component;


@Singleton
@Component(modules = {
        ApplicationModule.class,
        DataModule.class
})
public interface ApplicationComponent {

    @ApplicationContext
    Context getContext();

    TwitterApi getTwitterApi();
    TwitterRepository getTwitterRepository();
    TweetsDatabase getTweetsDatabase();
    SharedPrefController getSharedPref();

    void inject(TwitterTestApplication application);
}
