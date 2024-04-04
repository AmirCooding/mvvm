package com.amircodeing.mvvm.presentation.home


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import com.amircodeing.mvvm.data.local.model.Note
import com.amircodeing.mvvm.data.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import com.amircodeing.mvvm.data.local.prefs.PerfManager
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow


// Annotation to indicate that this ViewModel is managed by Hilt for dependency injection
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: NoteRepository,
    //The live state that remains in the fragment
    // All fragment activity is stored in the save state handle
    private val state: SavedStateHandle,
    private val prefsManager: PerfManager
) : ViewModel() {


    /*     val _notes = MutableStateFlow<List<Note>>(emptyList())
        val note = _notes.asStateFlow() */

    private val _homeEventsChannel = Channel<HomeEvents>()
    val homeViewChannel = _homeEventsChannel.receiveAsFlow()

    /*     init {
            getNotes()
        }

        private fun getNotes() {
            viewModelScope.launch {
                repository.getNotes().collectLatest {
                    _notes.emit(it)
                }
            }
        } */

    // change after Dynamic Query for Search and Favorite and Sort
    // send empty request to database and get a list of all data as response
    val searchQuery = state.getLiveData("search", "")

    private val notes =
        combine(searchQuery.asFlow(), prefsManager.readSearchNote) { query, filters ->
            Pair(
                query,
                filters
            )
        }.flatMapLatest { (query, filters) ->
            repository.getNotes(query, filters.isFavorite, filters.sortBy)
        }

    fun fabClicked() {
        viewModelScope.launch {
            _homeEventsChannel.send(HomeEvents.NavigateToNoteFragment)
        }
    }

    sealed class HomeEvents() {
        data object NavigateToNoteFragment : HomeEvents() {

        }
    }
}

