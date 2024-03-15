package com.amircodeing.mvvm.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.amircodeing.mvvm.data.local.model.Note

@Dao
interface NoteDao {
    /**
    REPLACE: When this strategy is used, if there is a conflict
    (such as trying to insert a row with a primary key that already exists in the table),
    the existing row in the database
    will be replaced with the new row. Essentially, the new data overwrites the old data in case of a conflict.
     *
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
   suspend fun insertNote(note: Note):Long

    @Query("select * from  note_table")
    fun getNotes() : List<Note>

}