package com.riabkov.android.twittertestapp.network;

import android.support.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class ResponseInterceptor implements Interceptor {
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request original = chain.request();

        Request.Builder builder = original.newBuilder()
                .addHeader("Content-Type", "application/json; charset=utf-8");
        Request request = builder.build();
        return chain.proceed(request);
    }
}
