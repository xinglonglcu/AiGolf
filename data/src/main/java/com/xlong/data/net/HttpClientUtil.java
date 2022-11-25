package com.xlong.data.net;


import android.util.Log;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.internal.tls.OkHostnameVerifier;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by vigorous on 16/11/4.
 */

public class HttpClientUtil {
    private static final int READ_TIME_OUT = 20;
    private static final int WRITE_TIME_OUT = 20;
    private static final int CONNECT_TIME_OUT = 10;
    private static OkHttpClient mOKHttpClient;
    private static OkHttpClient mDefaultOKHttpClient;
    private static OkHttpClient mUrltOKHttpClient;

    private HttpClientUtil() {
    }

    public static OkHttpClient getCommonQueryParamsHttpClient(Interceptor commonQueryInterceptor, long timeOut) {
        mOKHttpClient = new OkHttpClient
                .Builder()
                .hostnameVerifier(new HostnameVerifier() {
                    /*
                     * 关于这个接口的说明，官方有文档描述：
                     * This is an extended verification option that implementers can provide.
                     * It is to be used during a handshake if the URL's hostname does not match the
                     * peer's identification hostname.
                     *
                     * 使用HTTPDNS后URL里设置的hostname不是远程的主机名(如:m.taobao.com)，与证书颁发的域不匹配，
                     * Android HttpsURLConnection提供了回调接口让用户来处理这种定制化场景。
                     * 在确认HTTPDNS返回的源站IP与Session携带的IP信息一致后，您可以在回调方法中将待验证域名替换为原来的真实域名进行验证。
                     *
                     */
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return HttpsURLConnection.getDefaultHostnameVerifier().verify(Constants.HOST, session);
                    }
                })
                .addInterceptor(new HttpHeaderInterceptor())
                .addInterceptor(commonQueryInterceptor)
                .connectTimeout(timeOut, TimeUnit.MILLISECONDS)
                .readTimeout(timeOut, TimeUnit.MILLISECONDS)
                .writeTimeout(timeOut, TimeUnit.MILLISECONDS)
                .build();
        return mOKHttpClient;
    }

    public static OkHttpClient getCommonQueryParamsHttpClient(Interceptor commonQueryInterceptor) {
        mOKHttpClient = new OkHttpClient
                .Builder()
                .hostnameVerifier(new HostnameVerifier() {
                    /*
                     * 关于这个接口的说明，官方有文档描述：
                     * This is an extended verification option that implementers can provide.
                     * It is to be used during a handshake if the URL's hostname does not match the
                     * peer's identification hostname.
                     *
                     * 使用HTTPDNS后URL里设置的hostname不是远程的主机名(如:m.taobao.com)，与证书颁发的域不匹配，
                     * Android HttpsURLConnection提供了回调接口让用户来处理这种定制化场景。
                     * 在确认HTTPDNS返回的源站IP与Session携带的IP信息一致后，您可以在回调方法中将待验证域名替换为原来的真实域名进行验证。
                     *
                     */
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        Log.d("Xlong", "verify: hostname = " + hostname+"   " + Constants.HOST +"  ");
                        return HttpsURLConnection.getDefaultHostnameVerifier().verify(Constants.HOST, session);
                    }
                })
                .addInterceptor(new HttpHeaderInterceptor())
                .addInterceptor(commonQueryInterceptor)
                .connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIME_OUT, TimeUnit.SECONDS)
                .build();
        return mOKHttpClient;
    }

    public static OkHttpClient getDefaultHttpClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.NONE);

        if (mDefaultOKHttpClient == null) {
            if (mOKHttpClient == null) {
                ApiClient.Companion.getInstance();
            }
            OkHttpClient.Builder builder = mOKHttpClient.newBuilder();
            builder.interceptors().clear();
            builder.hostnameVerifier(OkHostnameVerifier.INSTANCE);
            mDefaultOKHttpClient = builder
                    .addInterceptor(logging)
                    .build();
        }
        return mDefaultOKHttpClient;
    }
}
