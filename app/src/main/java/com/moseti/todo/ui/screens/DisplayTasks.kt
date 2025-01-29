package com.moseti.todo.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.moseti.todo.db.Task
import com.moseti.todo.viewmodels.AddTasksViewModel
import com.moseti.todo.viewmodels.DisplayTasksViewModel
import com.moseti.todo.viewmodels.DisplayTasksViewModelFactory
import com.moseti.todo.viewmodels.MainActivityViewModel

@Composable
fun ShowTasks(
    addTaskViewModel: AddTasksViewModel,
    innerPadding: PaddingValues,
    onEditTask: (Task) -> Unit,
    mainActivityVModel: MainActivityViewModel
) {
    val context = LocalContext.current
    val tasks by addTaskViewModel.tasks.collectAsState(initial = emptyList())
    val viewModel: DisplayTasksViewModel = viewModel(factory = DisplayTasksViewModelFactory())

    LazyColumn(
        modifier = Modifier.padding(innerPadding), contentPadding = PaddingValues(8.dp)
    ) {

        item{
            LazyRow (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ){
                item {
                    FilterChip(
                        onClick = { viewModel.allSelected.value = !viewModel.allSelected.value },
                        label = {
                            Text("All")
                        },
                        selected = viewModel.allSelected.value,
                        leadingIcon = if (viewModel.allSelected.value) {
                            {
                                Icon(
                                    imageVector = Icons.Filled.Check,
                                    contentDescription = "Done icon",
                                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                                )
                            }
                        } else {
                            null
                        },
                        modifier = Modifier.padding(5.dp)
                    )
                }
                item {
                    FilterChip(
                        onClick = { viewModel.dailySelected.value = !viewModel.dailySelected.value },
                        label = {
                            Text("Daily")
                        },
                        selected = viewModel.dailySelected.value,
                        leadingIcon = if (viewModel.dailySelected.value) {
                            {
                                Icon(
                                    imageVector = Icons.Filled.Check,
                                    contentDescription = "Done icon",
                                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                                )
                            }
                        } else {
                            null
                        },
                        modifier = Modifier.padding(5.dp)
                    )
                }
               item {
                   FilterChip(
                       onClick = { viewModel.weeklySelected.value = !viewModel.weeklySelected.value },
                       label = {
                           Text("weekly")
                       },
                       selected = viewModel.weeklySelected.value,
                       leadingIcon = if (viewModel.weeklySelected.value) {
                           {
                               Icon(
                                   imageVector = Icons.Filled.Check,
                                   contentDescription = "Done icon",
                                   modifier = Modifier.size(FilterChipDefaults.IconSize)
                               )
                           }
                       } else {
                           null
                       },
                       modifier = Modifier.padding(5.dp)
                   )
               }
                item {
                    FilterChip(
                        onClick = { viewModel.monthlySelected.value = !viewModel.monthlySelected.value },
                        label = {
                            Text("monthly")
                        },
                        selected = viewModel.monthlySelected.value,
                        leadingIcon = if (viewModel.monthlySelected.value) {
                            {
                                Icon(
                                    imageVector = Icons.Filled.Check,
                                    contentDescription = "Done icon",
                                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                                )
                            }
                        } else {
                            null
                        },
                        modifier = Modifier.padding(5.dp)
                    )
                }
               item {
                   FilterChip(
                       onClick = { viewModel.dailySelected.value = !viewModel.dailySelected.value },
                       label = {
                           Text("Priority")
                       },
                       selected = viewModel.dailySelected.value,
                       leadingIcon = if (viewModel.dailySelected.value) {
                           {
                               Icon(
                                   imageVector = Icons.Filled.Check,
                                   contentDescription = "Done icon",
                                   modifier = Modifier.size(FilterChipDefaults.IconSize)
                               )
                           }
                       } else {
                           null
                       },
                       modifier = Modifier.padding(5.dp)
                   )
               }
            }
        }
        items(tasks, key = { it.id }) { task ->
            TaskCard(
                task = task,
                onDeleteTask = { taskToDelete ->
                    addTaskViewModel.deleteTask(taskToDelete.id, taskToDelete.ownerEmail)
                    Toast.makeText(context, "Task deleted", Toast.LENGTH_SHORT).show()
                },
                onMarkCompleted = { taskToComplete ->
                    addTaskViewModel.markTaskAsCompleted(
                        taskToComplete.id, taskToComplete.ownerEmail
                    )
                    Toast.makeText(context, "Task marked as completed", Toast.LENGTH_SHORT).show()
                },
                onEditTask = { taskToEdit ->
                    addTaskViewModel.startEditing(taskToEdit)
                    mainActivityVModel.setDialogState(true)
                },
                onArchiveTask = { taskToArchive ->
                    addTaskViewModel.archiveTask(taskToArchive.id, taskToArchive.ownerEmail)
                    Toast.makeText(context, "Task archived", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskCard(
    task: Task,
    onDeleteTask: (Task) -> Unit,
    onMarkCompleted: (Task) -> Unit,
    onEditTask: (Task) -> Unit,
    onArchiveTask: (Task) -> Unit
) {
    val swipeState = rememberSwipeToDismissBoxState()

    // Determine the background content based on the swipe direction
    val (icon, alignment, color) = when (swipeState.dismissDirection) {
        SwipeToDismissBoxValue.EndToStart -> Triple(
            Icons.Default.Delete,
            Alignment.CenterEnd,
            MaterialTheme.colorScheme.errorContainer
        )
        SwipeToDismissBoxValue.StartToEnd -> Triple(
            Icons.Default.Email,
            Alignment.CenterStart,
            Color.Green.copy(alpha = 0.3f)
        )
        SwipeToDismissBoxValue.Settled -> Triple(
            Icons.Default.Delete,
            Alignment.CenterEnd,
            MaterialTheme.colorScheme.errorContainer
        )
    }

    // Handle swipe actions
    LaunchedEffect(swipeState.currentValue) {
        when (swipeState.currentValue) {
            SwipeToDismissBoxValue.EndToStart -> {
                onDeleteTask(task) // Swipe left to delete
                swipeState.reset() // Reset the state after the action
            }
            SwipeToDismissBoxValue.StartToEnd -> {
                onMarkCompleted(task) // Swipe right to mark as completed
                swipeState.reset() // Reset the state after the action
            }
            SwipeToDismissBoxValue.Settled -> {
                // do nothing
            }
        }
    }

    SwipeToDismissBox(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        state = swipeState,
        backgroundContent = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color),
                contentAlignment = alignment
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        },
        content = {
            // Main task card content
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                ),
                border = BorderStroke(1.dp, Color.Black),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 6.dp
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onEditTask(task)
                    },
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    // Title and Priority Icon
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = task.title,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        if (task.priority == "High") { // Show priority icon for high-priority tasks
                            Icon(
                                imageVector = Icons.Filled.Star,
                                contentDescription = "Priority Task",
                                tint = Color.Yellow,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    // Partial Description
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = task.description.take(50) + if (task.description.length > 50) "..." else "",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Justify
                    )

                    // Due Date and Actions
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Due Date: ${task.dueDate}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                        Row {
                            // Mark as Completed
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Mark as Completed",
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .clickable { onMarkCompleted(task) }
                            )

                            // Delete Task
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete Task",
                                modifier = Modifier.clickable { onDeleteTask(task) }
                            )
                        }
                    }
                }
            }
        }
    )
}