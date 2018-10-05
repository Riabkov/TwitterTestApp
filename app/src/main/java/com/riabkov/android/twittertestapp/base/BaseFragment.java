package com.riabkov.android.twittertestapp.base;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.riabkov.android.twittertestapp.TwitterTestApplication;
import com.riabkov.android.twittertestapp.dagger.ApplicationComponent;

import butterknife.ButterKnife;

public abstract class BaseFragment extends Fragment {
    @CallSuper
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(onRequestLayout(), container, false);
        initializeInjection();
        initializeViewsInjection(view);
        return view;
    }

    private void initializeInjection() {
        onInitializeInjection();
    }

    private void initializeViewsInjection(@NonNull View view) {
        ButterKnife.bind(this, view);
        onViewReady();
    }

    protected ApplicationComponent getApplicationComponent() {
        return TwitterTestApplication.mApplicationComponent;
    }

    @LayoutRes
    protected abstract int onRequestLayout();

    protected abstract void onViewReady();

    protected abstract void onInitializeInjection();
}
