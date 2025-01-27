package com.moseti.todo.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.moseti.todo.db.Task
import com.moseti.todo.db.TaskDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class AddTasksViewModel(private val taskDao: TaskDao) : ViewModel() {
    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> get() = _tasks

    var title by mutableStateOf("")
        private set
    var description by mutableStateOf("")
        private set
    var frequency by mutableStateOf("daily")
        private set
    var dueDate by mutableStateOf("")
        private set
    var priority by mutableStateOf(false)
        private set

    fun updateTitle(newTitle: String) {
        title = newTitle
    }

    fun updateDescription(newDescription: String) {
        description = newDescription
    }

    fun updateFrequency(newFrequency: String) {
        frequency = newFrequency
    }

    fun updateDueDate(newDueDate: String) {
        dueDate = newDueDate
    }

    fun updatePriority(newPriority: Boolean) {
        priority = newPriority
    }

    fun loadTasks(email: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val userTasks = taskDao.getTasksByUser(email)
            _tasks.value = userTasks
        }
    }

    // Add a task to the database or task list
    fun addTask(ownerEmail: String, taskDao: TaskDao) {
        viewModelScope.launch {
            val task = Task(
                id = UUID.randomUUID(),
                ownerEmail = ownerEmail,
                title = title,
                description = description,
                freq = frequency,
                dueDate = dueDate,
                priority = if (priority) "High" else "Low"
            )
            taskDao.insertTask(task)
            loadTasks(email = ownerEmail)
        }
    }

    fun deleteTask(taskId: UUID, email: String) {
        viewModelScope.launch {
            taskDao.deleteTask(taskId)
            loadTasks(email)
        }
    }

    fun markTaskAsCompleted(taskId: UUID, email: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val task = taskDao.getTaskById(taskId)
            if (task != null) {
                val updatedTask = task.copy(description = "${task.description} (Completed)")
                taskDao.insertTask(updatedTask) // Reinsert the updated task
                loadTasks(email) // Reload tasks for the user
            }
        }
    }

    fun editTask(task: Task) {
        updateTitle(task.title)
        updateDescription(task.description)
        updateFrequency(task.freq)
        updateDueDate(task.dueDate)
        updatePriority(task.priority == "High")
    }

    fun clearEntries() {
        updateTitle("")
        updatePriority(false)
        updateFrequency("")
        updateDescription("")
        updateDueDate("")
    }
}

// ViewModel Factory
class AddTasksViewModelFactory(private val taskDao: TaskDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddTasksViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddTasksViewModel(taskDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}