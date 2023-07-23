package com.nbw.searchmediaapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nbw.searchmediaapp.data.repository.MediaRepository

@Suppress("UNCHECKED_CAST")
class MediaViewModelProviderFactory(
    private val mediaRepository: MediaRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MediaViewModel::class.java)) {
            return MediaViewModel(mediaRepository) as T
        }
        throw IllegalArgumentException("ViewModel class not found")
    }
}