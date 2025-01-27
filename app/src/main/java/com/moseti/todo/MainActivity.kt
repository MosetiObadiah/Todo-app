package com.moseti.todo

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.moseti.todo.db.AppDatabase
import com.moseti.todo.db.TaskDao
import com.moseti.todo.db.UserDao
import com.moseti.todo.ui.screens.AddTasks
import com.moseti.todo.ui.screens.LockScreen
import com.moseti.todo.ui.screens.Login
import com.moseti.todo.ui.screens.SettingsScreen
import com.moseti.todo.ui.screens.ShowTasks
import com.moseti.todo.ui.screens.SignUp
import com.moseti.todo.ui.theme.ToDoTheme
import com.moseti.todo.viewmodels.AddTasksViewModel
import com.moseti.todo.viewmodels.AddTasksViewModelFactory
import com.moseti.todo.viewmodels.LoginScreenViewModel
import com.moseti.todo.viewmodels.LoginScreenViewModelFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

data class NavigationItem(
    val title: String, val selectedIcon: ImageVector, val unselectedIcon: ImageVector
)

class MainActivity : ComponentActivity() {
    private lateinit var userDao: UserDao
    private lateinit var taskDao: TaskDao

    @RequiresApi(Build.VERSION_CODES.S)
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {

        val splashscreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        var keepSplashScreen = true
        splashscreen.setKeepOnScreenCondition { keepSplashScreen }
        lifecycleScope.launch {
            delay(1500)
            //TODO, load stuff here
            //preloadData()
            //checkUserSession()
            keepSplashScreen = false
        }

        enableEdgeToEdge()

        val database = AppDatabase.getInstance(applicationContext)
        userDao = database.userDao()
        taskDao = database.taskDao()

        setContent {
            ToDoTheme {
                // Create the ViewModelFactory
                val addTaskFactory = AddTasksViewModelFactory(taskDao)
                val loginScreenFactory = LoginScreenViewModelFactory(userDao, taskDao)
                //navController
                val navController = rememberNavController()
                val scrollBehavior =
                    TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
                val items = listOf(
                    NavigationItem(
                        title = "Daily",
                        selectedIcon = Icons.Filled.Home,
                        unselectedIcon = Icons.Outlined.Home
                    ), NavigationItem(
                        title = "Weekly",
                        selectedIcon = Icons.Filled.DateRange,
                        unselectedIcon = Icons.Outlined.DateRange
                    ), NavigationItem(
                        title = "Monthly",
                        selectedIcon = Icons.Filled.DateRange,
                        unselectedIcon = Icons.Outlined.DateRange
                    ), NavigationItem(
                        title = "Settings",
                        selectedIcon = Icons.Filled.Settings,
                        unselectedIcon = Icons.Outlined.Settings
                    )
                )
                Surface(
                    Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                    // This state variable will be used to control the visibility of the dialog
                    var showDialog by rememberSaveable { mutableStateOf(false) }

                    val scope = rememberCoroutineScope()
                    var selectedItemIndex by rememberSaveable {
                        mutableIntStateOf(0)
                    }
                    //TODO display sidebar, TopBar and FAB on valid route only
                    if (observeCurrentRoute(navController)) {
                        ModalNavigationDrawer(
                            drawerContent = {
                                ModalDrawerSheet {
                                    Log.d("Modal Drawer sheet accessed", "True")
                                    Spacer(modifier = Modifier.height(16.dp))
                                    items.forEachIndexed { index, item ->
                                        NavigationDrawerItem(label = {
                                            Text(text = item.title)
                                        },
                                            selected = index == selectedItemIndex,
                                            onClick = {
                                                when (index) {
                                                    0, 1, 2 -> navController.navigate(DisplayTasks)
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
                                            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                                        )
                                    }
                                }
                            }, drawerState = drawerState
                        ) {
                            val context = LocalContext.current
                            //content of actual screen
                            Scaffold(modifier = Modifier
                                .fillMaxSize()
                                .nestedScroll(scrollBehavior.nestedScrollConnection), topBar = {
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
                                            Toast.makeText(
                                                context, "Made by Obadiah", Toast.LENGTH_SHORT
                                            ).show()
                                        }) {
                                            Icon(
                                                imageVector = Icons.Filled.Info,
                                                contentDescription = "info about the developer"
                                            )
                                        }
                                    },
                                    scrollBehavior = scrollBehavior,
                                )
                            }, floatingActionButton = {
                                ExtendedFloatingActionButton(onClick = {
                                    showDialog = true
                                },
                                    icon = { Icon(Icons.Filled.Edit, "Add new task") },
                                    text = { Text(text = "Add Task") })
                            }) { innerPadding ->
                                //initialize the viewmodel
                                val addTasksViewModel: AddTasksViewModel =
                                    viewModel(factory = addTaskFactory)
                                val loginViewmodel: LoginScreenViewModel =
                                    viewModel(factory = loginScreenFactory)

                                // Dialog content
                                if (showDialog) {
                                    AlertDialog(onDismissRequest = { showDialog = false }, title = {
                                        Text(
                                            "Add Task",
                                            modifier = Modifier.padding(5.dp),
                                            fontSize = 30.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                        )
                                    }, text = {
                                        AddTasks(addTasksViewModel)
                                    }, confirmButton = {
                                        FilledTonalButton(
                                            onClick = {
                                                /*Todo save task entry*/
                                                addTasksViewModel.addTask(
                                                    loginViewmodel.userEmail.value, taskDao
                                                )
                                                showDialog = false
                                            }, shape = RoundedCornerShape(5.dp)
                                        ) {
                                            Text("Save")
                                        }
                                    }, modifier = Modifier
                                        .wrapContentWidth(
                                            align = Alignment.CenterHorizontally,
                                        )
                                        .wrapContentHeight(
                                            align = Alignment.CenterVertically,
                                        ), dismissButton = {
                                        FilledTonalButton(
                                            onClick = {
                                                addTasksViewModel.clearEntries()
                                                navController.navigate(DisplayTasks)
                                                showDialog = false
                                            }, shape = RoundedCornerShape(5.dp)
                                        ) {
                                            Text("Cancel")
                                        }
                                    })
                                }
                                NavHost(
                                    navController = navController, startDestination = LoginScreen
                                ) {
                                    composable<Lock> {
                                        LockScreen(innerPadding)
                                    }
                                    composable<LoginScreen> {
                                        Login(innerPadding, navController, loginViewmodel)
                                    }
                                    composable<SignupScreen> {
                                        SignUp(innerPadding, navController, loginViewmodel)
                                    }
                                    composable<AddTask> {
                                        AddTasks(addTasksViewModel)
                                    }
                                    composable<DisplayTasks> {
                                        ShowTasks(
                                            addTasksViewModel, innerPadding
                                        ) {}
                                        LaunchedEffect(Unit) {
                                            addTasksViewModel.loadTasks(loginViewmodel.userEmail.value)
                                        }
                                    }
                                    composable<Settings> {
                                        SettingsScreen(innerPadding)
                                    }
                                }
                            }
                        }
                    } else {
                        val context = LocalContext.current
                        Scaffold(modifier = Modifier
                            .fillMaxSize()
                            .nestedScroll(scrollBehavior.nestedScrollConnection), topBar = {
                            if (observeCurrentRoute(navController)) {
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
                                            Toast.makeText(
                                                context, "Made by Obadiah", Toast.LENGTH_SHORT
                                            ).show()
                                        }) {
                                            Icon(
                                                imageVector = Icons.Filled.Info,
                                                contentDescription = "info about the developer"
                                            )
                                        }
                                    },
                                    scrollBehavior = scrollBehavior,
                                )
                            }
                        }, floatingActionButton = {
                            if (observeCurrentRoute(navController)) {
                                Toast.makeText(
                                    this, "valid route reached", Toast.LENGTH_SHORT
                                ).show()
                                ExtendedFloatingActionButton(onClick = {
                                    showDialog = true
                                },
                                    icon = { Icon(Icons.Filled.Edit, "Add new task") },
                                    text = { Text(text = "Add Task") })
                            }
                        }) { innerPadding ->
                            //initialize the viewmodel
                            val addTasksViewModel: AddTasksViewModel =
                                viewModel(factory = addTaskFactory)
                            val loginViewmodel: LoginScreenViewModel =
                                viewModel(factory = loginScreenFactory)

                            // Dialog content
                            if (showDialog) {
                                AlertDialog(onDismissRequest = { showDialog = false }, title = {
                                    Text(
                                        "Add Task",
                                        modifier = Modifier.padding(5.dp),
                                        fontSize = 30.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                    )
                                }, text = {
                                    AddTasks(addTasksViewModel)
                                }, confirmButton = {
                                    FilledTonalButton(
                                        onClick = {
                                            /*Todo save task entry*/
                                            addTasksViewModel.addTask(
                                                loginViewmodel.userEmail.value, taskDao
                                            )
                                            showDialog = false
                                        }, shape = RoundedCornerShape(5.dp)
                                    ) {
                                        Text("Save")
                                    }
                                }, modifier = Modifier
                                    .wrapContentWidth(
                                        align = Alignment.CenterHorizontally,
                                    )
                                    .wrapContentHeight(
                                        align = Alignment.CenterVertically,
                                    ), dismissButton = {
                                    FilledTonalButton(
                                        onClick = {
                                            addTasksViewModel.clearEntries()
                                            navController.navigate(DisplayTasks)
                                            showDialog = false
                                        }, shape = RoundedCornerShape(5.dp)
                                    ) {
                                        Text("Cancel")
                                    }
                                })
                            }
                            NavHost(
                                navController = navController, startDestination = LoginScreen
                            ) {
                                composable<Lock> {
                                    LockScreen(innerPadding)
                                }
                                composable<LoginScreen> {
                                    Login(innerPadding, navController, loginViewmodel)
                                }
                                composable<SignupScreen> {
                                    SignUp(innerPadding, navController, loginViewmodel)
                                }
                                composable<AddTask> {
                                    AddTasks(addTasksViewModel)
                                }
                                composable<DisplayTasks> {
                                    ShowTasks(addTasksViewModel, innerPadding) {}
                                    LaunchedEffect(Unit) {
                                        addTasksViewModel.loadTasks(loginViewmodel.userEmail.value)
                                    }
                                }
                                composable<Settings> {
                                    SettingsScreen(innerPadding)
                                }
                            }
                        }

                    }
                }
            }
        }
    }

    @Composable
    fun observeCurrentRoute(navController: NavController): Boolean {
        val currentRoute = remember { mutableStateOf<String?>(null) }
        LaunchedEffect(navController) {
            navController.currentBackStackEntryFlow.map { it.destination.route }.collect { route ->
                Log.d("CurrentRoute", "Route is: $route")
                currentRoute.value = route
            }
        }
        return currentRoute.value == "com.moseti.todo.DisplayTasks"
    }
}

@Serializable
object SignupScreen

@Serializable
object LoginScreen

@Serializable
object Lock

@Serializable
object AddTask

@Serializable
object DisplayTasks

@Serializable
object Settings