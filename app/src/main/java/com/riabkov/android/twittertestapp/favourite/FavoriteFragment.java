package com.riabkov.android.twittertestapp.favourite;

import android.os.Bundle;


import com.riabkov.android.twittertestapp.R;
import com.riabkov.android.twittertestapp.base.BaseFragment;
import com.riabkov.android.twittertestapp.dagger.DaggerViewComponent;
import com.riabkov.android.twittertestapp.dagger.ViewModule;

import javax.inject.Inject;


public class FavoriteFragment extends BaseFragment implements FavoriteContract.View {

    @Inject
    FavoriteContract.Presenter mPresenter;

    public static FavoriteFragment newInstance() {
        FavoriteFragment fragment = new FavoriteFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    protected int onRequestLayout() {
        return R.layout.fragment_recycler;
    }

    @Override
    protected void onViewReady() {

    }

    @Override
    protected void onInitializeInjection() {
        DaggerViewComponent.builder()
                .applicationComponent(getApplicationComponent())
                .viewModule(new ViewModule(this))
                .build()
                .inject(this);
    }

}
