package com.example.fittracker.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
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
import com.example.fittracker.data.ActivityEntry
import com.example.fittracker.ui.components.RecentActivityCard

@Composable
fun HomeScreen(
    onNavigate: (String) -> Unit,
    allActivities: List<ActivityEntry>
) {
    val scrollState = rememberScrollState()

    val recentActivities = allActivities.take(4) //only most 4 recent activities

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Text(
            text = "Your Recent Activities:",
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
            textAlign = TextAlign.Left,
            modifier = Modifier.fillMaxWidth()
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
                textAlign = TextAlign.Justify,
                fontSize = 15.sp
            )
        }
        else if (allActivities.size > 4){
            Text(
                text = "To see all your activities go to history panel",
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp,
                textAlign = TextAlign.Left,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}