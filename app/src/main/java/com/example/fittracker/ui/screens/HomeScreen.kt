package com.example.fittracker.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fittracker.ui.components.RoundedButton
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
fun HomeScreen(onLogout: () -> Unit) {
    val user = Firebase.auth.currentUser

    Column(
        modifier = androidx.compose.ui.Modifier.fillMaxSize(),
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
    ) {
        Text(
            text = "Welcome, ${user?.email ?: "User"}",
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )

        androidx.compose.foundation.layout.Spacer(
            modifier = androidx.compose.ui.Modifier.height(20.dp)
        )

        RoundedButton(
            text = "Log Out",
            onClick = {
                onLogout()
            }
        )
    }
}