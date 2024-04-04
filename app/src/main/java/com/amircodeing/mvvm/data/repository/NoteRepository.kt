package com.amircodeing.mvvm.data.repository

import com.amircodeing.mvvm.data.local.dataSource.LocalDataSource
import com.amircodeing.mvvm.data.local.model.Note
import com.amircodeing.mvvm.data.local.prefs.SortBy
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Repository class responsible for providing data to the ViewModel.
 * It acts as an intermediary between the ViewModel and data sources (local and remote).
 *
 */
class NoteRepository @Inject constructor(
    // localDataSource: An instance of LocalDataSource to fetch notes from the  database.
    private val localDataSource: LocalDataSource
) {
    // Function to retrieve notes from the data source.
    // Returns: List of Note objects.

    /**
     * @getNotes rewriting to get the list and requested items at the same time, such as search or sorted by and favorite
     * first i add parameters to getNote Method
     * this time i'll start from Repository to reach DAO
     * then i send the parameters entered to the getNote() Methode as input parameters for the Database
     */
    fun getNotes(search : String , isFavorite : Boolean , sortBy: SortBy): Flow<List<Note>> {
        // Delegate the task of fetching notes to the localDataSource.
        return localDataSource.getNotes(search,isFavorite,sortBy)
    }
}