package com.example.fittracker.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QueryStats
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.unit.dp
import com.example.fittracker.ui.theme.ButtonsGreen

@Composable
fun BottomAppBar(
    currentScreen: String?,
    onNavigate: (String) -> Unit
){
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center){
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 8.dp //the color is darker by 8 dp then the background
            ){
                NavigationBarItem(
                    selected = currentScreen == "home",
                    onClick = { onNavigate("home") },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = ButtonsGreen,
                        selectedTextColor = ButtonsGreen
                    )
                )

                NavigationBarItem(
                    selected = currentScreen == "history",
                    onClick = { onNavigate("history") },
                    icon = { Icon(Icons.Default.History, contentDescription = "History") },
                    label = { Text("History") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = ButtonsGreen,
                        selectedTextColor = ButtonsGreen
                    )
                )

                NavigationBarItem(selected = false, onClick = {}, icon = {}, enabled = false) //space for floating action button

                NavigationBarItem(
                    selected = currentScreen == "statistics",
                    onClick = { onNavigate("statistics") },
                    icon = { Icon(Icons.Default.QueryStats, contentDescription = "Statistics") },
                    label = { Text("Statistics") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = ButtonsGreen,
                        selectedTextColor = ButtonsGreen
                    )
                )

                NavigationBarItem(
                    selected = currentScreen == "profile",
                    onClick = { onNavigate("profile") },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                    label = { Text("Profile") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = ButtonsGreen,
                        selectedTextColor = ButtonsGreen
                    )
                )

            }

        FloatingActionButton(
            onClick = { onNavigate("addActivity") },
            containerColor = ButtonsGreen,
            shape = CircleShape
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add",
                tint = Color.White) //so the plus is white
        }

    }
}