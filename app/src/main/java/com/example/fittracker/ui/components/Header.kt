package com.example.fittracker.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fittracker.ui.theme.DarkGreen
import com.example.fittracker.ui.theme.LightGreen
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import com.google.firebase.auth.FirebaseUser

@Composable
fun Header(
    user: FirebaseUser?,
    hasActivities: Boolean
){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight() //adjusting height to different components
            .background(brush = Brush.verticalGradient(listOf(LightGreen, DarkGreen)),
                shape = RoundedCornerShape(bottomEnd = 15.dp, bottomStart = 15.dp))
            .padding(15.dp)

    ){
        Column(
        verticalArrangement =  Arrangement.spacedBy(10.dp)
        ){
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                Column { //by default column aligns to the left (Alignment.Start)
                    Text(
                        text = "Welcome back, ",
                        color = Color.White,
                        fontSize = 16.sp
                    )
                    Text(
                        text = user?.displayName ?: "User",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Your weekly stats:",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Thin
                    )
                }

                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notification icon",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }

            if (hasActivities){
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ){
                    HeaderStatCard(label = "Activities", value = "3", modifier = Modifier.weight(1f)) // for each to take 1/3 of space (each takes 1 part of the parts sum)
                    HeaderStatCard(label = "Total Time", value = "1.5h", modifier = Modifier.weight(1f))
                    HeaderStatCard(label = "Favourite Activity", value = "Running", modifier = Modifier.weight(1f))
                }
            }
        }
    }
}
