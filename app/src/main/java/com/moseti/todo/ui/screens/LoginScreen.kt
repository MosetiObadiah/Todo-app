package com.moseti.todo.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.moseti.todo.DisplayTasks
import com.moseti.todo.SignupScreen
import com.moseti.todo.viewmodels.LoginScreenViewModel

@Composable
fun Login(
    innerPadding: PaddingValues, navController: NavHostController, loginVmodel: LoginScreenViewModel
) {
    val context = LocalContext.current
    val focusRequester1 = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Login", fontSize = 22.sp, fontWeight = FontWeight.Bold
        )

        // Email Input
        OutlinedTextField(
            value = loginVmodel.userEmail.value,
            onValueChange = { loginVmodel.userEmail.value = it },
            label = { Text("Email") },
            placeholder = { Text("johndoe@gmail.com") },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(onNext = {
                // Move to the next TextField
                if (loginVmodel.userEmail.value.isNotEmpty()) {
                    focusRequester1.requestFocus()
                } else {
                    Toast.makeText(context, "Email cannot be empty", Toast.LENGTH_SHORT).show()
                }
            })
        )

        // Password Input
        OutlinedTextField(
            value = loginVmodel.userPassword.value,
            onValueChange = { loginVmodel.userPassword.value = it },
            label = { Text("Password") },
            modifier = Modifier.focusRequester(focusRequester1),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = {
                keyboardController?.hide()
                //TODO remove this repetition
                loginVmodel.login(onSuccess = {
                    // Navigate to tasks screen on success
                    navController.navigate(DisplayTasks)
                }, onError = { errorMessage ->
                    // Show toast with error message
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                })
            })
        )

        Spacer(Modifier.height(10.dp))

        // Login Button
        FilledTonalButton(
            onClick = {
                loginVmodel.login(onSuccess = {
                    // Navigate to tasks screen on success
                    navController.navigate(DisplayTasks)
                }, onError = { errorMessage ->
                    // Show toast with error message
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                })
            }, modifier = Modifier.fillMaxWidth(0.8f), shape = RoundedCornerShape(5.dp)
        ) {
            Text("Login")
        }

        TextButton(onClick = {
            navController.navigate(SignupScreen)
        }) {
            Text("Don't have an account? Sign up")
        }
    }
}