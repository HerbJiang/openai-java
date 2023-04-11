package com.theokanning.openai.service;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * OkHttp Interceptor that adds an authorization token header
 */
public class ApikeyInterceptor implements Interceptor {

    private final String apikey;

    ApikeyInterceptor(String apikey) {
        this.apikey = apikey;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request()
                .newBuilder()
                .header("api-key", apikey)
                .build();
        return chain.proceed(request);
    }
}
