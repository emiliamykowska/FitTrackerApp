package com.example.fittracker.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fittracker.data.ActivityEntry
import com.example.fittracker.ui.theme.LightGreen
import java.time.LocalDate

@Composable
fun DayCalendar(
    date: LocalDate,
    isCurrentMonth: Boolean,
    activities: List<ActivityEntry>
){
    Box(
        modifier = Modifier
            .aspectRatio(1f) //so each day is square of the same size, even with emojis inside
            .padding(1.dp)
            .background(
                color = if (activities.isNotEmpty()) LightGreen.copy(alpha = 0.2f) else Color.White,
                shape = RoundedCornerShape(8.dp)
            ),
        contentAlignment = Alignment.TopCenter
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(
                text = date.dayOfMonth.toString(),
                fontSize = 12.sp,
                color = if (isCurrentMonth) Color.Black else Color.LightGray //if day is from another month change color to gray
            )

            if (activities.isNotEmpty()){
                Row {
                    if (activities.size > 2){
                        activities.take(2).forEach {
                            Text(
                                text = it.emoji,
                                fontSize = 12.sp
                            )
                        }
                        Text(
                            text = "+",
                            fontSize = 12.sp
                        )
                    }
                    else{
                        activities.forEach {
                            Text(
                                text = it.emoji,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }
    }
}