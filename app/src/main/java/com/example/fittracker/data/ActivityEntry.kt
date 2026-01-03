package com.example.fittracker.data

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

data class ActivityEntry( //need default values for firebase, if not the app crashes
    @DocumentId val id: String = "", //to get unique id for each activity from firebase
    val activityName: String = "",
    val emoji: String = "",
    val duration: Int = 0,
    val date: Timestamp = Timestamp.now(),
    val userId: String = ""
)
