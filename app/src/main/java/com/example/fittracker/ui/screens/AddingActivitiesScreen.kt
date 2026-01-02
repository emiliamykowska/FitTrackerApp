package com.example.fittracker.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fittracker.data.activitiesList
import com.example.fittracker.data.durationList
import com.example.fittracker.ui.components.ActivityCard
import com.example.fittracker.ui.components.InputTextField
import com.example.fittracker.ui.components.RoundedButton

@Composable
fun AddingActivitiesScreen(onNavigate: (String) -> Unit){
    var selectedActivity by remember { mutableStateOf<String?>(null) }
    var selectedDuration by remember { mutableStateOf<Int?>(null) }
    var customDuration by remember { mutableStateOf("")} // "" coz can't be null for inputTextField

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(15.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ){
        item {
            Text(
            text = "Add Activity:",
            fontSize = 18.sp
        )}

        item { Spacer(modifier = Modifier.height(10.dp)) }

        item {
            Text(
            text = "Activity Type",
            fontSize = 16.sp
        )}

        item{ Spacer(modifier = Modifier.height(10.dp)) }

        items(activitiesList.chunked(2)){ //divides elements into pairs and puts these pairs alternately inside 4 lists (coz 8 elements)
            rowActivities ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
        ) {
            rowActivities.forEach {activity ->
                ActivityCard(
                    value = activity.emoji,
                    label = activity.name,
                    isSelected = selectedActivity == activity.name,
                    onClick = { selectedActivity = activity.name },
                    modifier = Modifier.weight(1f) //so each card is the same size
                )
            }
        }}

        item{Spacer(modifier = Modifier.height(10.dp))}

        items(durationList.chunked(3)){
            rowDurations ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
        ) {
            rowDurations.forEach {time ->
                ActivityCard(
                    value = "$time",
                    label = "min",
                    isSelected = selectedDuration == time,
                    onClick = {
                        selectedDuration = time
                        customDuration = ""
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }}

        item{Text(
            text = "Duration (minutes)",
            fontSize = 16.sp
        )}

        item{Spacer(modifier = Modifier.height(10.dp))}

        item { InputTextField(
            value = customDuration,
            onValueChange = {if (it.all {char -> char.isDigit()}) {
                    customDuration = it
                    selectedDuration = null
            }},
            label = "Custom duration",
            keyboardType = KeyboardType.Number
        )}

        item{
            RoundedButton(
                text = "Add activity",
                onClick = {},
                enabled = selectedActivity != null && (selectedDuration != null || customDuration.isNotEmpty()))
        }
    }
}

