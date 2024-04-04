package com.amircodeing.mvvm.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.amircodeing.mvvm.data.local.model.Note
import com.amircodeing.mvvm.data.local.prefs.SortBy
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    /**
     * REPLACE: When this strategy is used, if there is a conflict
     * (such as trying to insert a row with a primary key that already exists in the table),
     * the existing row in the database
     * will be replaced with the new row. Essentially, the new data overwrites the old data in case of a conflict.
     **/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note): Long

    fun getNotes(search: String, isFavorite: Boolean, sortBy: SortBy) =
        when (sortBy) {
            SortBy.NAME -> getNotesByName(search, isFavorite)
            SortBy.DATE -> getNotesByDate(isFavorite, search)
        }

    // there must return  Flow<List<Note>>
    /**
     * Retrieves notes from the note_table based on the specified parameters.
     *
     * @param favorite 1 if the note is marked as favorite, 0 otherwise
     * @param search   keyword to search for in note titles or descriptions
     * @return A list of notes matching the specified criteria
     */
    @Query(
        "SELECT * FROM note_table WHERE isFavorite = CASE WHEN :favorite = 1 THEN 1 ELSE isFavorite END " +
                "AND (title LIKE '%' || :search || '%' OR description LIKE '%' || :search || '%' ) order by date asc"
    )
    fun getNotesByDate(favorite: Boolean, search: String): Flow<List<Note>>

    @Query(
        "SELECT * FROM note_table WHERE isFavorite = CASE WHEN :favorite = 1 THEN 1 ELSE isFavorite END " +
                "AND (title LIKE '%' || :search || '%' OR description LIKE '%' || :search || '%' ) order by title asc"
    )
    fun getNotesByName(search: String, favorite: Boolean): Flow<List<Note>>


    /*    @Query("select * from  note_table")
       fun getNotes(search: String, isFavorite: Boolean, sortBy: SortBy): Flow<List<Note>>
    */
}