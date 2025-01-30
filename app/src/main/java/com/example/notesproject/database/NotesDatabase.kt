package com.example.notesproject.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.notesproject.DAO.NotesDAO

import com.example.notesproject.models.Note


@Database(entities = [Note::class], version = 5)
abstract class NotesDatabase : RoomDatabase() {

    //access dao through database
    abstract fun getNoteDao(): NotesDAO
//    abstract fun getUserDao(): UserDAO

    //single instance of room database so it prevents multiple opening of database at same time

    companion object {
        @Volatile
        private var INSTANCE: NotesDatabase? = null
        fun getDatabase(context: Context): NotesDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NotesDatabase::class.java,
                    "note_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}