package com.example.fittracker.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fittracker.data.ActivityEntry
import com.example.fittracker.ui.components.SelectableSurface


enum class TimeUnit { Days, Weeks, Months, Years}
enum class Metric { Count, Duration}
@Composable
fun StatisticsScreen(
    onNavigate: (String) -> Unit,
    allActivities: List<ActivityEntry>){

    var selectedTimeUnit by remember { mutableStateOf(TimeUnit.Days) }
    var selectedMetric by remember { mutableStateOf(Metric.Count) }
    var selectedSport by remember { mutableStateOf("All") }

    val sports = remember(allActivities) {
        listOf("All") + (allActivities.map { it.activityName }).distinct() //list of all sport + possibility to choose all at once
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
            .verticalScroll(rememberScrollState())
    ){
        Text(text = "Statistics",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Text(text = "Analyze your fitness journey",
            fontSize = 16.sp
        )

        Spacer(Modifier.height(15.dp))

        Text(text = "Time Period",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )

        FlowRow( //automatically arranges elements horizontally, if there is no space next element is in the next row
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 5.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ){
            TimeUnit.entries.forEach { type ->
                SelectableSurface(
                    label = type.name,
                    isSelected = selectedTimeUnit == type,
                    onClick = { selectedTimeUnit = type },
                    modifier = Modifier.fillMaxWidth(0.22f) //so 4 are in 1 row
                )
            }
        }

        val periodDescription = when (selectedTimeUnit) {
            TimeUnit.Days -> "Last 7 days"
            TimeUnit.Weeks -> "Last 8 Weeks"
            TimeUnit.Months -> "Last 12 months"
            TimeUnit.Years -> "All time data"
        }

        Text(
            text = periodDescription,
            fontSize = 10.sp
        )

        Spacer(Modifier.height(15.dp))

        Text(text = "Metric",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )

        FlowRow( //automatically arranges elements horizontally, if there is no space next element is in the next row
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ){
            Metric.entries.forEach { type ->
                SelectableSurface(
                    label = type.name,
                    isSelected = selectedMetric == type,
                    onClick = { selectedMetric = type },
                    modifier = Modifier.fillMaxWidth(0.45f) //so 2 are in 1 row
                )
            }
        }

        Spacer(Modifier.height(15.dp))

        Text(text = "Activity Type",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )

        FlowRow( //automatically arranges elements horizontally, if there is no space next element is in the next row
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ){
            sports.forEach { type ->
                SelectableSurface(
                    label = type,
                    isSelected = selectedSport == type,
                    onClick = { selectedSport = type },
                    modifier = Modifier.fillMaxWidth(0.3f) //so 3 activities are in 1 row
                )
            }
        }

        Spacer(Modifier.height(15.dp))

        Text(
            text = "Chart"
        )

    }

}