package com.riabkov.android.twittertestapp.dagger;

import com.riabkov.android.twittertestapp.dagger.scope.ActivityScope;

import dagger.Component;


@ActivityScope
@Component(
        dependencies = ApplicationComponent.class,
        modules = {PresenterModule.class})
interface PresenterComponent {

}
