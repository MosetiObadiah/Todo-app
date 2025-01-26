package com.moseti.todo.db

import androidx.room.*
import java.util.*

@Entity(
    tableName = "Netizen",
    indices = [Index(value = ["email"], unique = true)]
)
data class User(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val email: String,
    val password: String
)

@Entity(
    tableName = "Tasks",
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = ["email"],
        childColumns = ["ownerEmail"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["ownerEmail"])]
)
data class Task(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    val ownerEmail: String,
    val title: String,
    val description: String,
    val freq: String, // daily, weekly, monthly
    val dueDate: String,
    val priority: String // Low, Medium, High
)

data class UserWithTasks(
    @Embedded val user: User,
    @Relation(
        parentColumn = "email",
        entityColumn = "ownerEmail"
    )
    val tasks: List<Task>
)

class Converters {
    @TypeConverter
    fun fromUUID(uuid: UUID?): String? = uuid?.toString()

    @TypeConverter
    fun toUUID(uuid: String?): UUID? = uuid?.let { UUID.fromString(it) }
}