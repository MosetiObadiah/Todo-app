package com.moseti.todo.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

data class Task(
    val title: String,
    val description: String,
    val freq: String,
    val dueDate: String,
    val priority: String
)

class AddTasksViewModel : ViewModel() {

    var title by mutableStateOf("New Task")
        private set
    var description by mutableStateOf("This is a sample description")
        private set
    var frequency by mutableStateOf("daily")
        private set
    var dueDate by mutableStateOf("")
        private set
    var priority by mutableStateOf("Low")
        private set


    private val _myTasks = mutableListOf<Task>()
    val myTasks: List<Task> get() = _myTasks

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

    fun updatePriority(newPriority: String) {
        priority = newPriority
    }

    fun addTask() {
        val newTask = Task(title, description, frequency, dueDate, priority)
        _myTasks.add(newTask)
        println("New task added: $newTask")
    }
}