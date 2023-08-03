package com.nbw.searchmediaapp.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.nbw.searchmediaapp.databinding.FragmentFavoriteMediaBinding
import com.nbw.searchmediaapp.ui.adapter.MediaAdapter
import com.nbw.searchmediaapp.ui.viewmodel.MediaViewModel

class FavoriteMediaFragment : Fragment() {

    private var _binding: FragmentFavoriteMediaBinding? = null
    private val binding get() = _binding!!

    private lateinit var mediaViewModel: MediaViewModel
    private lateinit var mediaAdapter: MediaAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteMediaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mediaViewModel = (activity as MainActivity).mediaViewModel

        initRecyclerView()
        setupTouchHelper(view)

        mediaViewModel.favoriteMedias.observe(viewLifecycleOwner) {medias ->
            mediaAdapter.submitList(medias)
        }
    }

    private fun initRecyclerView() {
        mediaAdapter = MediaAdapter()
        binding.rvFavoriteMedias.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )
            adapter = mediaAdapter
        }
        mediaAdapter.setOnItemClickListener { currentMedia ->
            val action = FavoriteMediaFragmentDirections.actionFragmentFavoriteToMedia(currentMedia)
            findNavController().navigate(action)
        }
    }

    private fun setupTouchHelper(view: View) {
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val media = mediaAdapter.currentList[position]
                mediaViewModel.deleteMedia(media)
                Snackbar.make(view, "현재 미디어를 즐겨찾기에서 삭제했습니다.", Snackbar.LENGTH_SHORT).apply {
                    setAction("Undo") {
                        mediaViewModel.insertMedia(media)
                    }
                }.show()
            }
        }
        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.rvFavoriteMedias)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}