package com.amircodeing.mvvm.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.DateFormat

@Entity(tableName = "note_table")
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    val isFavorite: Boolean,
    val date: Long = System.currentTimeMillis()
) {
    val createDate: String get() = DateFormat.getDateInstance().format(date)
}
