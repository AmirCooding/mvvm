package com.amircodeing.mvvm.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.amircodeing.mvvm.data.local.dao.NoteDao
import com.amircodeing.mvvm.data.local.model.Note

/**
 * Version : indicates the Version of the database,
 * which must be increases with every change in database,otherwise the data will be lost
 */
@Database(entities = [Note:: class] , version = 1)
 abstract class NoteDatabase: RoomDatabase() {
  abstract  fun noteDao() : NoteDao
}