package com.example.fittracker.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import com.example.fittracker.ui.screens.HomeScreen
import com.example.fittracker.ui.screens.LoginScreen
import com.example.fittracker.ui.screens.RegisterScreen
import com.example.fittracker.ui.theme.DarkGreen
import com.example.fittracker.ui.theme.FitTrackerTheme
import com.example.fittracker.ui.theme.LightGreen
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class MainActivity: ComponentActivity(){
    override fun onCreate(savedInstanceState: Bundle?){ // the function is called once, when app is launched
        //savedInstanceState so the data is there (like email) after turning off the app
        //Bundle is like a dictionary, can be null if is launched first time
        super.onCreate(savedInstanceState)
        // before doing things below call parent onCreate

        val user = Firebase.auth.currentUser

        setContent { // tells to use jetpackcompose, not xml
            FitTrackerTheme {

                var currentScreen by remember { mutableStateOf(if (user != null) "home" else "login") } //create state that remembers which screen is shown, login at first
                // mutablestateof does recompososition, so if it's changed the screen is refreshed
                // remember so the state states there after each micro refresh
                // currentscreen is of type MutableState<String>, so there is by so .value dont have to be used
                // by is a "delegat"

                Box (modifier = Modifier
                    .fillMaxSize()
                    .background(Brush.verticalGradient(
                        colors = listOf(LightGreen, DarkGreen)))){
                    when (currentScreen){ //dont have to use currentScreen.value coz of by earlier
                        "login" -> LoginScreen(
                            onNavigateToRegister = {currentScreen = "register"},
                            onNavigateToHome = { currentScreen = "home" })
                        "register" -> RegisterScreen(onNavigateToLogin = {currentScreen = "login"})
                        "home" -> HomeScreen(onLogout = {
                            Firebase.auth.signOut()
                            currentScreen = "login"})
                    }
                }

            }
        }
    }
}
