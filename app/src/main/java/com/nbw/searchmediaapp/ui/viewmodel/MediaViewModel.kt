package com.nbw.searchmediaapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.nbw.searchmediaapp.data.model.Media
import com.nbw.searchmediaapp.data.repository.MediaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
            savedStateHandle[SAVE_STATE_KEY] = value
        }

    init {
        query = savedStateHandle.get<String>(SAVE_STATE_KEY) ?: ""
    }

    private val _searchMediaResult = MutableStateFlow<PagingData<Media>>(PagingData.empty())
    val searchMediaResult: StateFlow<PagingData<Media>> = _searchMediaResult.asStateFlow()

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun searchMedia(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = mediaRepository.searchMedias(
                query = query,
                sort = getSortMode()
            )

            response.cachedIn(viewModelScope).collect() {
                _searchMediaResult.value = it
            }
        }
    }

    // Room
    fun insertMedia(media: Media) = viewModelScope.launch(Dispatchers.IO) {
        mediaRepository.insertMedia(media)
    }

    fun deleteMedia(media: Media) = viewModelScope.launch(Dispatchers.IO) {
        mediaRepository.deleteMedia(media)
    }

    val favoriteMedias: StateFlow<PagingData<Media>> =
        mediaRepository.getFavoriteMedias()
            .cachedIn(viewModelScope)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PagingData.empty())

    // DataStore
    fun saveSortMode(value: String) = viewModelScope.launch(Dispatchers.IO) {
        mediaRepository.saveSortMode(value)
    }

    suspend fun getSortMode() = withContext(Dispatchers.IO) {
        mediaRepository.getSortMode().first()
    }
}