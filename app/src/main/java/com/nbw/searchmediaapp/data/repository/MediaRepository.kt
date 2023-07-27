package com.nbw.searchmediaapp.data.repository

import com.nbw.searchmediaapp.data.model.ImagesResponse
import com.nbw.searchmediaapp.data.model.ResultWrapper

interface MediaRepository {
    suspend fun searchImages(
        query: String,
        sort: String,
        page: Int,
        size: Int
    ): ResultWrapper<ImagesResponse>
}