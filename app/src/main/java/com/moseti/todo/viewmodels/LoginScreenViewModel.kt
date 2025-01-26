package com.moseti.todo.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.moseti.todo.db.TaskDao
import com.moseti.todo.db.User
import com.moseti.todo.db.UserDao
import kotlinx.coroutines.launch

class LoginScreenViewModel(private val userDao: UserDao) : ViewModel() {

    var userEmail = mutableStateOf("")
    var userPassword = mutableStateOf("")

    // Function to sign up a new user
    fun signUp(onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            val existingUser = userDao.getUserByEmail(userEmail.value)
            if (existingUser == null) {
                // User doesn't exist, create a new account
                val newUser = User(
                    email = userEmail.value,
                    password = userPassword.value
                )
                userDao.insertUser(newUser)
                onSuccess() // Notify success
            } else {
                // User already exists
                onError("User with this email already exists")
            }
        }
    }

    // Function to log in an existing user
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

class LoginScreenViewModelFactory(private val userDao: UserDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginScreenViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginScreenViewModel(userDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}