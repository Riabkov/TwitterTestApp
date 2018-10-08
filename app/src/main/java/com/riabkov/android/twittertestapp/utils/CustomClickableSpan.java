package com.riabkov.android.twittertestapp.utils;

import android.support.annotation.NonNull;
import android.text.style.ClickableSpan;
import android.view.View;

public abstract class CustomClickableSpan extends ClickableSpan {

    private final String mClickableText;

    protected CustomClickableSpan(String text){
        mClickableText = text;
    }

    @Override
    public void onClick(@NonNull View widget) {
        onTextClick(mClickableText, widget);
    }

    protected abstract void onTextClick(String text, View widget);
}