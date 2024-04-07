package com.amircodeing.mvvm.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.amircodeing.mvvm.data.local.dao.NoteDao
import com.amircodeing.mvvm.data.local.model.Note
import com.amircodeing.mvvm.di.scops.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

/**
 * Version : indicates the Version of the database,
 * which must be increases with every change in database,otherwise the data will be lost
 */
@Database(entities = [Note::class], version = 1)
abstract class NoteDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao

    class CallBack @Inject constructor(
        private val database: Provider<NoteDatabase>,
    @ApplicationScope  private val applicationScope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            val noteDao = database.get().noteDao()
            // show prototype to users
            applicationScope.launch {
                noteDao.insertNote(

                    Note(
                        title = "test1",
                        description = "This is the test1 description",
                        isFavorite = true
                    )
                )
                noteDao.insertNote(

                    Note(
                        title = "test4",
                        description = "This is the test4 description",
                        isFavorite = false
                    )
                )
                noteDao.insertNote(
                    Note(
                        title = "test2",
                        description = "This is the test2 description",
                        isFavorite = false
                    )
                )
                noteDao.insertNote(
                    Note(
                        title = "test3",
                        description = "This is the test3 description",
                        isFavorite = true
                    )
                )
            }
        }
    }
}