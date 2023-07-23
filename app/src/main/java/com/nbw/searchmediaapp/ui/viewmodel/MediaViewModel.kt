package com.nbw.searchmediaapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nbw.searchmediaapp.data.model.ImagesResponse
import com.nbw.searchmediaapp.data.repository.MediaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MediaViewModel(
    private val mediaRepository: MediaRepository
): ViewModel() {

    private val _searchMediaResult = MutableLiveData<ImagesResponse>()
    val searchMediaResult: LiveData<ImagesResponse> get() = _searchMediaResult

    fun searchImages(query: String) = viewModelScope.launch(Dispatchers.IO) {
        val response = mediaRepository.searchImages(query, "accuracy", 1, 15)
        if (response.isSuccessful) {
            response.body()?.let { body ->
                _searchMediaResult.postValue(body)
            }
        }
    }
}