package com.example.notesproject.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update


@Dao
interface NotesDAO {
    //we want insert and delete to be run on the background thread so that while inserting and deleting our app wong hang
    @Insert(onConflict = OnConflictStrategy.IGNORE) // ignores duplicates in notes table
    suspend fun insert(note: Note)

    @Delete
    suspend fun delete(note: Note)

    @Update
    suspend fun updateNote(note: Note)

    @Query("SELECT * FROM notes_table ORDER BY id ASC")// it will select all data from our table according to id ascending order
    suspend fun getAllNotes(): List<Note>

    @Query("SELECT * FROM notes_table WHERE title LIKE '%' || :query || '%' OR text LIKE '%' || :query || '%' ORDER BY title ASC")
    suspend fun searchNotes(query: String): List<Note>

}