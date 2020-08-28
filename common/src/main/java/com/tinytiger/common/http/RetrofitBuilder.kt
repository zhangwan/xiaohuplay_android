package com.tinytiger.common.http

import com.alibaba.fastjson.support.retrofit.Retrofit2ConverterFactory

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.util.concurrent.TimeUnit
import okhttp3.Response
import java.io.IOException
import android.text.TextUtils
import com.tinytiger.common.R
import com.tinytiger.common.utils.preference.SpUtils


/**
 * Created by zwy on 2020/6/5.
 */
class RetrofitBuilder {

    private var timeOut: Long = 600
    private var baseUrl = ""
    private val builder = OkHttpClient.Builder()
    private var interceptor: Interceptor? = null

    var timeoutInterceptor: Interceptor = object : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()

            var connectTimeout = chain.connectTimeoutMillis()
            var readTimeout = chain.readTimeoutMillis()
            var writeTimeout = chain.writeTimeoutMillis()

            val connectNew = request.header(HttpUtils.CONNECT_TIMEOUT)
            val readNew = request.header(HttpUtils.READ_TIMEOUT)
            val writeNew = request.header(HttpUtils.WRITE_TIMEOUT)

            if (!TextUtils.isEmpty(connectNew)) {
                connectTimeout = Integer.valueOf(connectNew!!)
            }
            if (!TextUtils.isEmpty(readNew)) {
                readTimeout = Integer.valueOf(readNew!!)
            }
            if (!TextUtils.isEmpty(writeNew)) {
                writeTimeout = Integer.valueOf(writeNew!!)
            }

            return chain
                    .withConnectTimeout(connectTimeout, TimeUnit.SECONDS)
                    .withReadTimeout(readTimeout, TimeUnit.SECONDS)
                    .withWriteTimeout(writeTimeout, TimeUnit.SECONDS)
                    .proceed(request)
        }
    }

    fun initTimeOut(timeOut: Long): RetrofitBuilder {
        this.timeOut = timeOut
        return this
    }

    fun initBaseUrl(baseUrl: String): RetrofitBuilder {
        this.baseUrl = baseUrl
        return this
    }

    fun addInterceptor(interceptor: Interceptor): RetrofitBuilder {
        this.interceptor = interceptor
        return this
    }

    fun <T> build(service: Class<T>): T {
        builder.connectTimeout(timeOut, TimeUnit.SECONDS)
        builder.writeTimeout(timeOut, TimeUnit.SECONDS)
        builder.readTimeout(timeOut, TimeUnit.SECONDS)

        builder.addInterceptor { chain ->
            var newBuilder = chain.request().newBuilder()
            newBuilder.addHeader("token", SpUtils.getString(R.string.token, ""))
            newBuilder.addHeader("imei", SpUtils.getString(R.string.android_id, ""))
            newBuilder.addHeader("channel", SpUtils.getString(R.string.channel, "1"))
            newBuilder.addHeader("v", SpUtils.getString(R.string.version, ""))
            chain.proceed(newBuilder.build())
        }

//        if (BuildConfig.DEBUG) {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        builder.addInterceptor(logging)
//        }

        builder.addInterceptor(timeoutInterceptor)

        if (interceptor != null) {
            builder.addInterceptor(interceptor!!)
        }
        return Retrofit.Builder()
                .client(builder.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(Retrofit2ConverterFactory())
                .baseUrl(baseUrl)
                .build()
                .create(service)
    }
}