package com.nbw.searchmediaapp.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.nbw.searchmediaapp.R
import com.nbw.searchmediaapp.data.repository.MediaRepositoryImpl
import com.nbw.searchmediaapp.databinding.ActivityMainBinding
import com.nbw.searchmediaapp.ui.viewmodel.MediaViewModel
import com.nbw.searchmediaapp.ui.viewmodel.MediaViewModelProviderFactory

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private lateinit var navController: NavController

    lateinit var mediaViewModel: MediaViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupBottomNavigationView()

        val mediaRepository = MediaRepositoryImpl()
        val viewModelFactory = MediaViewModelProviderFactory(mediaRepository, this)
        mediaViewModel = ViewModelProvider(this, viewModelFactory)[MediaViewModel::class.java]
    }

    private fun setupBottomNavigationView() {
        val host = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment? ?: return
        navController = host.navController
        binding.bottomNavigationView.setupWithNavController(navController)
    }
}