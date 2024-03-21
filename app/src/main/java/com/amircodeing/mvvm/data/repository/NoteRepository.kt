package com.amircodeing.mvvm.data.repository

import com.amircodeing.mvvm.data.local.dataSource.LocalDataSource
import com.amircodeing.mvvm.data.local.model.Note
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Repository class responsible for providing data to the ViewModel.
 * It acts as an intermediary between the ViewModel and data sources (local and remote).
 *
 */
class NoteRepository @Inject constructor(
    // localDataSource: An instance of LocalDataSource to fetch notes from the local database.
    private val localDataSource: LocalDataSource
) {
    // Function to retrieve notes from the data source.
    // Returns: List of Note objects.
    fun getNotes(): Flow<List<Note>> {
        // Delegate the task of fetching notes to the localDataSource.
        return localDataSource.getNotes()
    }
}