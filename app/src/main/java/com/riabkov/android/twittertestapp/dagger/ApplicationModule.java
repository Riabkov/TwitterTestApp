package com.riabkov.android.twittertestapp.dagger;

import android.app.Application;
import android.content.Context;

import com.riabkov.android.twittertestapp.dagger.scope.ApplicationContext;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {
    private final Application mApplication;

    public ApplicationModule(Application application){mApplication = application;}

    @Provides
    @ApplicationContext
    @Singleton
    Context provideContext(){return mApplication;}

    @Provides
    @Singleton
    Application provideApplication(){return mApplication;}

}
