package com.moseti.todo.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import java.util.UUID

@Dao
interface TaskDao {
    @Query("SELECT * FROM Tasks WHERE ownerEmail = :email")
    suspend fun getTasksByUser(email: String): List<Task>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task)

    @Query("DELETE FROM Tasks WHERE id = :taskId")
    suspend fun deleteTask(taskId: UUID)

    @Query("DELETE FROM Tasks WHERE ownerEmail = :email")
    suspend fun deleteAllTasksForUser(email: String)

    @Query("SELECT * FROM Tasks WHERE id = :taskId")
    suspend fun getTaskById(taskId: UUID): Task?
}