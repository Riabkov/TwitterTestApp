package com.riabkov.android.twittertestapp.recycler.tweetShort;

import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import com.riabkov.android.twittertestapp.database.TweetShort;

import java.util.List;

public class TweetShortResultDiff extends DiffUtil.Callback {

    private final List<TweetShort> mOldList;
    private final List<TweetShort> mNewList;

    public TweetShortResultDiff(List<TweetShort> oldList, List<TweetShort> newList) {
        this.mOldList = oldList;
        this.mNewList = newList;
    }

    @Override
    public int getOldListSize() {
        return mOldList.size();
    }

    @Override
    public int getNewListSize() {
        return mNewList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return mOldList.get(oldItemPosition).getId() == mNewList.get(newItemPosition).getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return mOldList.get(oldItemPosition).getText().equals(mNewList.get(newItemPosition).getText());
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
