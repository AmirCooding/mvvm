package com.amircodeing.mvvm.presentation.home

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.amircodeing.mvvm.R
import com.amircodeing.mvvm.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var homeAdapter: HomeAdaptor
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        viewModel.getNotes()
        homeAdapter = HomeAdaptor()
        binding.rvHome.apply {
            adapter = homeAdapter
            layoutManager = LinearLayoutManager(binding.root.context)
        }
        //observers()
        observedEvent()
        binding.fabAddNote.setOnClickListener {
            viewModel.fabClicked()
        }
    }

    private fun observedEvent() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.homeViewChannel.collect {event ->
                    when (event) {
                        is HomeViewModel.HomeEvents.NavigateToNoteFragment -> {
                            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToNoteFragment())
                        }
                        is HomeViewModel.HomeEvents.SendNotes -> homeAdapter.submitList(event.notes)

                    }
                }
            }
        }
    }

/*     private fun observers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.notes.collectLatest {
                    homeAdapter.submitList(it)
                }
            }
        }
    } */
}
