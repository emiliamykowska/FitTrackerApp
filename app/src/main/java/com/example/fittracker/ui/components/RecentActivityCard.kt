package com.example.fittracker.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun RecentActivityCard(
    activityName: String,
    emoji: String,
    duration: Int,
    timestamp: Timestamp
){
    val sdf = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) } //Locale.getDefault gives users country name of months fe
    val formattedDate = sdf.format(timestamp.toDate()) //change timestamp to date and then format

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp),
        shape = RoundedCornerShape(15.dp),
        border = BorderStroke(1.dp, Color.Gray)
    ){
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)){

            Column (
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ){
                Text(
                    text = activityName,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
                Row (
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ){
                    Text(
                        text = "$duration min",
                        fontSize = 13.sp
                    )

                    Text(
                        text = formattedDate,
                        fontSize = 13.sp
                    )
                }
            }

            Text(
                text = emoji,
                fontSize = 20.sp
            )

        }
    }
}