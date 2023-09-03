package com.example.project2_todolistapp.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date


// lets understand about room database.
// this is divided into four parts; part (1) is here
// 1. Entity -> Table Structure -> how your data is stored.

@Entity(tableName = "notes_table")          // to let the room db know that its an entity and a table has to be created from this.
data class Todo(
    @PrimaryKey(autoGenerate = true)        // here, we are setting the primary key for the table.
    val id: Int = 0,
    var isDone: Boolean = false,
    val title: String,
    val desc: String,
    val date: Date
)

