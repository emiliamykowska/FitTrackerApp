package com.example.fittracker.ui.screens

import android.widget.Button
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material.icons.filled.PersonPin
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fittracker.data.ActivityEntry
import com.example.fittracker.ui.components.ProfileCard
import com.example.fittracker.ui.components.RoundedButton
import com.example.fittracker.ui.theme.ButtonsGreen
import com.example.fittracker.ui.theme.DarkGreen
import com.example.fittracker.ui.theme.LightGreen
import com.example.fittracker.ui.theme.OffWhite
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import java.time.ZoneId


@Composable
fun ProfileScreen(
    onNavigate: (String) -> Unit,
    onLogout: () -> Unit,
    allActivities: List<ActivityEntry>,
    notificationsEnabled: Boolean,
    onNotificationsChanged: (Boolean) -> Unit
){
    val currentUser = Firebase.auth.currentUser
    val scrollState = rememberScrollState()

    val totalActivities = allActivities.size

    val totalDuration = allActivities.sumOf { it.duration } / 60.0 //to get hours

    val favouriteActivity = allActivities
        .groupBy { it.activityName } //gives like a dictionary
        .maxByOrNull { it.value.size } //values are entries with given activityName, we want where it's greatest
         //activityName with the greatest number of entries

    val daysActive = allActivities
        .distinctBy { //it gives list so .size is used
            it.date.toDate() //changes timestamp to date
                .toInstant() //changes date to instant
                .atZone(ZoneId.systemDefault())  //uses instant to create date in user's time zone
                .toLocalDate() //gives only the date (ignores hours, minutes etc)
           }.size

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ){
        ProfileCard(
            icon = Icons.Default.PersonPin,
            value = "${currentUser?.displayName}",
            label ="${currentUser?.email}",
            modifierIcon = Modifier.size(55.dp)
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ){
            ProfileCard(
                icon = Icons.Default.EmojiEvents,
                value = "$totalActivities",
                label ="Activities",
                modifierCard = Modifier.weight(1f)
            )

            ProfileCard(
                icon =  Icons.Default.Timer,
                value = "%.1f h".format(totalDuration),
                label ="Total Time",
                modifierCard = Modifier.weight(1f)
            )

            ProfileCard(
                icon = Icons.Default.CalendarToday,
                value = "$daysActive",
                label ="Days Active",
                modifierCard = Modifier.weight(1f)
            )
        }

        if (favouriteActivity != null) {
            Card(
                shape = RoundedCornerShape(15.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.horizontalGradient(listOf(LightGreen, DarkGreen)),
                            shape = RoundedCornerShape(15.dp))
                        .padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {

                    Text(
                        text = "Favourite activity",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Text(
                        text = favouriteActivity.key,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Text(
                        text = "${favouriteActivity.value.size} sessions completed",
                        fontSize = 12.sp,
                        color = Color.White
                    )
                }
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(15.dp)){

            Column(
                modifier = Modifier.padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ){
                Text(
                    text = "Settings",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(15.dp),
                    colors = CardDefaults.cardColors(containerColor = OffWhite)
                ){
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ){
                            Icon(
                                imageVector = if (notificationsEnabled) Icons.Default.Notifications else Icons.Default.NotificationsOff,
                                contentDescription = "Notification icon",
                                tint = ButtonsGreen,
                                modifier = Modifier.size(20.dp)
                            )

                            Column(
                                verticalArrangement = Arrangement.Center
                            ){
                                Text(
                                    text = "Notifications",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = if (notificationsEnabled) "Enabled" else "Disabled",
                                    fontSize = 12.sp
                                )
                            }
                        }
                        Switch(
                            checked = notificationsEnabled,
                            onCheckedChange = onNotificationsChanged
                        )
                    }
                }

                RoundedButton(text="Log out", onClick = onLogout)
            }
        }
    }
}