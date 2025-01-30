//package com.example.notesproject.viewmodels
//
//import android.content.Context
//import com.example.notesproject.database.NotesDatabase
//import com.example.notesproject.models.Note
//import com.example.notesproject.models.User
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.withContext
//
//class UserViewModel(var context: Context) {
//
//    suspend fun fetchAllUsers(context: Context): List<User> {
//        val db = NotesDatabase.getDatabase(context)
//        val users = db.getUserDao().getAllUsers()
//        return users
//    }
//
//    suspend fun userExists(context: Context, user: User): Boolean {
//        val db = NotesDatabase.getDatabase(context)
//        val exists = db.getUserDao().getUser(user.username,user.password)
//        return exists
//    }
//
//    suspend fun insertUser(context: Context, u : User) {
//        val db = NotesDatabase.getDatabase(context)
//        val user = db.getUserDao().insert(u)
//
//    }
//
//}

