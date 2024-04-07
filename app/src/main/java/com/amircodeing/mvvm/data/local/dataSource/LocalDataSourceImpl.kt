package com.amircodeing.mvvm.data.local.dataSource

import com.amircodeing.mvvm.data.local.dao.NoteDao
import com.amircodeing.mvvm.data.local.model.Note
import com.amircodeing.mvvm.data.local.model.helper.SortBy
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

// Implementation of the LocalDataSource interface.
// This class is responsible for interacting with the local database to fetch notes.
class LocalDataSourceImpl @Inject constructor(
    // noteDao: An instance of NoteDao to access the database operations related to notes.
    private val noteDao: NoteDao
) : LocalDataSource {

    // Overrides the getNotes function from the LocalDataSource interface.
    // Uses the noteDao to retrieve a list of Note objects from the local database.
    override fun getNotes(search: String, isFavorite: Boolean, sortBy: SortBy): Flow<List<Note>> {
        // Call the getNotes() method on noteDao to fetch notes from the database.
        return noteDao.getNotes(search, isFavorite, sortBy)
    }

}