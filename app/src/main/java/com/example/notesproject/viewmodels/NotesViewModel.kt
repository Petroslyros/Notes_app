package com.example.notesproject.viewmodels

import android.content.Context
import com.example.notesproject.database.Note
import com.example.notesproject.database.NotesDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


//moderator between IU and data
class NotesViewModel(var context: Context) {

    suspend fun fetchAllNotes(context: Context): List<Note> {
        val db = NotesDatabase.getDatabase(context)
        val notes = db.getNoteDao().getAllNotes()
        return notes
    }

    fun searchNotes(context: Context, query: String, callback: (List<Note>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val db = NotesDatabase.getDatabase(context)
            val notes = db.getNoteDao().searchNotes(query)
            withContext(Dispatchers.Main) {
                callback(notes)
            }
        }
    }


    fun addNote(context: Context,title: String, note: String, importance: Int, callback: () -> Unit) {
        if (note.isBlank() || title.isBlank()) {
            throw IllegalArgumentException("Note cannot be blank")
        }


        val newNote = Note(title = title, text = note,importance = importance ) //URGENCY OF NOTE AS WELL
        CoroutineScope(Dispatchers.IO).launch {

            val db = NotesDatabase.getDatabase(context)
            db.getNoteDao().insert(newNote)
            withContext(Dispatchers.Main) {
                callback()
            }
        }
    }

}