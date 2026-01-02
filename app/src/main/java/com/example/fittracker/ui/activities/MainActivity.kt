package com.example.fittracker.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.fittracker.ui.components.BottomAppBar
import com.example.fittracker.ui.components.Header
import com.example.fittracker.ui.screens.AddingActivitiesScreen
import com.example.fittracker.ui.screens.HistoryScreen
import com.example.fittracker.ui.screens.HomeScreen
import com.example.fittracker.ui.screens.LoginScreen
import com.example.fittracker.ui.screens.ProfileScreen
import com.example.fittracker.ui.screens.RegisterScreen
import com.example.fittracker.ui.screens.StatisticsScreen
import com.example.fittracker.ui.theme.ButtonsGreen
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
                // navController controlls which screen user is in, the history of screens and the destination
                // the app remembers navController even after each microrefresh
                val startDestination = if (user != null) "home" else "login"
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                //currentBackStack changes the history of navigation into a state, so currentRoute is also updated (so currentRoute can be used)
                //it has to be a state coz Compose needs state to understand what the app should display/do right now
                val currentRoute = navBackStackEntry?.destination?.route
                //navBackStackEntry is the screen the user sees right now, .destination to see where this entry leads to (navDestination object), .route is the string name of this destination
                val shouldShowHeaderAndBar = currentRoute != "login" && currentRoute != "register"

                Scaffold(
                    topBar = {
                        if (shouldShowHeaderAndBar){
                            Header(user, hasActivities = true)
                        }
                    },
                    bottomBar = {
                        if (shouldShowHeaderAndBar){
                            BottomAppBar(
                                currentScreen = currentRoute ?: "home", //home, not start destination coz to get there user has to be logged in
                                onNavigate = { route -> navController.navigate(route)})
                        }
                    },
                    floatingActionButton = {
                        if (currentRoute == "home"){
                            FloatingActionButton(
                                onClick = { navController.navigate("addActivity") },
                                containerColor = ButtonsGreen,
                                shape = CircleShape
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Add",
                                    tint = Color.White)
                            }
                        }
                    },
                    floatingActionButtonPosition = FabPosition.Center
                ){ innerPadding -> //innerPadding is used so the content of the screen is shown between header and appbar, not under them
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                    ){
                        NavHost( //navHost is like a map which tells where to go when onNavigate with some parameter is called
                            // each composable is like an entry in this map (dictionary)
                            //it's also the place in which screens will be shown
                            navController = navController,
                            startDestination = startDestination
                        ){
                            composable("login"){
                                LoginScreen(onNavigate = { route -> navController.navigate(route) })
                            } //so when onNavigate("login") is in code it knows to go to LoginScreen, coz it calls function(LoginScreen)
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
                                ProfileScreen(
                                    onNavigate = { route -> navController.navigate(route) },
                                    onLogout = {
                                        Firebase.auth.signOut()
                                        navController.navigate("login") {popUpTo("home") {inclusive = true} } //go to login and pop whole history till home (from top to bottom) as it is always first screen if logged in, including home so can't login again by using back arrow
                                    }
                                )
                            }
                            composable("statistics"){
                                StatisticsScreen(onNavigate = { route -> navController.navigate(route) })
                            }
                            composable("addActivity"){
                                AddingActivitiesScreen(onNavigate = {route -> navController.navigate(route)})
                            }
                        }
                    }
                }
            }
        }
    }
}
