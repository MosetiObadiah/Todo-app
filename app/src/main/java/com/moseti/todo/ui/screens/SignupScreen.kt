package com.moseti.todo.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.moseti.todo.DisplayTasks
import com.moseti.todo.LoginScreen
import com.moseti.todo.viewmodels.LoginScreenViewModel

@Composable
fun SignUp(
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
            text = "Create an account",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        // Email Input
        OutlinedTextField(
            value = loginVmodel.userEmail.value,
            onValueChange = { newValue ->
                loginVmodel.userEmail.value = newValue
            },
            label = { Text("Email") }
        )

        // Password Input
        OutlinedTextField(
            value = loginVmodel.userPassword.value,
            onValueChange = { loginVmodel.userPassword.value = it },
            label = { Text("Password") }
        )

        Spacer(modifier = Modifier.height(5.dp))

        // Create Account Button
        FilledTonalButton(
            onClick = {
                loginVmodel.signUp(
                    onSuccess = {
                        Toast.makeText(context, "Account created successfully!", Toast.LENGTH_SHORT).show()
                        navController.navigate(DisplayTasks)
                    },
                    onError = { error ->
                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                    }
                )
            },
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Text("Create Account")
        }

        // Navigate to Login Button
        TextButton(
            onClick = {
                navController.navigate(LoginScreen)
            }
        ) {
            Text("Already have an account? Log in")
        }
    }
}