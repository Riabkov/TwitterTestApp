package com.riabkov.android.twittertestapp.dagger;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.Context;

import com.riabkov.android.twittertestapp.TwitterTestApplication;
import com.riabkov.android.twittertestapp.dagger.scope.ApplicationContext;
import com.riabkov.android.twittertestapp.database.SharedPrefController;
import com.riabkov.android.twittertestapp.database.TweetsDatabase;
import com.riabkov.android.twittertestapp.network.TwitterApi;
import com.riabkov.android.twittertestapp.repo.TwitterRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@Module
public class DataModule {

    private Application mApplication;

    public DataModule(Application application){mApplication = application;}

    @Provides
    @Singleton
    TwitterApi provideTwitterApi(){
        return TwitterTestApplication.getCustomApiClient().createService(TwitterApi.class);
    }

    @Provides
    @Singleton
    TweetsDatabase provideTweetsDatabase(){
        return Room.databaseBuilder(mApplication, TweetsDatabase.class, "twitter-test-database")
                .fallbackToDestructiveMigration()
                .build();
    }

    @Provides
    @Singleton
    TwitterRepository provideTwitterRepository(TwitterApi api, TweetsDatabase database, SharedPrefController sharedPrefController){
        return new TwitterRepository(api, database, sharedPrefController);
    }

    @Provides
    @Singleton
    SharedPrefController provideSharedPref(@ApplicationContext Context context){
        return new SharedPrefController(context);
    }
}
