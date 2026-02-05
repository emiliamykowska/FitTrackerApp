package com.example.fittracker.ui.screens

import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.fittracker.data.ActivityEntry
import com.example.fittracker.ui.components.DayCalendar
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import java.time.YearMonth
import java.time.ZoneId

@Composable
fun CalendarScreen(
    onNavigate: (String) -> Unit,
    allActivities: List<ActivityEntry>){
    val activitiesByDate = remember(allActivities){
        allActivities.groupBy {
            it.date.toDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        }
    }

    val currentMonth = remember { YearMonth.now() }
    val state = rememberCalendarState(
        startMonth = remember { currentMonth.minusMonths(12) }, //users can see 1 year of workouts in the calendar
        endMonth = currentMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = firstDayOfWeekFromLocale()
    )

    HorizontalCalendar(
        state = state,
        monthHeader = { month ->
            Text(
                text = "${month.yearMonth.month} ${month.yearMonth.year}",
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        },
        dayContent = { day ->
            val activitiesForDay = activitiesByDate[day.date] ?: emptyList()
            DayCalendar(day = day, activities = activitiesForDay)
        }
    )
}