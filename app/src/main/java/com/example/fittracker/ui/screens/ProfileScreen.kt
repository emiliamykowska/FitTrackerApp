package com.example.fittracker.ui.screens

import android.widget.Button
import androidx.compose.runtime.Composable
import com.example.fittracker.ui.components.RoundedButton

@Composable
fun ProfileScreen(onNavigate: (String) -> Unit, onLogout: () -> Unit){
    RoundedButton(text="Log out", onClick = onLogout)
}