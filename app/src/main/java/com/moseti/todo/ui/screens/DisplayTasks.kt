package com.moseti.todo.ui.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.moseti.todo.OutlineCardCustom
import java.sql.Date
import java.sql.Time

data class tasks(
    val title : String,
    val description : String,
    val endDate : Date,
    val endTime : Time,
    val priority: Boolean
)

@Composable
fun ShowTasks(innerPadding: PaddingValues) {
    LazyColumn(
        Modifier.padding(innerPadding)
    ) {
        item {
            OutlineCardCustom()
        }
    }
}