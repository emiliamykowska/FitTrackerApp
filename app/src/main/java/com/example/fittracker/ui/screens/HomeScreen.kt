package com.example.fittracker.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.fittracker.ui.components.BottomAppBar
import com.example.fittracker.ui.components.Header
import com.example.fittracker.ui.components.RoundedButton
import com.example.fittracker.ui.theme.ButtonsGreen
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add

@Composable
fun HomeScreen(onNavigate: (String) -> Unit) {
    val user = Firebase.auth.currentUser

    Scaffold(
        bottomBar = {
            BottomAppBar(currentScreen = "home",
            onNavigate = { onNavigate("home") }
            )
        },

        floatingActionButton = {
            FloatingActionButton(
                onClick = { onNavigate("addActivity") },
                containerColor = ButtonsGreen,
                shape = CircleShape
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add",
                    tint = Color.White)
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ){  innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Header(user, hasActivities = true)


        }
    }
}