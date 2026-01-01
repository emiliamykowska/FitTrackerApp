package com.example.fittracker.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fittracker.ui.screens.HistoryScreen
import com.example.fittracker.ui.screens.HomeScreen
import com.example.fittracker.ui.screens.LoginScreen
import com.example.fittracker.ui.screens.ProfileScreen
import com.example.fittracker.ui.screens.RegisterScreen
import com.example.fittracker.ui.screens.StatisticsScreen
import com.example.fittracker.ui.theme.FitTrackerTheme
import com.google.firebase.Firebase
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
//                var currentScreen by remember { mutableStateOf(if (user != null) "home" else "login") } //create state that remembers which screen is shown, login at first
                // mutablestateof does recompososition, so if it's changed the screen is refreshed
                // remember so the state states there after each micro refresh
                // currentscreen is of type MutableState<String>, so there is by so .value dont have to be used
                // by is a "delegat"

                val navController = rememberNavController()
                val startDestination = if (user != null) "home" else "login"

                NavHost(
                    navController = navController,
                    startDestination = startDestination
                ){
                    composable("login"){
                        LoginScreen(onNavigate = { route -> navController.navigate(route) })
                    }
                    composable("register"){
                        RegisterScreen(onNavigate = { route -> navController.navigate(route) })
                    }
                    composable("home"){
                        HomeScreen(onNavigate = { route -> navController.navigate(route) })
                    }
                    composable("history"){
                        HistoryScreen(onNavigate = { route -> navController.navigate(route) })
                    }
                    composable("profile"){
                        ProfileScreen(onNavigate = { route -> navController.navigate(route) },
                            onLogout = {
                                Firebase.auth.signOut()
                                navController.navigate("login") {popUpToId} //popUpToId so it's not possible to click back arrow to be logged in again
                            }
                        )
                    }
                    composable("statistics"){
                        StatisticsScreen(onNavigate = { route -> navController.navigate(route) })
                    }
                }
            }
        }
    }
}
