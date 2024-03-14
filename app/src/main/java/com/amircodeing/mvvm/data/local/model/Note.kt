package com.amircodeing.mvvm.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.DateFormat

@Entity(tableName = "note_table")
data class Note(
    @PrimaryKey(autoGenerate = true)
    private val id: Int = 0,
    private val title: String,
    private val description: String,
    private val isFavorite: Boolean,
    private val date: Long = System.currentTimeMillis()
) {
    val createDate: String get() = DateFormat.getDateInstance().format(date)
}
