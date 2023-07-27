package com.nbw.searchmediaapp.data.api

import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.Response
import okhttp3.ResponseBody
import okio.Buffer
import okio.BufferedSource
import retrofit2.HttpException
import retrofit2.Response.error

class HttpExceptionInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        when (response.code) {
            204 -> {
                // No Content
                throw HttpException(error<Any>(response.body ?: EmptyResponseBody(), response))
            }
        }

        return response
    }

    inner class EmptyResponseBody : ResponseBody() {
        override fun contentLength(): Long {
            return 0
        }

        override fun contentType(): MediaType? {
            return null
        }

        override fun source(): BufferedSource {
            return Buffer()
        }
    }
}