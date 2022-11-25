package com.xlong.data.net

import android.text.TextUtils
import android.util.Log
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Response
import java.nio.charset.Charset

/**
 * 拦截器
 * Create by xlong 2022/11/23
 */
class HttpCommonQueryInterceptor : Interceptor {
    private var commonFunc: ICommonParamFunc? = null

    constructor(commonFunc: ICommonParamFunc?) {
        this.commonFunc = commonFunc
    }

    override fun intercept(chain: Interceptor.Chain): Response {

        var original = chain.request()
        val originalHttpUrl = original.url()
        var _commonParams = mutableMapOf<String, String>()
        if (commonFunc?.get() != null) {
            _commonParams = commonFunc?.get()!!
        }

        //添加 scheme host port
        val urlBuilder = HttpUrl.Builder().scheme(originalHttpUrl.scheme()).host(originalHttpUrl.host()).port(originalHttpUrl.port())
        // path segment
        val pathSegments = originalHttpUrl.pathSegments()
        if (pathSegments != null && pathSegments.size > 0) {
            for (i in pathSegments.indices) {
                urlBuilder.addPathSegment(pathSegments[i])
            }
        }

        // 调用api时传递的参数（sign参数为加密参数，需要url中所有参数进行md5）
        val queryParams: MutableMap<String, String?> = LinkedHashMap()
        for (str in originalHttpUrl.queryParameterNames()) {
            var value = originalHttpUrl.queryParameter(str)
            if (TextUtils.isEmpty(value)) {
                value = ""
            }
            queryParams[str] = value
            urlBuilder.addQueryParameter(str, value)
        }
        // 添加通用参数
        if (_commonParams.isNotEmpty()) {
            for (en in _commonParams.entries) {
                var value = en.value
                if (TextUtils.isEmpty(value)) {
                    value = ""
                }
                value = value.replace(" ", "_")
                urlBuilder.addQueryParameter(en.key, value)
            }
        }

        val url = urlBuilder.build()
        original = original.newBuilder().url(url).build()

        val response = chain.proceed(original)
        if (Constants.DEBUG) {
            val starttime = System.currentTimeMillis()
            //获得返回的body，注意此处不要使用responseBody.string()获取返回数据，原因在于这个方法会消耗返回结果的数据(buffer)
            val responseBody = response.body()

            //为了不消耗buffer，我们这里使用source先获得buffer对象，然后clone()后使用
            val source = responseBody!!.source()
            source.request(Long.MAX_VALUE) // Buffer the entire body.
            //获得返回的数据
            val buffer = source.buffer()

            //使用前clone()下，避免直接消耗
            val readString = buffer.clone().readString(Charset.forName("UTF-8"))
            Log.i("NEWHTTP", "httpRequest  method  : " + original.method() + "  url: " + url.toString())
            Log.i("NEWHTTP", "httpResponse  method : " + response.request().method() + " response: " + readString)
        }
        return response
    }
}