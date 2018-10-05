package com.riabkov.android.twittertestapp.nearby;

import android.os.Bundle;

import com.riabkov.android.twittertestapp.R;
import com.riabkov.android.twittertestapp.base.BaseFragment;
import com.riabkov.android.twittertestapp.dagger.DaggerViewComponent;
import com.riabkov.android.twittertestapp.dagger.ViewModule;

import javax.inject.Inject;


public class NearbyFragment extends BaseFragment implements NearbyContract.View {

    @Inject
    NearbyContract.Presenter mPresenter;

    public static NearbyFragment newInstance() {
        NearbyFragment fragment = new NearbyFragment();
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
        return R.layout.fragment_swipe_recycler;
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
