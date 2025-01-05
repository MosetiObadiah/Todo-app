package com.moseti.todo

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.moseti.todo.ui.screens.AddTasks
import com.moseti.todo.ui.screens.LockScreen
import com.moseti.todo.ui.screens.SettingsScreen
import com.moseti.todo.ui.screens.ShowTasks
import com.moseti.todo.ui.screens.Tasks
import com.moseti.todo.ui.theme.ToDoTheme
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

data class NavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

private val tasksState = mutableStateOf(TaskRepository.tasks)
private val saveTask: (String, String, String, Long?, Boolean) -> Unit = { title, description, selectedOption, selectedDueDate, priorityTask ->
    TaskRepository.addTask(
        Tasks(
            title = title,
            description = description,
            taskPeriod = selectedOption,
            dueDate = selectedDueDate,
            priority = priorityTask
        )
    )
    // Notify Compose of data change
    tasksState.value = TaskRepository.tasks
}

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ToDoTheme {
                //navController
                val navController = rememberNavController()

                val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
                val items = listOf(
                    NavigationItem(
                        title = "Daily",
                        selectedIcon = Icons.Filled.Home,
                        unselectedIcon = Icons.Outlined.Home
                    ),
                    NavigationItem(
                        title = "Weekly",
                        selectedIcon = Icons.Filled.DateRange,
                        unselectedIcon = Icons.Outlined.DateRange
                    ),
                    NavigationItem(
                        title = "Monthly",
                        selectedIcon = Icons.Filled.DateRange,
                        unselectedIcon = Icons.Outlined.DateRange
                    ),
                    NavigationItem(
                        title = "Settings",
                        selectedIcon = Icons.Filled.Settings,
                        unselectedIcon = Icons.Outlined.Settings
                    )
                )
                Surface(
                    Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                    // This state variable will be used to control the visibility of the dialog
                    var showDialog by rememberSaveable { mutableStateOf(false) }

                    val scope = rememberCoroutineScope()
                    var selectedItemIndex by rememberSaveable {
                        mutableIntStateOf(0)
                    }
                    ModalNavigationDrawer(
                        drawerContent = {
                            //content of the drawer
                            ModalDrawerSheet {
                                Spacer(modifier = Modifier.height(16.dp))
                                items.forEachIndexed { index, item ->
                                    NavigationDrawerItem(
                                        label = {
                                            Text(text = item.title)
                                        },
                                        selected = index == selectedItemIndex,
                                        onClick = {
                                            println("item index $index")
                                            when(index){
                                                0,1,2 -> navController.navigate(DisplayTasks)
                                                3 -> navController.navigate(Settings)
                                            }
                                            selectedItemIndex = index
                                            scope.launch {
                                                drawerState.close()
                                            }
                                        },
                                        icon = {
                                            Icon(
                                                imageVector = if (index == selectedItemIndex) {
                                                    item.selectedIcon
                                                } else item.unselectedIcon,
                                                contentDescription = item.title
                                            )
                                        },
                                        modifier = Modifier
                                            .padding(NavigationDrawerItemDefaults.ItemPadding)
                                    )
                                }
                            }
                        },
                        drawerState = drawerState
                    ) {
                        val context = LocalContext.current
                        //content of actual screen
                        Scaffold(
                            modifier = Modifier
                                .fillMaxSize()
                                .nestedScroll(scrollBehavior.nestedScrollConnection),
                            topBar = {
                                CenterAlignedTopAppBar(
                                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                                        titleContentColor = MaterialTheme.colorScheme.primary,
                                    ),
                                    title = {
                                        Text(
                                            "ToDo",
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    },
                                    navigationIcon = {
                                        IconButton(onClick = {
                                            //pull side bar
                                            scope.launch {
                                                drawerState.open()
                                            }

                                        }) {
                                            Icon(
                                                imageVector = Icons.AutoMirrored.Filled.List,
                                                contentDescription = "Localized description"
                                            )
                                        }
                                    },
                                    actions = {
                                        IconButton(onClick = {
                                            //info about the dev
                                            Toast.makeText(context, "Made by Obadiah", Toast.LENGTH_SHORT).show()
                                        }) {
                                            Icon(
                                                imageVector = Icons.Filled.Info,
                                                contentDescription = "info about the developer"
                                            )
                                        }
                                    },
                                    scrollBehavior = scrollBehavior,
                                )
                            },
                            floatingActionButton = {

                                ExtendedFloatingActionButton(
                                    onClick = {
                                        showDialog = true
                                    },
                                    icon = { Icon(Icons.Filled.Edit, "Add new task") },
                                    text = { Text(text = "Add Task") },
                                )
                            }
                        ) { innerPadding ->
                            // Dialog content
                            if (showDialog) {
                                AlertDialog(
                                    onDismissRequest = { showDialog = false },
                                    title = {
                                        Text(
                                            "Add Task",
                                            modifier = Modifier.padding(5.dp),
                                            fontSize = 30.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                        )
                                    },
                                    text = {
                                        AddTasks(onSave = saveTask) }, // Pass your composable here
                                    confirmButton = {
                                        FilledTonalButton(
                                            onClick = {
                                                /*Todo save task entry*/
                                                showDialog = false
                                            },
                                            shape = RoundedCornerShape(5.dp)
                                        ) {
                                            Text("Save")
                                        }
                                    },
                                    modifier = Modifier
                                        .wrapContentWidth(
                                            align = Alignment.CenterHorizontally,
                                        )
                                        .wrapContentHeight(
                                            align = Alignment.CenterVertically,
                                        ),
                                    dismissButton = {
                                        FilledTonalButton(
                                            onClick = {
                                            /*TODO cancel task entry*/
                                                showDialog = false
                                            },
                                            shape = RoundedCornerShape(5.dp)
                                        ) {
                                            Text("Cancel")
                                        }
                                    }
                                )
                            }

                            NavHost(
                                navController = navController,
                                startDestination = DisplayTasks
                            ) {
                                composable<Lock> {
                                    LockScreen(innerPadding)
                                }
                                composable<AddTask> {
                                    AddTasks(onSave = saveTask)
                                }
                                composable<DisplayTasks>{
                                    ShowTasks(tasks = tasksState.value, innerPadding)
                                }
                                composable<Settings>{
                                    SettingsScreen(innerPadding)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

object TaskRepository {
    private val _tasks = mutableListOf<Tasks>()
    val tasks: List<Tasks>
        // Returns an immutable copy of the internal list
        get() = _tasks.toList()

    fun addTask(task: Tasks) {
        // Prevent duplicates
        if (_tasks.none { it.title == task.title }) {
            _tasks.add(task)
        }
    }

    fun removeTask(task: Tasks) {
        _tasks.remove(task)
    }

    fun clearTasks() {
        _tasks.clear()
    }
}

@Serializable
object Lock

@Serializable
object AddTask

@Serializable
object DisplayTasks

@Serializable
object Settings