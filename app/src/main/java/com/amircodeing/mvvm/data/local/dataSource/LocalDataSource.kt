package com.amircodeing.mvvm.data.local.dataSource
import com.amircodeing.mvvm.data.local.model.Note
import com.amircodeing.mvvm.data.local.prefs.SortBy
import kotlinx.coroutines.flow.Flow

// Interface representing a local data source for retrieving notes
interface LocalDataSource {

    // Function to get notes from the local data source
    // Returns: List of Note objects
    fun getNotes(search: String, isFavorite: Boolean, sortBy: SortBy): Flow<List<Note>>
}