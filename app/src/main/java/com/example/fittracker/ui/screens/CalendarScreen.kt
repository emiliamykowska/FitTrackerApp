package com.example.fittracker.ui.screens


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fittracker.data.ActivityEntry
import com.example.fittracker.ui.components.DayCalendar
import java.time.YearMonth
import java.time.ZoneId
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment


@Composable
fun CalendarScreen(
    onNavigate: (String) -> Unit,
    allActivities: List<ActivityEntry>)
{
    var displayedMonth by remember { mutableStateOf(YearMonth.now()) }
    val activitiesByDate = remember(allActivities){
        allActivities.groupBy {
            it.date.toDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        }
    }

    val daysInMonth = remember(displayedMonth) {
        (1..displayedMonth.lengthOfMonth()).map { displayedMonth.atDay(it) }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp))    {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { displayedMonth = displayedMonth.minusMonths(1) }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Previous Month")
            }
            Text(
                text = "${displayedMonth.month} ${displayedMonth.year}",
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            IconButton(onClick = { displayedMonth = displayedMonth.plusMonths(1) }) {
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next Month")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            contentPadding = PaddingValues(3.dp)
        ) {
            val firstDayOfMonth = daysInMonth.first().dayOfWeek.value - 1 //calculate how many empty slots before first day

            items(firstDayOfMonth) {
                Spacer(modifier = Modifier.aspectRatio(1f))
            }

            items(daysInMonth) { date ->
            val activitiesForDay = activitiesByDate[date] ?: emptyList()
            DayCalendar(
                date = date,
                isCurrentMonth = true,
                activities = activitiesForDay)
        }
        }
    }
}