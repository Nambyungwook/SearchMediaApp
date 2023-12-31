package com.nbw.searchmediaapp.ui.view

import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.nbw.searchmediaapp.databinding.FragmentSearchMediaBinding
import com.nbw.searchmediaapp.ui.adapter.MediaAdapter
import com.nbw.searchmediaapp.ui.viewmodel.MediaViewModel
import com.nbw.searchmediaapp.utils.Constants.SEARCH_TIME_DELAY
import com.nbw.searchmediaapp.utils.collectLatestStateFlow

class SearchMediaFragment : Fragment() {

    private var _binding: FragmentSearchMediaBinding? = null
    private val binding get() = _binding!!

    private lateinit var mediaViewModel: MediaViewModel
    private lateinit var mediaAdapter: MediaAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchMediaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mediaViewModel = (activity as MainActivity).mediaViewModel

        initRecyclerView()
        search()

        collectLatestStateFlow(mediaViewModel.searchMediaResult) {
            mediaAdapter.submitData(it)
        }

        mediaViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
        }
    }

    private fun initRecyclerView() {
        mediaAdapter = MediaAdapter()
        binding.rvSearchResult.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            addItemDecoration(
                DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
            )
            adapter = mediaAdapter
        }
        mediaAdapter.setOnItemClickListener { currentMedia ->
            val action = SearchMediaFragmentDirections.actionFragmentSearchToMedia(currentMedia)
            findNavController().navigate(action)
        }
    }

    private fun search() {
        val startTime = System.currentTimeMillis()
        var endTime: Long

        binding.etSearch.text =
            Editable.Factory.getInstance().newEditable(mediaViewModel.query)

        binding.etSearch.addTextChangedListener { text: Editable? ->
            endTime = System.currentTimeMillis()
            if (endTime - startTime >= SEARCH_TIME_DELAY) {
                text?.let {
                    val query = it.toString().trim()
                    if (query.isNotEmpty()) {
                        mediaViewModel.searchMedia(query)
                        mediaViewModel.query = query
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}