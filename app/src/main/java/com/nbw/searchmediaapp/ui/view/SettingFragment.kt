package com.nbw.searchmediaapp.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.nbw.searchmediaapp.R
import com.nbw.searchmediaapp.databinding.FragmentSettingBinding
import com.nbw.searchmediaapp.ui.viewmodel.MediaViewModel
import com.nbw.searchmediaapp.utils.Sort
import kotlinx.coroutines.launch

class SettingFragment : Fragment() {

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    private lateinit var mediaViewModel: MediaViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mediaViewModel = (activity as MainActivity).mediaViewModel

        saveSettings()
        loadSettings()
    }

    private fun saveSettings() {
        binding.rgSort.setOnCheckedChangeListener { _, checkedId ->
            val value = when (checkedId) {
                R.id.rb_accuracy -> Sort.ACCURACY.value
                R.id.rb_recency -> Sort.RECENCY.value
                else -> return@setOnCheckedChangeListener
            }
            mediaViewModel.saveSortMode(value)
        }
    }

    private fun loadSettings() {
        lifecycleScope.launch {
            val buttonId = when (mediaViewModel.getSortMode()) {
                Sort.ACCURACY.value -> R.id.rb_accuracy
                Sort.RECENCY.value -> R.id.rb_recency
                else -> return@launch
            }
            binding.rgSort.check(buttonId)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}