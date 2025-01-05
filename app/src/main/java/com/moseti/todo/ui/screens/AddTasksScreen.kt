package com.moseti.todo.ui.screens

import android.text.format.DateFormat
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moseti.todo.DateRangePickerModal

@Composable
fun AddTasks(innerPaddingValues: PaddingValues) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    // State to control the visibility of the DateRangePickerModal
    var showDateRangePicker by remember { mutableStateOf(false) }
    // State to hold the selected date range
    var selectedDateRange by remember { mutableStateOf<Pair<Long?, Long?>>(null to null) }

    var priorityTask by remember { mutableStateOf(true) }

    Column(
        Modifier.fillMaxSize()
            .padding(innerPaddingValues)
    ) {
        Text(
            "Add Task",
            modifier = Modifier.padding(5.dp),
            fontSize = 30.sp,
            fontWeight = FontWeight.ExtraBold,
        )

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            modifier = Modifier.padding(5.dp, 5.dp, 5.dp, 0.dp)
                .fillMaxWidth(),
            label = { Text("Title") }
        )

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            modifier = Modifier.padding(5.dp, 0.dp, 5.dp, 0.dp)
                .fillMaxWidth(),
            label = { Text("Description") }
        )

        Button(
            onClick = { showDateRangePicker = true },
            modifier = Modifier.padding(5.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(4.dp)
        ) {
            Text("Select Date Range")
        }

        // State to hold formatted date strings
        var startingDate by remember { mutableStateOf("Select starting date") }
        var endingDate by remember { mutableStateOf("Select end date") }

        // Extract start and end dates
        selectedDateRange.let { (start, end) ->
            if (start != null) {
                startingDate = DateFormat.format("MM/dd/yyyy", start).toString()
            }
            if (end != null) {
                endingDate = DateFormat.format("MM/dd/yyyy", end).toString()
            }
        }

        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = startingDate,
                onValueChange = { /*Read only*/ },
                modifier = Modifier
                    .padding(5.dp)
                    .weight(1f)
                    .clickable {
                        //TODO on click launch date picker
                    },
                readOnly = true,
                label = { Text("Start") }
            )

            Text(
                "to",
                modifier = Modifier.padding(5.dp),
                fontSize = 15.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center
            )

            OutlinedTextField(
                value = endingDate,
                onValueChange = {/*Read only*/ },
                modifier = Modifier
                    .padding(5.dp)
                    .weight(1f)
                    .clickable {
                        //TODO on click launch date picker
                    },
                readOnly = true,
                label = { Text("End") }
            )
        }

        // Show the DateRangePickerModal when showDateRangePicker is true
        if (showDateRangePicker) {
            DateRangePickerModal(
                onDateRangeSelected = { dateRange ->
                    selectedDateRange = dateRange
                    showDateRangePicker = false // Dismiss modal after selection
                },
                onDismiss = {
                    showDateRangePicker = false // Dismiss modal on cancel
                }
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(5.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "High Priority",
                modifier = Modifier.padding(5.dp),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
            )

            Switch(
                checked = priorityTask,
                onCheckedChange = {
                    priorityTask = it
                },
                thumbContent = if (priorityTask) {
                    {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = null,
                            modifier = Modifier.size(SwitchDefaults.IconSize),
                        )
                    }
                } else {
                    null
                }
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(5.dp),
            horizontalArrangement = Arrangement.SpaceAround ,
            verticalAlignment = Alignment.CenterVertically
        ) {
            FilledTonalButton(
                onClick = { /*TODO cancel task entry*/ },
                shape = RoundedCornerShape(5.dp)
            ) {
                Text("Cancel")
            }

            FilledTonalButton(
                onClick = { /*Todo save task entry*/ },
                shape = RoundedCornerShape(5.dp)
            ) {
                Text("Save")
            }
        }
    }
}