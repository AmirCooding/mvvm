package com.amircodeing.mvvm.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.DateFormat

@Entity(tableName = "note_table")
data class Note(
    @PrimaryKey(autoGenerate = true)
   var id: Int = 0,
   var title: String,
   var description: String,
    var isFavorite: Boolean,
     var date: Long = System.currentTimeMillis()
) {
    val createDate: String get() = DateFormat.getDateInstance().format(date)



}
