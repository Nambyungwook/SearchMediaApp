package com.nbw.searchmediaapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nbw.searchmediaapp.data.model.ImagesResponse
import com.nbw.searchmediaapp.data.model.ResultWrapper
import com.nbw.searchmediaapp.data.repository.MediaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MediaViewModel(
    private val mediaRepository: MediaRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

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


}