package com.amircodeing.mvvm.presentation.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.amircodeing.mvvm.R
import com.amircodeing.mvvm.data.local.model.helper.SortBy
import com.amircodeing.mvvm.databinding.FragmentHomeBinding
import com.amircodeing.mvvm.utils.onQueryTextChanged
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var homeAdapter: HomeAdaptor

    //Implement Search 3 Step
    private lateinit var searchView: SearchView

    // Implement >Toolbar 1 step
    private lateinit var menuHost: MenuHost

    // Implement >Toolbar 3 step
    private val menuProvider: MenuProvider = object : MenuProvider {
        // create menu
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menuInflater.inflate(R.menu.menu_home, menu)
            implementSearchView(menu)
            // save last action of Favorite
            implementFavoriteState(menu)
        }


        // access to menus components
        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            return when (menuItem.itemId) {
                R.id.action_show_favorite -> {
                    menuItem.isChecked = !menuItem.isChecked
                    viewModel.onFavoriteIsSelected(menuItem.isChecked)
                    true
                }

                R.id.action_sort_name -> {
                    viewModel.onSortedIsSelected(SortBy.NAME)
                    true
                }

                R.id.action_sort_date -> {
                    viewModel.onSortedIsSelected(SortBy.DATE)
                    true
                }

                else -> false
            }
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        // Implement >Toolbar 5 step
        menuHost = requireActivity()
        // Implement >Toolbar 2 step
        val activity = requireActivity()
        if (activity is AppCompatActivity) activity.setSupportActionBar(binding.homeToolbar)
        // Implement >Toolbar 4 step
        menuHost.addMenuProvider(menuProvider, viewLifecycleOwner, Lifecycle.State.RESUMED)
        viewModel.getNotes()
        homeAdapter = HomeAdaptor()
        binding.rvHome.apply {
            adapter = homeAdapter
            layoutManager = LinearLayoutManager(binding.root.context)
        }
        observedEvent()
        binding.fabAddNote.setOnClickListener {
            viewModel.fabClicked()
        }
    }

    private fun observedEvent() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.homeViewChannel.collect { event ->
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

    override fun onDestroy() {
        super.onDestroy()
        searchView.setOnQueryTextListener(null)
    }

    private fun implementSearchView(menu: Menu) {
        // save data from Search Field
        //Implement Search 1 Step => UiTools.kt
        //Implement Search 2 Step => get Id
        val searchItem = menu.findItem(R.id.action_search)
        //Implement Search 4 Step => set ActionView
        searchView = searchItem.actionView as SearchView
        //Implement Search 5 Step
        val pendingQuery = viewModel.searchQuery.value
        //Implement Search 6 Step
        if (!pendingQuery.isNullOrEmpty()) {
            searchItem.expandActionView()
            searchView.setQuery(pendingQuery, false)
        }
        //Implement Search 7 Step
        searchView.onQueryTextChanged {
            viewModel.searchQuery.value = it
        }
    }
        private fun implementFavoriteState(menu: Menu) {
            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                    viewModel.showFavorite.collectLatest {
                        menu.findItem(R.id.action_show_favorite).isChecked = it
                    }
                }
            }
        }

}

