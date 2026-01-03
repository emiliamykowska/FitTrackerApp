package com.example.fittracker.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fittracker.data.ActivityEntry
import com.example.fittracker.ui.components.RecentActivityCard
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

@Composable
fun HomeScreen(onNavigate: (String) -> Unit) {
    val db = FirebaseFirestore.getInstance()
    val auth = Firebase.auth
    val currentUser = auth.currentUser

    var recentActivities by remember { mutableStateOf<List<ActivityEntry>>(emptyList()) }

    LaunchedEffect(currentUser) {
        if (currentUser != null) {
            db.collection("activities")
                .whereEqualTo("userId", currentUser.uid) //take activities with field userId = currentUSer.uid
                .orderBy("date", Query.Direction.DESCENDING)
                .limit(5)
                .addSnapshotListener { snapshot, error -> // listen for changes in firebase
                    if (error != null) {
                        return@addSnapshotListener
                    }

                    if (snapshot != null){ //if is not null that means that collection("activities") was changed f.e. new activity added
                        recentActivities = snapshot.toObjects(ActivityEntry::class.java)} //to objects change json from firestore into ActivityEntry
                    //Use ActivityEntry class but since firebase is in java, it's kotlin structure has to be changes into class.java
                }
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Text(
            text = "Recent Activities",
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp
        )

        recentActivities.forEach { activity ->
            RecentActivityCard(
                activityName = activity.activityName,
                emoji = activity.emoji,
                duration = activity.duration,
                timestamp = activity.date
            )
        }

        if (recentActivities.isEmpty()) {
            Text(
                text = "You haven't add any activities yet, add your first one after a workout!",
                fontSize = 15.sp
            )
        }
    }
}