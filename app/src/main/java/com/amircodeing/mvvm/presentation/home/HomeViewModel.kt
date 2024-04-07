package com.amircodeing.mvvm.presentation.home


import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import com.amircodeing.mvvm.data.local.model.Note
import com.amircodeing.mvvm.data.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import com.amircodeing.mvvm.data.local.model.helper.SortBy
import com.amircodeing.mvvm.data.local.prefs.PerfManager
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
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

    private val _homeEventsChannel = Channel<HomeEvents>()
    val homeViewChannel = _homeEventsChannel.receiveAsFlow()
    private val _showFavorite = MutableStateFlow(false)
    val showFavorite : StateFlow<Boolean> = _showFavorite

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


     init {
         giveMeFavoriteAction()


     }

    private fun giveMeFavoriteAction() {
      viewModelScope.launch { prefsManager.readFavorite.collectLatest {
          _showFavorite.emit(it)
      } }
    }

    fun getNotes() {
        viewModelScope.launch {
            notes.collectLatest {
                _homeEventsChannel.send(HomeEvents.SendNotes(it))
            }
        }
    }

    fun onFavoriteIsSelected(checked: Boolean) {
        viewModelScope.launch { prefsManager.saveFavorite(checked) }

    }

    fun onSortedIsSelected(sort: SortBy) {
  viewModelScope.launch { prefsManager.saveSortOrder(sort) }
    }


    sealed class HomeEvents() {
        data object NavigateToNoteFragment : HomeEvents()
        data class SendNotes(val notes: List<Note>) : HomeEvents()

    }
}


