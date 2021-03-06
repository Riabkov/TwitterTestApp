package com.riabkov.android.twittertestapp.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.google.gson.annotations.SerializedName;
import com.twitter.sdk.android.core.models.Coordinates;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.TweetEntities;
import com.twitter.sdk.android.core.models.User;

import java.util.List;

@Entity(indices = @Index(value = {"id"}, unique = true))
@TypeConverters(Converters.class)
public class TweetShort {

    public TweetShort(){

    }

    public TweetShort(Tweet tweet) {
        this.id = tweet.getId();
        this.coordinates = tweet.coordinates;
        this.tweetEntities = tweet.entities;
        this.text = tweet.text;
        this.user = tweet.user;
        this.displayTextRange = tweet.displayTextRange;
    }

    @PrimaryKey
    @SerializedName("id")
    private long id;

    @SerializedName("id_str")
    private String idStr;

    @SerializedName("coordinates")
    private Coordinates coordinates;

    @SerializedName("entities")
    private TweetEntities tweetEntities;

    @SerializedName(value = "text", alternate = {"full_text"})
    private String text;

    @SerializedName("user")
    private User user;

    @SerializedName("display_text_range")
    private List<Integer> displayTextRange;

    private boolean isFavorite = false;

    public long getId() {
        return id;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public TweetEntities getTweetEntities() {
        return tweetEntities;
    }

    public String getText() {
        return text;
    }

    public User getUser() {
        return user;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public void setTweetEntities(TweetEntities tweetEntities) {
        this.tweetEntities = tweetEntities;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if(o instanceof Tweet){
            return ((Tweet) o).id == this.id;
        }
        if (!(o instanceof TweetShort)) return false;
        final TweetShort other = (TweetShort) o;
        return this.id == other.id;
    }

    public List<Integer> getDisplayTextRange() {
        return displayTextRange;
    }

    public void setDisplayTextRange(List<Integer> displayTextRange) {
        this.displayTextRange = displayTextRange;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite() {
        isFavorite = true;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public String getIdStr() {
        return idStr;
    }

    public void setIdStr(String idStr) {
        this.idStr = idStr;
    }
}
