package com.moseti.todo.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.moseti.todo.db.AppDatabase.Companion.getInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID
import java.util.concurrent.Executors

@Database(entities = [User::class, Task::class], version = 2)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun taskDao(): TaskDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext, AppDatabase::class.java, "app_database"
                ).fallbackToDestructiveMigration()
                    .addCallback(DatabaseCallback(context))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

private class DatabaseCallback(
    private val context: Context
) : RoomDatabase.Callback() {
    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        // Use a coroutine to insert data
        Executors.newSingleThreadExecutor().execute {
            val database = getInstance(context)
            val userDao = database.userDao()
            val taskDao = database.taskDao()

            // Insert prepopulated data
            CoroutineScope(Dispatchers.IO).launch {
                userDao.insertUser(User(email = "admin@example.com", password = "admin123"))
                taskDao.insertTask(
                    Task(
                        id = UUID.randomUUID(),
                        ownerEmail = "admin@example.com",
                        title = "Welcome Task",
                        description = "This is your first task!",
                        freq = "daily",
                        dueDate = "2023-12-31",
                        priority = "High"
                    )
                )
            }
        }
    }
}