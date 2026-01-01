package com.example.fittracker.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QueryStats
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.fittracker.ui.theme.ButtonsGreen

@Composable
fun BottomAppBar(
    onNavigate: (String) -> Unit
){
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp //the color is darker by 8 dp then the background
    ){
        NavigationBarItem(
            selected = true,
            onClick = { onNavigate("home") },
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = ButtonsGreen,
                selectedTextColor = ButtonsGreen
            )
        )

        NavigationBarItem(
            selected = false,
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
            selected = true,
            onClick = { onNavigate("profile") },
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            label = { Text("Profile") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = ButtonsGreen,
                selectedTextColor = ButtonsGreen
            )
        )

        NavigationBarItem(
            selected = true,
            onClick = { onNavigate("statistics") },
            icon = { Icon(Icons.Default.QueryStats, contentDescription = "Statistics") },
            label = { Text("Statistics") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = ButtonsGreen,
                selectedTextColor = ButtonsGreen
            )
        )
    }
}