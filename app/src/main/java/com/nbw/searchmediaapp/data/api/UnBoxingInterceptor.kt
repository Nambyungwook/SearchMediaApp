package com.nbw.searchmediaapp.data.api

import okhttp3.Interceptor
import okhttp3.Response

class UnBoxingInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        when (response.code) {
            400 -> {

            }
            401 -> {

            }
            403 -> {

            }
            404 -> {

            }
        }

        return response
    }
}