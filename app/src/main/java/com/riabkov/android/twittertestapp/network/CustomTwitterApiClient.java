package com.riabkov.android.twittertestapp.network;

import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterSession;

import okhttp3.OkHttpClient;

public class CustomTwitterApiClient extends TwitterApiClient {
    public CustomTwitterApiClient(TwitterSession session){
        super(session);
    }

    public CustomTwitterApiClient() {
        super();
    }

    public CustomTwitterApiClient(OkHttpClient client) {
        super(client);
    }

    public CustomTwitterApiClient(TwitterSession session, OkHttpClient client) {

        super(session, client);
    }

    public <S> S createService(Class<S> serviceClass){
        return getService(serviceClass);
    }
}
