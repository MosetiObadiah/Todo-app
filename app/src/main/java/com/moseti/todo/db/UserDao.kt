package com.moseti.todo.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {
    @Query("SELECT * FROM Netizen")
    suspend fun getAllUsers(): List<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM Netizen WHERE email = :email")
    suspend fun getUserByEmail(email: String): User?

    @Query("DELETE FROM Netizen WHERE email = :email")
    suspend fun deleteUserByEmail(email: String)
}