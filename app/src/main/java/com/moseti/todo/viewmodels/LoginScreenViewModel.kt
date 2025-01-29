package com.moseti.todo.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.moseti.todo.db.Task
import com.moseti.todo.db.TaskDao
import com.moseti.todo.db.User
import com.moseti.todo.db.UserDao
import kotlinx.coroutines.launch
import java.util.UUID

class LoginScreenViewModel(private val userDao: UserDao, private val taskDao: TaskDao) :
    ViewModel() {

    var userEmail = mutableStateOf("moseti")
    var userPassword = mutableStateOf("1234")

    fun signUp(onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            val existingUser = userDao.getUserByEmail(userEmail.value)
            if (existingUser == null) {
                val newUser = User(
                    email = userEmail.value,
                    password = userPassword.value
                )
                userDao.insertUser(newUser)
                // Insert default tasks for the new user
                insertDefaultTasksForUser(userEmail.value)
                onSuccess()
            } else {
                onError("User with this email already exists")
            }
        }
    }

    private suspend fun insertDefaultTasksForUser(email: String) {
        val defaultTasks = listOf(
            Task(
                id = UUID.randomUUID(),
                ownerEmail = email,
                title = "Welcome to ToDo",
                description = "This is your first task!",
                freq = "daily",
                dueDate = "2023-12-31",
                priority = "High"
            ),
            Task(
                id = UUID.randomUUID(),
                ownerEmail = email,
                title = "Explore Features",
                description = "Check out all the features of this app.",
                freq = "weekly",
                dueDate = "2023-12-31",
                priority = "Low"
            )
        )
        taskDao.insertTasks(defaultTasks)
    }

    fun login(onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            val user = userDao.getUserByEmail(userEmail.value)
            when {
                user == null -> {
                    onError("User does not exist")
                }

                user.password != userPassword.value -> {
                    onError("Invalid password")
                }
                else -> {
                    onSuccess()
                }
            }
        }
    }
}

class LoginScreenViewModelFactory(private val userDao: UserDao, private val taskDao: TaskDao) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginScreenViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return LoginScreenViewModel(userDao, taskDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}