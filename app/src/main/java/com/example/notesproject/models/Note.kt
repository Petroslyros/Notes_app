package com.example.notesproject.models

import androidx.room.Entity
import androidx.room.PrimaryKey


//this will create an entity table
//entity class is a data class
//annotating a class will create a table to store our notes

@Entity(tableName = "notes_table")
data class Note(
    val title: String,
    val text: String,
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var importance: Int,
    val date : String
    )