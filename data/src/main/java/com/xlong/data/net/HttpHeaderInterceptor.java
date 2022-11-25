package com.xlong.data.net;

import android.text.TextUtils;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by vigorous on 16/11/7.
 */

public class HttpHeaderInterceptor implements Interceptor {
    private Map<String, String> mHeaders = new ConcurrentHashMap<>();

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder requestBuilder = chain.request().newBuilder();
        Request request = chain.request();
        String path = request.url().host();
        if (TextUtils.isEmpty(path)) {
            mHeaders.put("Host", Constants.HOST);
            addHeaders(requestBuilder);
        }

        return chain.proceed(requestBuilder.build());
    }


    private void addHeaders(Request.Builder builder) {

        for (Map.Entry<String, String> entry : mHeaders.entrySet()) {
            builder.addHeader(entry.getKey(), entry.getValue());
        }
    }
}
