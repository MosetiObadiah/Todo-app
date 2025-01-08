package com.moseti.todo.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "Email") val email: String?,
    @ColumnInfo(name = "Password") val password: String?
)
