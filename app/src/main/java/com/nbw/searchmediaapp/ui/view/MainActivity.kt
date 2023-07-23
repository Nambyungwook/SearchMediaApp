package com.nbw.searchmediaapp.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.nbw.searchmediaapp.R
import com.nbw.searchmediaapp.data.repository.MediaRepositoryImpl
import com.nbw.searchmediaapp.databinding.ActivityMainBinding
import com.nbw.searchmediaapp.ui.viewmodel.MediaViewModel
import com.nbw.searchmediaapp.ui.viewmodel.MediaViewModelProviderFactory

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    lateinit var mediaViewModel: MediaViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupBottomNavigationView()

        if (savedInstanceState == null) {
            binding.bottomNavigationView.selectedItemId = R.id.fragment_search_media
        }

        val mediaRepository = MediaRepositoryImpl()
        val viewModelFactory = MediaViewModelProviderFactory(mediaRepository)
        mediaViewModel = ViewModelProvider(this, viewModelFactory)[MediaViewModel::class.java]
    }

    private fun setupBottomNavigationView() {
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.fragment_search_media -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, SearchMediaFragment())
                        .commit()
                    true
                }
                R.id.fragment_favorite_media -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, FavoriteMediaFragment())
                        .commit()
                    true
                }
                R.id.fragment_setting -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, SettingFragment())
                        .commit()
                    true
                }
                else -> false
            }
        }
    }
}