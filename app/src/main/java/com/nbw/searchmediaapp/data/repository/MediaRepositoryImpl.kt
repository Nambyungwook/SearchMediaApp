package com.nbw.searchmediaapp.data.repository

import com.nbw.searchmediaapp.data.api.RetrofitInstance.api
import com.nbw.searchmediaapp.data.api.RetrofitInstance.rxApi
import com.nbw.searchmediaapp.data.model.ImagesResponse
import com.nbw.searchmediaapp.data.model.ResultWrapper
import com.nbw.searchmediaapp.utils.safeApiCall
import io.reactivex.Observable
import kotlinx.coroutines.Dispatchers

class MediaRepositoryImpl: MediaRepository {
    override suspend fun searchImages(
        query: String,
        sort: String,
        page: Int,
        size: Int
    ): ResultWrapper<ImagesResponse> {
        return safeApiCall(Dispatchers.IO) {
            api.searchImages(
                query,
                sort,
                page,
                size
            )
        }
    }

    override fun rxSearchImages(
        query: String,
        sort: String,
        page: Int,
        size: Int
    ): Observable<ImagesResponse> {
        return rxApi.rxSearchImages(
            query,
            sort,
            page,
            size
        )
    }
}