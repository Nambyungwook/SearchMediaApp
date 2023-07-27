package com.nbw.searchmediaapp.data.api

import com.nbw.searchmediaapp.utils.Constants.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitInstance {
    private val okHttpClient: OkHttpClient by lazy {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)
        val httpExceptionInterceptor = HttpExceptionInterceptor()
        val headerInterceptor = HeaderInterceptor()

        OkHttpClient.Builder()
            .addInterceptor(httpExceptionInterceptor)
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(headerInterceptor)
            .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .build()
    }

    val api: SearchApi by lazy {
        retrofit.create(SearchApi::class.java)
    }
}