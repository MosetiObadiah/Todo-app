package com.moseti.todo.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.moseti.todo.LoginScreen
import com.moseti.todo.R
import com.moseti.todo.viewmodels.LoginScreenViewModel

@Composable
fun SettingsScreen(
    navController: NavController,
    innerPadding: PaddingValues,
    loginViewmodel: LoginScreenViewModel
) {
    var isDarkTheme by remember { mutableStateOf(false) }
    var isBiometricsEnabled by remember { mutableStateOf(false) }
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Profile Section
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_background),
            contentDescription = "Profile Picture",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .border(2.dp, Color.Black)
                .padding(7.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = loginViewmodel.userEmail.value, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = loginViewmodel.userPassword.value, fontSize = 16.sp)

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Dark Theme", fontSize = 16.sp)
            Switch(
                checked = isDarkTheme,
                onCheckedChange = { isDarkTheme = it }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Biometrics Toggle
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Enable Biometrics", fontSize = 16.sp)
            Switch(
                checked = isBiometricsEnabled,
                onCheckedChange = { isBiometricsEnabled = it }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Swipe left to delete", fontSize = 16.sp)
            Switch(
                checked = isBiometricsEnabled,
                onCheckedChange = { isBiometricsEnabled = it }
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Swipe right to Mark as completed", fontSize = 16.sp)
            Switch(
                checked = isBiometricsEnabled,
                onCheckedChange = { isBiometricsEnabled = it }
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Turn on Trash can", fontSize = 16.sp)
            Switch(
                checked = isBiometricsEnabled,
                onCheckedChange = { isBiometricsEnabled = it }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Sign Out Button
        Button(
            onClick = {
                //TODO end session
                navController.navigate(LoginScreen)
            },
            modifier = Modifier.fillMaxWidth(0.8f),
            shape = RoundedCornerShape(6.dp)
        ) {
            Text(text = "Log Out")
        }
    }
}