package com.nbw.searchmediaapp.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.nbw.searchmediaapp.data.api.RetrofitInstance
import com.nbw.searchmediaapp.data.model.ImagesResponse
import com.nbw.searchmediaapp.data.model.Media
import com.nbw.searchmediaapp.data.model.MediaType
import com.nbw.searchmediaapp.data.model.ResultWrapper
import com.nbw.searchmediaapp.data.model.VideosResponse
import com.nbw.searchmediaapp.utils.Constants
import com.nbw.searchmediaapp.utils.safeApiCall
import kotlinx.coroutines.Dispatchers
import okio.IOException
import retrofit2.HttpException

class MediaPagingSource(
    private val query: String,
    private val sort: String
): PagingSource<Int, Media>() {
    override fun getRefreshKey(state: PagingState<Int, Media>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Media> {
        val mediaList = mutableListOf<Media>()

        return try {
            var imageResponseEndOfPaginactionReached: Boolean? = null
            var videoResponseEndOfPaginactionReached: Boolean? = null

            val pageNumber = params.key ?: Constants.STARTING_PAGE_INDEX
            val imagesResponse = safeApiCall(Dispatchers.IO) {
                RetrofitInstance.api.searchImages(query, sort, pageNumber, params.loadSize)
            }
            val videosResponse = safeApiCall(Dispatchers.IO) {
                RetrofitInstance.api.searchVideos(query, sort, pageNumber, params.loadSize)
            }
            var responseError: ResultWrapper.Error? = null
            val responses = listOf(imagesResponse, videosResponse)
            responses.forEach { response ->
                when (response) {
                    is ResultWrapper.Success -> {
                        when (response.value) {
                            is ImagesResponse -> {
                                response.value.images?.forEach { image ->
                                    mediaList.add(
                                        Media(
                                            mediaType = MediaType.IMAGE,
                                            image = image,
                                            video = null
                                        )
                                    )
                                }
                                imageResponseEndOfPaginactionReached = response.value.meta?.isEnd
                            }

                            is VideosResponse -> {
                                response.value.videos?.forEach { video ->
                                    mediaList.add(
                                        Media(
                                            mediaType = MediaType.VIDEO,
                                            image = null,
                                            video = video
                                        )
                                    )
                                }
                                videoResponseEndOfPaginactionReached = response.value.meta?.isEnd
                            }

                            else -> {
                                mediaList.add(
                                    Media(
                                        mediaType = MediaType.NOTHING,
                                        image = null,
                                        video = null
                                    )
                                )
                            }
                        }
                    }
                    is ResultWrapper.Error -> {
                        responseError = response
                        return@forEach
                    }
                }
            }
            if (responseError != null) {
                responseError?.error?.let {
                    LoadResult.Error(it)
                } ?: kotlin.run {
                    LoadResult.Invalid()
                }
            } else {
                val endOfPaginactionReached = (imageResponseEndOfPaginactionReached == true
                        && videoResponseEndOfPaginactionReached == true)
                val data = mediaList
                val prevKey = if (pageNumber == Constants.STARTING_PAGE_INDEX) null else pageNumber - 1
                val nextKey = if (endOfPaginactionReached == true) {
                    null
                } else {
                    pageNumber + (params.loadSize / Constants.PAGING_SIZE)
                }

                LoadResult.Page(
                    data = data,
                    prevKey = prevKey,
                    nextKey = nextKey
                )
            }
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }
}