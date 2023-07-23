package com.nbw.searchmediaapp.data.repository

import com.nbw.searchmediaapp.data.api.RetrofitInstance.api
import com.nbw.searchmediaapp.data.model.ImagesResponse
import retrofit2.Response

class MediaRepositoryImpl: MediaRepository {
    override suspend fun searchImages(
        query: String,
        sort: String,
        page: Int,
        size: Int
    ): Response<ImagesResponse> {
        return api.searchImages(
            query,
            sort,
            page,
            size
        )
    }
}