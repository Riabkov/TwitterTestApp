package com.riabkov.android.twittertestapp.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;

import com.riabkov.android.twittertestapp.TwitterTestApplication;
import com.riabkov.android.twittertestapp.dagger.ApplicationComponent;

import butterknife.ButterKnife;


public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        beforeLayout();
        if(onRequestLayout() != 0) {
            setContentView(onRequestLayout());
        }
        onInitializeInjection();
        initializeViewsInjection(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        onViewDestroy();
    }

    protected abstract void onViewReady(Bundle savedInstanceState);

    protected abstract void onViewDestroy();

    @LayoutRes
    protected abstract int onRequestLayout();

    protected abstract void onInitializeInjection();

    protected void beforeLayout() {
    }

   protected void initializeViewsInjection(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        onViewReady(savedInstanceState);
    }

    protected ApplicationComponent getApplicationComponent() {
        return TwitterTestApplication.mApplicationComponent;
    }
}
