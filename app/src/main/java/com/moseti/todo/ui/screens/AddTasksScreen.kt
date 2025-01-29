package com.moseti.todo.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moseti.todo.viewmodels.AddTasksViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTasks(
    addTaskviewmodel: AddTasksViewModel,
    onSave: () -> Unit
) {
    val radioOptions = listOf("daily", "weekly", "monthly")
    val selectedFrequency = addTaskviewmodel.frequency

    var showDatePicker by remember { mutableStateOf(false) }
    // State to hold the selected date range
    // Set default due date as current date
    val currentDateMillis = System.currentTimeMillis()
    // Convert ViewModel's string date to millis
    val initialDate = try {
        SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
            .parse(addTaskviewmodel.dueDate)
            ?.time ?: System.currentTimeMillis()
    } catch (e: Exception) {
        System.currentTimeMillis()
    }

    var selectedDueDate by remember { mutableLongStateOf(initialDate) }

    // Update both states when date changes
    LaunchedEffect(selectedDueDate) {
        addTaskviewmodel.updateDueDate(convertMillisToDate(selectedDueDate))
    }

    Column {
        OutlinedTextField(
            value = addTaskviewmodel.title,
            onValueChange = {
                addTaskviewmodel.updateTitle(it)
            },
            modifier = Modifier
                .padding(5.dp, 5.dp, 5.dp, 0.dp)
                .fillMaxWidth(),
            label = { Text("Title") }
        )

        OutlinedTextField(
            value = addTaskviewmodel.description,
            onValueChange = {
                addTaskviewmodel.updateDescription(it)
            },
            modifier = Modifier
                .padding(5.dp, 0.dp, 5.dp, 0.dp)
                .fillMaxWidth(),
            label = { Text("Description") }
        )

        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .selectableGroup()
        ) {
            radioOptions.forEach { text ->
                Row(
                    Modifier
                        .height(56.dp)
                        .selectable(
                            selected = (text == addTaskviewmodel.frequency),
                            onClick = { addTaskviewmodel.updateFrequency(text) },
                            role = Role.RadioButton
                        )
                        .padding(bottom = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (text == addTaskviewmodel.frequency),
                        onClick = null
                    )
                    Text(
                        text = text,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = 2.dp, end = 18.dp)
                    )
                }
            }
        }

        Button(
            onClick = { showDatePicker = true },
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(4.dp)
        ) {
            Text("Due Date")
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Due date " + addTaskviewmodel.dueDate.toString()
            )
        }

        // Show the DatePickerDialog when showDatePicker is true
        if (showDatePicker) {
            val datePickerState = rememberDatePickerState()
            DatePickerDialog(
                onDismissRequest = {
                    showDatePicker = false
                },
                confirmButton = {
                    TextButton(onClick = {
                        selectedDueDate = datePickerState.selectedDateMillis!!
                        showDatePicker = false
                    }) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showDatePicker = false
                    }) {
                        Text("Cancel")
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "High Priority",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
            )

            Switch(
                checked = addTaskviewmodel.priority,
                onCheckedChange = {
                    addTaskviewmodel.updatePriority(it)
                },
                thumbContent = if (addTaskviewmodel.priority) {
                    {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = null,
                            modifier = Modifier.size(SwitchDefaults.IconSize),
                        )
                    }
                } else {
                    null
                }
            )
        }
    }
}

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}