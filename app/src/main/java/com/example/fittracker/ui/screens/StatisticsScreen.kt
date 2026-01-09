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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fittracker.data.ActivityEntry
import com.example.fittracker.data.DataPoint
import com.example.fittracker.ui.components.SelectableSurface
import com.example.fittracker.ui.components.StatisticsChart
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


enum class TimeUnit { Days, Weeks, Months, Years}
enum class Metric { Count, Duration}
@Composable
fun StatisticsScreen(
    onNavigate: (String) -> Unit,
    allActivities: List<ActivityEntry>){
    if (allActivities.isEmpty()){
        Text(
            text = "You haven't add any activities yet, add your first one after a workout to see your stats!",
            textAlign = TextAlign.Justify,
            fontSize = 15.sp,
            modifier = Modifier
                .padding(10.dp)
        )
    }
    else {
        var selectedTimeUnit by remember { mutableStateOf(TimeUnit.Days) }
        var selectedMetric by remember { mutableStateOf(Metric.Count) }
        var selectedSport by remember { mutableStateOf("All") }

        val sports = remember(allActivities) {
            listOf("All") + (allActivities.map { it.activityName }).distinct() //list of all sport + possibility to choose all at once
        }

        val filteredBySport = remember(allActivities, selectedSport) {
            if (selectedSport == "All") {
                allActivities
            } else {
                allActivities.filter { it.activityName == selectedSport }
            }
        }

        val firstEntry = remember (filteredBySport) {
            filteredBySport.minByOrNull { it.date.toDate().time }
        } //cant really be null coz the sport must exist to be chosen

        val finalFilteredActivities = remember(filteredBySport, selectedTimeUnit) {
            val calendar = getStartCalendar(selectedTimeUnit, firstEntry)

            val startTime = calendar.timeInMillis

            filteredBySport.filter { it.date.toDate().time >= startTime }
        }

        val chartPoints = remember(finalFilteredActivities, selectedTimeUnit, selectedMetric){
            val result = mutableListOf<DataPoint>()
            val calendar = getStartCalendar(selectedTimeUnit, firstEntry)

            val numberOfPoints = when(selectedTimeUnit){ //number of points on the chart
                TimeUnit.Days -> 14
                TimeUnit.Weeks -> 8
                TimeUnit.Months -> 12
                TimeUnit.Years -> {
                    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
                    val startYear = calendar.get(Calendar.YEAR)

                    (currentYear - startYear + 1).coerceAtLeast(1) //min value is 1, so at least 1 year has to be shown
                }
            }

            val sdf = SimpleDateFormat(
                when(selectedTimeUnit){
                    TimeUnit.Days -> "dd/MM"
                    TimeUnit.Weeks -> "dd/MM" //date of first day of the week
                    TimeUnit.Months -> "MM"
                    TimeUnit.Years -> "yyyy"
                }, Locale.getDefault()
            )

            repeat(numberOfPoints) {
                val startTime = calendar.timeInMillis
                val label = sdf.format(calendar.time)

                val endNextStepCalendar = calendar.clone() as Calendar //clone method gives any so it has to be changed to Calendar
                when(selectedTimeUnit) {
                    TimeUnit.Days -> endNextStepCalendar.add(Calendar.DAY_OF_YEAR, 1)
                    TimeUnit.Weeks -> endNextStepCalendar.add(Calendar.WEEK_OF_YEAR, 1)
                    TimeUnit.Months -> endNextStepCalendar.add(Calendar.MONTH, 1)
                    TimeUnit.Years -> endNextStepCalendar.add(Calendar.YEAR, 1)
                }
                val endTime = endNextStepCalendar.timeInMillis

                val activitiesInPoint = finalFilteredActivities.filter{
                    val activityTime = it.date.toDate().time

                    activityTime >= startTime && activityTime < endTime //smaller coz if activity is on 00:00:00 then we dont want it
                }

                val value =
                    if (selectedMetric == Metric.Duration){
                        activitiesInPoint.sumOf { it.duration }.toFloat() / 60f
                    }
                    else{
                    activitiesInPoint.size.toFloat()
                 }


                result.add(element = DataPoint(label, value))

                calendar.timeInMillis = endTime // move calendar for the next step

            }
            result
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Statistics",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Analyze your fitness journey",
                fontSize = 16.sp
            )

            Spacer(Modifier.height(15.dp))

            Text(
                text = "Time Period",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )

            FlowRow( //automatically arranges elements horizontally, if there is no space next element is in the next row
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
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
                TimeUnit.Days -> "Last 14 days"
                TimeUnit.Weeks -> "Last 8 Weeks"
                TimeUnit.Months -> "Last 12 months"
                TimeUnit.Years -> "All time data"
            }

            Text(
                text = periodDescription,
                fontSize = 10.sp
            )

            Spacer(Modifier.height(15.dp))

            Text(
                text = "Metric",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )

            FlowRow( //automatically arranges elements horizontally, if there is no space next element is in the next row
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
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

            Text(
                text = "Activity Type You Registered",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )

            FlowRow( //automatically arranges elements horizontally, if there is no space next element is in the next row
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
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
                text = "Statistics Chart",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.height(30.dp))

            StatisticsChart(
                data = chartPoints,
                selectedMetric = selectedMetric,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )

        }
    }
}


fun getStartCalendar(selectedTimeUnit: TimeUnit, firstEntry: ActivityEntry?): Calendar{
    return Calendar.getInstance().apply {
        set(java.util.Calendar.HOUR_OF_DAY, 0)
        set(java.util.Calendar.MINUTE, 0)
        set(java.util.Calendar.SECOND, 0)
        set(java.util.Calendar.MILLISECOND, 0)


        when (selectedTimeUnit) {
            TimeUnit.Days -> add(Calendar.DAY_OF_YEAR, -13) // 14 last days
            TimeUnit.Weeks -> {
                set(Calendar.DAY_OF_WEEK, firstDayOfWeek)
                add(Calendar.WEEK_OF_YEAR, -7) //last 8 weeks
            }

            TimeUnit.Months -> {
                set(Calendar.DAY_OF_MONTH, 1)
                add(Calendar.MONTH, -11) //last 12 months
            }

            TimeUnit.Years -> {
                if (firstEntry != null) {
                    time = firstEntry.date.toDate()
                    set(Calendar.MONTH, 0) //0 is january
                    set(Calendar.DAY_OF_MONTH, 1)
                } else {
                    timeInMillis = System.currentTimeMillis()
                }
            }
        }
    }
}