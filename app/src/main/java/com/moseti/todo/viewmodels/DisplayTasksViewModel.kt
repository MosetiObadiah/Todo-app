package com.moseti.todo.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class DisplayTasksViewModel() :
    ViewModel() {
    var allSelected = mutableStateOf(false)
        private set
    var dailySelected = mutableStateOf(false)
    var weeklySelected = mutableStateOf(false)
    var monthlySelected = mutableStateOf(false)

    fun updateAllSelected(bool : MutableState<Boolean>) {
        allSelected = bool
    }
    fun updateDailySelected(bool : MutableState<Boolean>) {
        dailySelected = bool
    }
    fun updateWeeklySelected(bool : MutableState<Boolean>) {
        weeklySelected = bool
    }
    fun updateMonthlySelected(bool : MutableState<Boolean>) {
        monthlySelected = bool
    }

}

class DisplayTasksViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DisplayTasksViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DisplayTasksViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}