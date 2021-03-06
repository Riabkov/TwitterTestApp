package com.riabkov.android.twittertestapp;

import android.app.Application;
import android.support.annotation.Nullable;
import android.support.text.emoji.EmojiCompat;
import android.support.text.emoji.FontRequestEmojiCompatConfig;
import android.support.v4.provider.FontRequest;
import android.util.Log;

import com.riabkov.android.twittertestapp.dagger.ApplicationComponent;
import com.riabkov.android.twittertestapp.dagger.ApplicationModule;
import com.riabkov.android.twittertestapp.dagger.DaggerApplicationComponent;
import com.riabkov.android.twittertestapp.dagger.DataModule;
import com.riabkov.android.twittertestapp.network.CustomTwitterApiClient;
import com.riabkov.android.twittertestapp.network.ResponseInterceptor;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

import static android.support.constraint.Constraints.TAG;

public class TwitterTestApplication extends Application {
    public static ApplicationComponent mApplicationComponent;



    private static CustomTwitterApiClient mCustomApiClient;

    @Override
    public void onCreate() {
        super.onCreate();

        mApplicationComponent = DaggerApplicationComponent
                .builder()
                .applicationModule(new ApplicationModule(this))
                .dataModule(new DataModule(this))
                .build();
        mApplicationComponent.inject(this);

        TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))//enable logging when app is in debug mode
                .twitterAuthConfig(new TwitterAuthConfig(getResources().getString(R.string.com_twitter_sdk_android_CONSUMER_KEY), getResources().getString(R.string.com_twitter_sdk_android_CONSUMER_SECRET)))//pass the created app Consumer KEY and Secret also called API Key and Secret
                .debug(true)//enable debug mode
                .build();
        Twitter.initialize(config);

        final TwitterSession activeSession = TwitterCore.getInstance()
                .getSessionManager().getActiveSession();

        // example of custom OkHttpClient with logging HttpLoggingInterceptor
        final HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        final OkHttpClient customClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(new ResponseInterceptor())
                .build();

        // pass custom OkHttpClient into TwitterApiClient and add to TwitterCore

        if (activeSession != null) {
            mCustomApiClient = new CustomTwitterApiClient(activeSession, customClient);
            TwitterCore.getInstance().addApiClient(activeSession, mCustomApiClient);
        } else {
            mCustomApiClient = new CustomTwitterApiClient(customClient);
            TwitterCore.getInstance().addGuestApiClient(mCustomApiClient);
        }

        final FontRequest fontRequest = new FontRequest(
                "com.google.android.gms.fonts",
                "com.google.android.gms",
                "Noto Color Emoji Compat",
                R.array.com_google_android_gms_fonts_certs);
        EmojiCompat.init(new FontRequestEmojiCompatConfig(getApplicationContext(), fontRequest)
                .setReplaceAll(true)
                .registerInitCallback(new EmojiCompat.InitCallback() {
                    @Override
                    public void onInitialized() {
                        Log.i(TAG, "EmojiCompat initialized");
                    }

                    @Override
                    public void onFailed(@Nullable Throwable throwable) {
                        Log.e(TAG, "EmojiCompat initialization failed", throwable);
                    }
                }));
    }

    public static CustomTwitterApiClient getCustomApiClient() {
        return mCustomApiClient;
    }
}
