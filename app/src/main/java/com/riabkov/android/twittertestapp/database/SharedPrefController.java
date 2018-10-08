package com.riabkov.android.twittertestapp.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.riabkov.android.twittertestapp.dagger.scope.ApplicationContext;

public class SharedPrefController {
    private final static String PREF_TWEETS_PER_PAGE = "TWEETS_PER_PAGE";
    private final static String PREF_NEARBY_RANGE = "PREF_NEARBY_RANGE";

    private final SharedPreferences mSharedPreferences;

    public SharedPrefController(@ApplicationContext Context context){
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public int getTweetsPerPage(){
        return mSharedPreferences.getInt(PREF_TWEETS_PER_PAGE, 10);
    }

    public void setTweetsPerPage(int count){
        mSharedPreferences.edit()
                .putInt(PREF_TWEETS_PER_PAGE, count)
                .apply();
    }


    public int getNearbyRange(){
        return mSharedPreferences.getInt(PREF_NEARBY_RANGE, 10);
    }

    public void setNearbyRange(int range){
        mSharedPreferences.edit()
                .putInt(PREF_NEARBY_RANGE, range)
                .apply();
    }
}
