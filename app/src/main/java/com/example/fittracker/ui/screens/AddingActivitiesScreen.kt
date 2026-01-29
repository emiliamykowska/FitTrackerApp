package com.example.fittracker.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fittracker.data.ActivityEntry
import com.example.fittracker.data.activitiesList
import com.example.fittracker.data.durationList
import com.example.fittracker.ui.components.AddingActivityCard
import com.example.fittracker.ui.components.InputTextField
import com.example.fittracker.ui.components.RoundedButton
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.edit

@Composable
fun AddingActivitiesScreen(onNavigate: (String) -> Unit){
    var selectedActivity by remember { mutableStateOf<String?>(null) }
    var selectedDuration by remember { mutableStateOf<Int?>(null) }
    var customDuration by remember { mutableStateOf("")} // "" coz can't be null for inputTextField

    val db = FirebaseFirestore.getInstance() //connecting with firebase database
    val auth = Firebase.auth
    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(15.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ){

        item {
            Text(
            text = "Choose Activity Type:",
            fontSize = 16.sp, fontWeight = FontWeight.SemiBold
        )}


        items(activitiesList.chunked(2)){ //divides elements into pairs and puts these pairs alternately inside 4 lists (coz 8 elements)
            rowActivities ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            rowActivities.forEach {activity ->
                AddingActivityCard(
                    value = activity.emoji,
                    label = activity.name,
                    isSelected = selectedActivity == activity.name,
                    onClick = { selectedActivity = activity.name },
                    modifier = Modifier.weight(1f) //so each card is the same size
                )
            }
        }}

        item{
            Text(
            text = "Choose Activity Duration:",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )}

        items(durationList.chunked(3)){
            rowDurations ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            rowDurations.forEach {time ->
                AddingActivityCard(
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

        item {
            InputTextField(
            value = customDuration,
            onValueChange = { input ->
                if (input.all { it.isDigit() } && input.length <= 3) {
                    customDuration = input
                    selectedDuration = null
                }},
            label = "Custom duration (1 - 999 minutes)",
            keyboardType = KeyboardType.Number
        )}

        item{
            val isCustomDurationValid = customDuration.isNotEmpty() && (customDuration.toIntOrNull() ?: 0) > 0
            RoundedButton(
                text = "Add activity",
                enabled = selectedActivity != null && (selectedDuration != null || isCustomDurationValid),
                onClick = {
                    val currentUser = auth.currentUser

                    if (currentUser != null){
                        val duration: Int = selectedDuration ?: (customDuration.toIntOrNull() ?: 0)
                        val selectedEmoji = activitiesList.find {it.name == selectedActivity}?.emoji ?: "❓"
                        val activityName: String = selectedActivity!! // !! means it for sure is not null, so changes String? to String here

                        val activityEntry = ActivityEntry(
                            activityName = activityName,
                            emoji = selectedEmoji,
                            duration = duration,
                            date = Timestamp.now(),
                            userId = currentUser.uid
                        )

                        db.collection("activities")
                            .add(activityEntry)
                            .addOnSuccessListener {
                                val preferences = context.getSharedPreferences("FitTrackerPreferences", Context.MODE_PRIVATE)
                                //shared preferences is xml file inside phone memory, the function searches for file FitTrackerPreferenes or creates it if doesnt exist
                                //mode private so other apps cannot see whats inside this file
                                preferences.edit {
                                    putLong("last_workout_timestamp", System.currentTimeMillis())
                                } //write it to this xml file

                                Toast.makeText(context, "Activity was successfully added!", Toast.LENGTH_SHORT).show()
                                onNavigate("home")
                            }
                            .addOnFailureListener { exception ->
                                val errorText = exception.localizedMessage ?: "Unknown error occurred"
                                Toast.makeText(context, errorText, Toast.LENGTH_LONG).show()
                            }
                    }
                }
            )
        }
    }
}

