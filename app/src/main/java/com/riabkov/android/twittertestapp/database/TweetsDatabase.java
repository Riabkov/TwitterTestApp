package com.riabkov.android.twittertestapp.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {
        TweetShort.class
}, version = 3)
public abstract class TweetsDatabase extends RoomDatabase {

    public abstract TweetsDao tweetsDao();
}
