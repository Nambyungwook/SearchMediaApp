package com.nbw.searchmediaapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nbw.searchmediaapp.data.model.ImagesResponse
import com.nbw.searchmediaapp.data.model.Media
import com.nbw.searchmediaapp.data.model.MediaType
import com.nbw.searchmediaapp.data.model.ResponseErrorBody
import com.nbw.searchmediaapp.data.model.ResultWrapper
import com.nbw.searchmediaapp.data.model.VideosResponse
import com.nbw.searchmediaapp.data.repository.MediaRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException

class MediaViewModel(
    private val mediaRepository: MediaRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        private const val SAVE_STATE_KEY = "query"
    }

    var query = String()
        set(value) {
            field = value
            savedStateHandle.set(SAVE_STATE_KEY, value)
        }

    init {
        query = savedStateHandle.get<String>(SAVE_STATE_KEY) ?: ""
    }

    private val disposables = CompositeDisposable()

    private val _searchMediaResult = MutableLiveData<List<Media>>()
    val searchMediaResult: LiveData<List<Media>> get() = _searchMediaResult

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage


    fun searchMedia(query: String) = viewModelScope.launch(Dispatchers.IO) {
        val imagesResponse = async {
            mediaRepository.searchImages(query, "accuracy", 1, 15)
        }
        val videosResponse = async {
            mediaRepository.searchVideos(query, "accuracy", 1, 15)
        }

        val mediaList = mutableListOf<Media>()

        val responses = listOf(imagesResponse, videosResponse).awaitAll()

        responses.forEach { response ->
            when(response) {
                is ResultWrapper.Success -> {
                    if (response.value is ImagesResponse) {
                        response.value.images?.forEach { image ->
                            mediaList.add(
                                Media(
                                    mediaType = MediaType.IMAGE,
                                    image = image,
                                    video = null
                                )
                            )
                        }
                    } else if (response.value is VideosResponse) {
                        response.value.videos?.forEach { video ->
                            mediaList.add(
                                Media(
                                    mediaType = MediaType.VIDEO,
                                    image = null,
                                    video = video
                                )
                            )
                        }
                    } else {
                        mediaList.add(
                            Media(
                                mediaType = MediaType.NOTHING,
                                image = null,
                                video = null
                            )
                        )
                    }
                    _searchMediaResult.postValue(mediaList)
                }
                is ResultWrapper.Error -> {
                    if (response.code == null) {
                        _errorMessage.postValue("error type : ${response.errorType}  error message : ${response.errorMessage}")
                    } else {
                        _errorMessage.postValue("http code : ${response.code}  error type : ${response.errorType}  error message : ${response.errorMessage}")
                    }
                }
            }
        }
    }

    fun rxSearchMedia(query: String) {
        val mediaList = mutableListOf<Media>()
        val imagesObservable = mediaRepository.rxSearchImages(query, "accuracy", 1, 15)
        val videosObservable = mediaRepository.rxSearchVideos(query, "accuracy", 1, 15)

        disposables.add(
            Observable.zip(
                imagesObservable.subscribeOn(Schedulers.io()),
                videosObservable.subscribeOn(Schedulers.io())
            ) { imagesResponse, videosResponse ->
                imagesResponse.images?.forEach { image ->
                    mediaList.add(
                        Media(
                            mediaType = MediaType.IMAGE,
                            image = image,
                            video = null
                        )
                    )
                }

                videosResponse.videos?.forEach { video ->
                    mediaList.add(
                        Media(
                            mediaType = MediaType.VIDEO,
                            image = null,
                            video = video
                        )
                    )
                }

                mediaList
            }.observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    _searchMediaResult.postValue(result)
                },
                { throwable ->
                    val errorResponse = parseErrorResponse(throwable)

                    if (errorResponse.code == null) {
                        _errorMessage.postValue("error type : ${errorResponse.errorType}  error message : ${errorResponse.errorMessage}")
                    } else {
                        _errorMessage.postValue("http code : ${errorResponse.code}  error type : ${errorResponse.errorType}  error message : ${errorResponse.errorMessage}")
                    }
                }
            )
        )
    }

    private fun parseErrorResponse(throwable: Throwable): ResultWrapper.Error {
        return when (throwable) {
            is IOException -> {
                try {
                    ResultWrapper.Error(
                        null,
                        "Unknown Error",
                        "알 수 없는 에러가 발생했습니다. 나중에 다시 시도해 주세요."
                    )
                } catch (e: Exception) {
                    ResultWrapper.Error(
                        null,
                        "Unknown Error",
                        "알 수 없는 에러가 발생했습니다. 나중에 다시 시도해 주세요."
                    )
                }
            }
            is HttpException -> {
                try {
                    val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                    val adapter = moshi.adapter(ResponseErrorBody::class.java)
                    val responseErrorBody =
                        throwable.response()?.errorBody()?.string()?.let { errorString ->
                            adapter.fromJson(errorString)
                        } ?: kotlin.run {
                            ResponseErrorBody().apply {
                                errorType = "Unknown Error"
                                message = "알 수 없는 에러가 발생했습니다. 나중에 다시 시도해 주세요."
                            }
                        }

                    ResultWrapper.Error(
                        throwable.code(),
                        responseErrorBody.errorType,
                        responseErrorBody.message
                    )
                } catch (e: Exception) {
                    e.stackTrace
                    ResultWrapper.Error(null, "알 수 없는 에러가 발생했습니다. 나중에 다시 시도해 주세요.")
                }
            }
            else -> ResultWrapper.Error(null, "알 수 없는 에러가 발생했습니다. 나중에 다시 시도해 주세요.")
        }
    }

    fun clearDisposables() {
        disposables.clear()
    }

    // Room
    fun insertMedia(media: Media) = viewModelScope.launch(Dispatchers.IO) {
        mediaRepository.insertMedia(media)
    }

    fun deleteMedia(media: Media) = viewModelScope.launch(Dispatchers.IO) {
        mediaRepository.deleteMedia(media)
    }

    val favoriteMedias: StateFlow<List<Media>> = mediaRepository.getFavoriteMedias()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), listOf())
}