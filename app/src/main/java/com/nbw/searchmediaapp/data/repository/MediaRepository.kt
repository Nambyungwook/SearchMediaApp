package com.nbw.searchmediaapp.data.repository

import com.nbw.searchmediaapp.data.model.ImagesResponse
import retrofit2.Response

interface MediaRepository {
    suspend fun searchImages(
        query: String,
        sort: String,
        page: Int,
        size: Int
    ): Response<ImagesResponse>
}