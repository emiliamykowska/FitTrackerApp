package com.example.fittracker.data

//data class so there is equals method and prettier print
data class ActivityType (
    val name: String,
    val emoji: String
    )

val activitiesList = listOf(
    ActivityType("Running", "🏃"),
    ActivityType("Walking", "🚶"),
    ActivityType("Cycling", "🚴"),
    ActivityType("Swimming", "🏊"),
    ActivityType("Yoga", "🧘"),
    ActivityType("Gym", "💪"),
    ActivityType("Dance", "💃"),
    ActivityType("Other", "⚽")
)

val durationList = listOf(15, 30, 45, 60, 90, 120)