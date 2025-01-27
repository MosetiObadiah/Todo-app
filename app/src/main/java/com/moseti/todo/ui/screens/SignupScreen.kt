package com.moseti.todo.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.moseti.todo.DisplayTasks
import com.moseti.todo.LoginScreen
import com.moseti.todo.viewmodels.LoginScreenViewModel

@Composable
fun SignUp(
    innerPadding: PaddingValues, navController: NavHostController, loginVmodel: LoginScreenViewModel
) {
    val context = LocalContext.current
    val focusRequester1 = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    Box(modifier = Modifier.fillMaxSize()) {
        //TODO put background
        ScreenContent(
            innerPadding, context, loginVmodel, navController, focusRequester1, keyboardController
        )
    }

}

@Composable
fun ScreenContent(
    innerPadding: PaddingValues,
    context: Context,
    loginVmodel: LoginScreenViewModel,
    navController: NavHostController,
    focusRequester1: FocusRequester,
    keyboardController: SoftwareKeyboardController?,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Create an account", fontSize = 22.sp, fontWeight = FontWeight.Bold
        )

        // Email Input
        OutlinedTextField(
            value = loginVmodel.userEmail.value,
            onValueChange = { newValue ->
                loginVmodel.userEmail.value = newValue
            },
            label = { Text("Email") },
            placeholder = { Text("johndoe@gmail.com") },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(onNext = {
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
                loginVmodel.signUp(onSuccess = {
                    Toast.makeText(
                        context, "Account created successfully!", Toast.LENGTH_SHORT
                    ).show()
                    navController.navigate(DisplayTasks)
                }, onError = { error ->
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                })
            })
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Create Account Button
        FilledTonalButton(
            onClick = {
                loginVmodel.signUp(onSuccess = {
                    Toast.makeText(context, "Account created successfully!", Toast.LENGTH_SHORT)
                        .show()
                    navController.navigate(DisplayTasks)
                }, onError = { error ->
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                })
            }, modifier = Modifier.fillMaxWidth(0.8f), shape = RoundedCornerShape(5.dp)
        ) {
            Text("Create Account")
        }

        // Navigate to Login Button
        TextButton(onClick = {
            navController.navigate(LoginScreen)
        }) {
            Text("Already have an account? Log in")
        }
    }
}