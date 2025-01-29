package com.moseti.todo.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class MainActivityViewModel : ViewModel() {
    var showDialog = mutableStateOf(false)
        private set

    fun setDialogState(bool: Boolean) {
        showDialog.value = bool
    }
}