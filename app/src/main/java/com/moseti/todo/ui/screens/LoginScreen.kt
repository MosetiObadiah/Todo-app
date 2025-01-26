package com.moseti.todo.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.moseti.todo.DisplayTasks
import com.moseti.todo.SignupScreen
import com.moseti.todo.viewmodels.LoginScreenViewModel

@Composable
fun Login(
    innerPadding: PaddingValues,
    navController: NavHostController,
    loginVmodel: LoginScreenViewModel
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Login",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        // Email Input
        OutlinedTextField(
            value = loginVmodel.userEmail.value,
            onValueChange = { loginVmodel.userEmail.value = it },
            label = { Text("Email") }
        )

        // Password Input
        OutlinedTextField(
            value = loginVmodel.userPassword.value,
            onValueChange = { loginVmodel.userPassword.value = it },
            label = { Text("Password") }
        )

        // Login Button
        FilledTonalButton(
            onClick = {
                loginVmodel.login(
                    onSuccess = {
                        // Navigate to tasks screen on success
                        navController.navigate(DisplayTasks)
                    },
                    onError = { errorMessage ->
                        // Show toast with error message
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                )
            }
        ) {
            Text("Login")
        }

        TextButton(
            onClick = {
                navController.navigate(SignupScreen)
            }
        ) {
            Text("Don't have an account? Sign up")
        }
    }
}