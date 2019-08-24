package com.example.music.network

import com.example.music.BASE_URL
import com.example.music.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by tk on 2019/8/19
 */

object ApiGenerator {
    private const val DEFAULT_TIME_OUT = 30

    private var retrofit: Retrofit
//    private var okHttpClient: OkHttpClient

    init {

        retrofit =   Retrofit.Builder()
            .baseUrl(BASE_URL)//基础URL 建议以 / 结尾
            .addConverterFactory(GsonConverterFactory.create())//设置 Json 转换器
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//RxJava 适配器
            .build()
    }

//    //开启okhttp调试
//    private fun configureOkHttp(builder: OkHttpClient.Builder): OkHttpClient {
//        builder.connectTimeout(DEFAULT_TIME_OUT.toLong(), TimeUnit.SECONDS)
//        val logInterceptor = HttpLoggingInterceptor()
//        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//        builder.addInterceptor(logInterceptor)
//        return builder.build()
//    }

    fun <T> getApiService(clazz: Class<T>) = retrofit.create(clazz)

    fun <T> getApiService(retrofit: Retrofit, clazz: Class<T>) = retrofit.create(clazz)
}