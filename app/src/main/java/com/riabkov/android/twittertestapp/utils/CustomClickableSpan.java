package com.riabkov.android.twittertestapp.utils;

import android.support.annotation.NonNull;
import android.text.style.ClickableSpan;
import android.view.View;

public abstract class CustomClickableSpan extends ClickableSpan {

    private String mClickableText;

    public CustomClickableSpan (String text){
        mClickableText = text;
    }

    @Override
    public void onClick(@NonNull View widget) {
        onTextClick(mClickableText, widget);
    }

    public abstract void onTextClick(String text, View widget);
}