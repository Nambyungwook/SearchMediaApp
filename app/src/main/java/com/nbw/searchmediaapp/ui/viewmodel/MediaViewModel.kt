package com.nbw.searchmediaapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nbw.searchmediaapp.data.model.ImagesResponse
import com.nbw.searchmediaapp.data.model.ResponseErrorBody
import com.nbw.searchmediaapp.data.model.ResultWrapper
import com.nbw.searchmediaapp.data.repository.MediaRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
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

    private val _searchMediaResult = MutableLiveData<ImagesResponse>()
    val searchMediaResult: LiveData<ImagesResponse> get() = _searchMediaResult

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun searchImages(query: String) = viewModelScope.launch(Dispatchers.IO) {
        val response = mediaRepository.searchImages(query, "accuracy", 1, 15)

        when (response) {
            is ResultWrapper.Success -> {
                _searchMediaResult.postValue(response.value)
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

    fun rxSearchImages(query: String) {
        disposables.add(
            mediaRepository.rxSearchImages(query, "accuracy", 1, 15)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result ->
                        _searchMediaResult.postValue(result)
                    },
                    { throwable ->
                        val errorResponse = when (throwable) {
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
                        if (errorResponse.code == null) {
                            _errorMessage.postValue("error type : ${errorResponse.errorType}  error message : ${errorResponse.errorMessage}")
                        } else {
                            _errorMessage.postValue("http code : ${errorResponse.code}  error type : ${errorResponse.errorType}  error message : ${errorResponse.errorMessage}")
                        }
                    }
                )
        )
    }

    fun clearDisposables() {
        disposables.clear()
    }
}