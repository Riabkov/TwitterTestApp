package com.riabkov.android.twittertestapp.database;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.twitter.sdk.android.core.models.Coordinates;
import com.twitter.sdk.android.core.models.TweetEntities;
import com.twitter.sdk.android.core.models.User;

import java.lang.reflect.Type;
import java.util.List;

public class Converters {
    private static final Gson sGson = new Gson();

    @TypeConverter
    public static TweetEntities fromTweetEntitiesString(String json){
        Type listType = new TypeToken<TweetEntities>(){}.getType();
        return json == null ? null : (TweetEntities) sGson.fromJson(json, listType);
    }

    @TypeConverter
    public static String fromTweetEntities(TweetEntities data){
        return data == null ? null : sGson.toJson(data);
    }

    @TypeConverter
    public static Coordinates fromCoordinatesString(String json){
        Type listType = new TypeToken<Coordinates>(){}.getType();
        return json == null ? null : (Coordinates) sGson.fromJson(json, listType);
    }

    @TypeConverter
    public static String fromCoordinates(Coordinates data){
        return data == null ? null : sGson.toJson(data);
    }

    @TypeConverter
    public static User fromUserString(String json){
        Type listType = new TypeToken<User>(){}.getType();
        return json == null ? null : (User) sGson.fromJson(json, listType);
    }

    @TypeConverter
    public static String fromUser(User data){
        return data == null ? null : sGson.toJson(data);
    }

    @TypeConverter
    public static List<Integer> fromIntegerListString(String json){
        Type listType = new TypeToken<List<Integer>>(){}.getType();
        return json == null ? null : (List<Integer>) sGson.fromJson(json, listType);
    }

    @TypeConverter
    public static String fromIntegerList(List<Integer> data){
        return data == null ? null : sGson.toJson(data);
    }
}
