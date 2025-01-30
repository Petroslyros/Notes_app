//package com.example.notesproject.DAO
//
//import androidx.room.Dao
//import androidx.room.Delete
//import androidx.room.Insert
//import androidx.room.Query
//import androidx.room.Update
//import com.example.notesproject.models.User
//
//@Dao
//interface UserDAO {
//    //we want insert and delete to be run on the background thread so that while inserting and deleting our app wong hang
//    @Insert
//    suspend fun insert(user: User)
//
//    @Delete
//    suspend fun delete(user: User)
//
//    @Update
//    suspend fun updateUser(user: User)
//
//    @Query("SELECT * FROM user_table")
//    suspend fun getAllUsers(): List<User>
//
//    @Query("Select * from user_table where username Like :userName and password LIKE :pass")
//    suspend fun getUser(userName:String, pass : String): Boolean
//}