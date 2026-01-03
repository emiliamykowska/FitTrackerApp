package com.example.fittracker.data

import com.google.firebase.Timestamp

data class ActivityEntry(
    val activityName: String,
    val emoji: String,
    val duration: Int,
    val date: Timestamp,
    val userId: String
)
