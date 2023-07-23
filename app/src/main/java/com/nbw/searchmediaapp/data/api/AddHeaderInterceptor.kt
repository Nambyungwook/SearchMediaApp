package com.nbw.searchmediaapp.data.api

import com.nbw.searchmediaapp.utils.Constants
import okhttp3.Interceptor
import okhttp3.Response

class AddHeaderInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        requestBuilder.addHeader("Authorization", "KakaoAK ${Constants.API_KEY}")

        return chain.proceed(requestBuilder.build())
    }
}