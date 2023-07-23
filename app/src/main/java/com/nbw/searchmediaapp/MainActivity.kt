package com.nbw.searchmediaapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.nbw.searchmediaapp.databinding.ActivityMainBinding
import com.nbw.searchmediaapp.ui.view.FavoriteMediaFragment
import com.nbw.searchmediaapp.ui.view.SearchMediaFragment
import com.nbw.searchmediaapp.ui.view.SettingFragment

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupBottomNavigationView()

        if (savedInstanceState == null) {
            binding.bottomNavigationView.selectedItemId = R.id.fragment_search_media
        }
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