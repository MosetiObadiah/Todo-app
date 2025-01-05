package com.moseti.todo.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moseti.todo.R

var isLocked = false

@Composable
fun LockScreen(innerPadding: PaddingValues) {
    Column(
        Modifier.padding(innerPadding)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(
                if(isLocked) {
                    R.drawable.locked_svgrepo_com
                } else {
                    R.drawable.unlocked_svgrepo_com
                }
            ),
            contentDescription =
                if(isLocked) {
                    "Locked"
                } else {
                    "unlocked"
                },
            modifier = Modifier.size(40.dp, 45.dp)
        )

        OutlinedButton(
            onClick = {
                //TODO trigger fingerprint if app lock is turned on
            }
        ) {
            Text(
                if(isLocked) {
                "Locked"
                } else {
                "unlocked"
                }
            )
        }
    }
}

@Preview
@Composable
fun LockScreenPreview() {
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ){innerPadding ->
        LockScreen(innerPadding)
    }
}