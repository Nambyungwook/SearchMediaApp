package com.nbw.searchmediaapp.utils

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.nbw.searchmediaapp.data.model.ResponseErrorBody
import com.nbw.searchmediaapp.data.model.ResultWrapper
import com.nbw.searchmediaapp.ui.view.FavoriteMediaFragment
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okio.IOException
import retrofit2.HttpException

suspend fun <T> safeApiCall(
    dispatcher: CoroutineDispatcher,
    apiCall: suspend () -> T
): ResultWrapper<T> {
    return withContext(dispatcher) {
        try {
            val response = apiCall.invoke()

            response?.let { apiResponse ->
                ResultWrapper.Success(apiResponse)
            } ?: kotlin.run {
                ResultWrapper.Error(null, "Response Null", "응답 값이 없습니다!!")
            }
       } catch (throwable: Throwable) {
           when (throwable) {
               is IOException -> {
                   try {
                       ResultWrapper.Error(null, "Unknown Error","알 수 없는 에러가 발생했습니다. 나중에 다시 시도해 주세요.")
                   } catch (e: Exception) {
                       ResultWrapper.Error(null, "Unknown Error","알 수 없는 에러가 발생했습니다. 나중에 다시 시도해 주세요.")
                   }
               }
               is HttpException -> {
                   try {
                       val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                       val adapter = moshi.adapter(ResponseErrorBody::class.java)
                       val responseErrorBody = throwable.response()?.errorBody()?.string()?.let { errorString ->
                           adapter.fromJson(errorString)
                       } ?: kotlin.run {
                           ResponseErrorBody().apply {
                               errorType = "Unknown Error"
                               message = "알 수 없는 에러가 발생했습니다. 나중에 다시 시도해 주세요."
                           }
                       }

                       ResultWrapper.Error(throwable.code(), responseErrorBody.errorType, responseErrorBody.message)
                   } catch (e: Exception) {
                       e.stackTrace
                       ResultWrapper.Error(null, "알 수 없는 에러가 발생했습니다. 나중에 다시 시도해 주세요.")
                   }
               }
               else -> ResultWrapper.Error(null, "알 수 없는 에러가 발생했습니다. 나중에 다시 시도해 주세요.")
           }
       }
    }
}

fun <T> FavoriteMediaFragment.collectLatestStateFlow(flow: Flow<T>, collect: suspend (T) -> Unit) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collectLatest(collect)
        }
    }
}