package com.amircodeing.mvvm.presentation.home


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.amircodeing.mvvm.data.local.model.Note
import com.amircodeing.mvvm.data.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow


// Annotation to indicate that this ViewModel is managed by Hilt for dependency injection
@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: NoteRepository) : ViewModel() {


    private val _homeEventsChannel = Channel<HomeEvents>()
    val homeViewChannel = _homeEventsChannel.receiveAsFlow()
    val _notes = MutableStateFlow<List<Note>>(emptyList())
    val note = _notes.asStateFlow()

    init {
        getNotes()
    }

    private fun getNotes() {
        viewModelScope.launch {
            repository.getNotes().collectLatest {
                _notes.emit(it)
            }
        }
    }

    fun fabClicked() {
        viewModelScope.launch {
            _homeEventsChannel.send(HomeEvents.NavigateToNoteFragment)
        }
    }

    sealed class HomeEvents() {
       data object NavigateToNoteFragment: HomeEvents(){

       }
    }
}

