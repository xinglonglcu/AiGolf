package com.xlong.data.net

import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 *
 * Create by xlong 2022/11/23
 */
class ApiClient {

    private var mOkHttpClient: OkHttpClient? = null
    private val mRequestFailListener: RequestFailListener? = null
    val basicService: BasicService by lazy {
        init(BasicService::class.java, Constants.BaseUrl)
    }

    constructor(commonFunc: ICommonParamFunc?) {
        globalCommonFunc = commonFunc
        creatClient(globalCommonFunc)
    }

    private fun <T> init(targetClass: Class<T>, url: String): T {
        val gson = GsonBuilder().registerTypeAdapterFactory(ItemTypeAdapterFactory()) // This is the important line ;)
            .create()
        val retrofit = Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create(gson)).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).client(mOkHttpClient).build()
        return retrofit.create(targetClass)
    }

    private fun creatClient(commonFunc: ICommonParamFunc?) {
        // 公共参数拦截器
        mOkHttpClient = HttpClientUtil.getCommonQueryParamsHttpClient(HttpCommonQueryInterceptor(commonFunc)).newBuilder().addInterceptor(Interceptor { chain ->
            val requestTime = System.currentTimeMillis()
            val request = chain.request()
            try {
                val response = chain.proceed(request)
                if (!response.isSuccessful) {
                    onRequestFail(request, requestTime)
                }
                return@Interceptor response
            } catch (e: Throwable) {
                onRequestFail(request, requestTime)
                throw e
            }
        }).build()
    }

    private fun onRequestFail(request: Request, requestTime: Long) {
        mRequestFailListener?.onRequestFail(request, requestTime)
    }

    interface RequestFailListener {
        fun onRequestFail(request: Request?, requestTime: Long)
    }

    companion object {
        var globalCommonFunc: ICommonParamFunc? = null
        private var apiClient: ApiClient? = null

        fun getInstance(): ApiClient {
            if (apiClient == null) {
                apiClient = ApiClient(globalCommonFunc)
            }
            return apiClient!!
        }
    }

}