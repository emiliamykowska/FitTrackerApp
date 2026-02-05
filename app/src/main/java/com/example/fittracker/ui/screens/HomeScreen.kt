package com.example.fittracker.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
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
    val recentActivities = allActivities.take(4) //only most 4 recent activities

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onNavigate("calendar")},
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(Color.LightGray.copy(alpha = 0.2f))
        ){
            Row(
                modifier = Modifier.padding(20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ){
                Text(text = "📅", fontSize = 34.sp)
                Spacer(modifier = Modifier.width(15.dp))
                Column {
                    Text(
                        text = "Activity Calendar",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    )
                    Text(
                        text = "View your progress by date",
                        fontSize = 14.sp
                    )
                }
            }
        }
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
                text = "To see all your activities go to the history panel",
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp,
                textAlign = TextAlign.Left,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}