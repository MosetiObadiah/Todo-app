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

    var currentTaskId by mutableStateOf<UUID?>(null)
        private set

    var isEditing by mutableStateOf(false)
        private set

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

    fun saveTask(ownerEmail: String) {
        viewModelScope.launch {
            val task = Task(
                id = currentTaskId ?: UUID.randomUUID(), // Use existing ID if editing
                ownerEmail = ownerEmail,
                title = title,
                description = description,
                freq = frequency,
                dueDate = dueDate,
                priority = if (priority) "High" else "Low"
            )
            taskDao.insertTask(task) // Insert or update the task
            loadTasks(ownerEmail) // Reload tasks
            clearEntries()
            if (isEditing) {
                resetEditingState() // Reset editing state after saving
            }
        }
    }

    fun startEditing(task: Task) {
        currentTaskId = task.id
        isEditing = true
        // Populate fields with task data
        title = task.title
        description = task.description
        frequency = task.freq
        dueDate = task.dueDate
        priority = task.priority == "High"
    }

    private fun resetEditingState() {
        currentTaskId = null
        isEditing = false
        clearEntries()
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

    fun archiveTask(id: UUID, ownerEmail: String) {

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
        title = ""
        description = ""
        frequency = "daily"
        dueDate = ""
        priority = false
        currentTaskId = null
        isEditing = false
    }
}

// ViewModel Factory
class AddTasksViewModelFactory(private val taskDao: TaskDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddTasksViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return AddTasksViewModel(taskDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}