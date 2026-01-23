package com.example.fittracker.ui.activities

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.fittracker.data.ActivityEntry
import com.example.fittracker.notifications.NotificationsUtils
import com.example.fittracker.ui.components.BottomAppBar
import com.example.fittracker.ui.components.Header
import com.example.fittracker.ui.components.LoadingBox
import com.example.fittracker.ui.screens.AddingActivitiesScreen
import com.example.fittracker.ui.screens.HistoryScreen
import com.example.fittracker.ui.screens.HomeScreen
import com.example.fittracker.ui.screens.LoginScreen
import com.example.fittracker.ui.screens.ProfileScreen
import com.example.fittracker.ui.screens.RegisterScreen
import com.example.fittracker.ui.screens.StatisticsScreen
import com.example.fittracker.ui.theme.FitTrackerTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.util.Calendar
import androidx.activity.enableEdgeToEdge

class MainActivity: ComponentActivity(){
    override fun onCreate(savedInstanceState: Bundle?){ // the function is called once, when app is launched
        //savedInstanceState so the data is there (like email) after turning off the app
        //Bundle is like a dictionary, can be null if is launched first time
        super.onCreate(savedInstanceState)
        // before doing things below call parent onCreate
//        enableEdgeToEdge()

        NotificationsUtils.createNotificationChannel(this) //before setContent coz its not part of the ui and doesnt need to be refreshed
        requestNotificationPermissionIfNeeded()

        setContent { // tells to use jetpackcompose, not xml

            FitTrackerTheme {
//                var currentScreen by remember { mutableStateOf(if (user != null) "home" else "login") } //create state that remembers which screen is shown, login at first
                // mutablestateof does recompososition, so if it's changed the screen is refreshed
                // remember so the state states there after each micro refresh
                // currentscreen is of type MutableState<String>, so there is by so .value dont have to be used
                // by is a "delegat"

                val context = LocalContext.current
                val preferences = remember {context.getSharedPreferences("FitTrackerPreferences", Context.MODE_PRIVATE)}
                var notificationsEnabled by remember { mutableStateOf(preferences.getBoolean("notifications_enabled", true)) }

                var user by remember { mutableStateOf(Firebase.auth.currentUser) }
                var allActivities by remember {mutableStateOf<List<ActivityEntry>?>(null)}
                val db = FirebaseFirestore.getInstance()

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

                LaunchedEffect(Unit) { //change user state if was logged in
                    Firebase.auth.addAuthStateListener { auth ->
                        user = auth.currentUser
                    }
                }

                LaunchedEffect(user) {
                    val currentUser = user

                    if (currentUser != null) {
                        db.collection("activities")
                            .whereEqualTo("userId", currentUser.uid) //take activities with field userId = currentUSer.uid
                            .orderBy("date", Query.Direction.DESCENDING)
                            .addSnapshotListener { snapshot, error -> // listen for changes in firebase
                                if (error != null) {
                                    return@addSnapshotListener
                                }

                                if (snapshot != null){ //if is not null that means that collection("activities") was changed f.e. new activity added
                                    allActivities = snapshot.toObjects(ActivityEntry::class.java)} //to objects change json from firestore into ActivityEntry
                                //Use ActivityEntry class but since firebase is in java, it's kotlin structure has to be changes into class.java
                            }
                    }
                }

                val startOfThisWeekMillis = remember { //calculates it only once, after turning off the app
                    Calendar.getInstance().apply { //getting the date of now but modifying
                        set(Calendar.HOUR_OF_DAY, 0) //setting the time to 00:00:00:00, Monday
                        set(Calendar.MINUTE, 0)
                        set(Calendar.SECOND, 0)
                        set(Calendar.MILLISECOND, 0)
                        set(Calendar.DAY_OF_WEEK, firstDayOfWeek) // coz in some countries week starts from sunday and in some from monday
                    }.timeInMillis
                }

                val activitiesThisWeek = remember(allActivities){ //remember to calculate it only when allActivities is changed
                    allActivities?.filter { it.date.toDate().time >= startOfThisWeekMillis } ?: emptyList()
                }

                Scaffold(
                    topBar = {
                        if (shouldShowHeaderAndBar && currentRoute == "home"){
                            Header(
                                user = user,
                                showActivities = allActivities != null && activitiesThisWeek.isNotEmpty(),
                                activitiesThisWeek = activitiesThisWeek,
                                notificationsEnabled = notificationsEnabled)
                        }
                        else if (shouldShowHeaderAndBar) {
                            Header(
                                user = user,
                                showActivities = false,
                                notificationsEnabled = notificationsEnabled)
                        }
                    },
                    bottomBar = {
                        if (shouldShowHeaderAndBar){
                            BottomAppBar(
                                currentScreen = currentRoute ?: "home", //home, not start destination coz to get there user has to be logged in
                                onNavigate = { route -> navController.navigate(route)})
                        }
                    },
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
                                if (allActivities == null){
                                    LoadingBox()
                                }
                                else{
                                    HomeScreen(
                                        onNavigate = { route -> navController.navigate(route) },
                                        allActivities = allActivities!!
                                    )
                                }

                            }
                            composable("history"){
                                if (allActivities == null){
                                    LoadingBox()
                                }
                                else{
                                    HistoryScreen(
                                        onNavigate = { route -> navController.navigate(route) },
                                        allActivities = allActivities!!
                                    )
                                }

                            }
                            composable("profile"){
                                if (allActivities == null){
                                    LoadingBox()
                                }
                                else{
                                    ProfileScreen(
                                        onNavigate = { route -> navController.navigate(route) },
                                        onLogout = {
                                            Firebase.auth.signOut()
                                            navController.navigate("login") {popUpTo("home") {inclusive = true} } //go to login and pop whole history till home (from top to bottom) as it is always first screen if logged in, including home so can't login again by using back arrow
                                        },
                                        allActivities = allActivities!!,
                                        notificationsEnabled = notificationsEnabled,
                                        onNotificationsChanged = { notificationsEnabled = it }
                                    )
                                }

                            }
                            composable("statistics"){
                                if (allActivities == null){
                                    LoadingBox()
                                }
                                else{
                                    StatisticsScreen(
                                        onNavigate = { route -> navController.navigate(route) },
                                        allActivities = allActivities!!)
                                }

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

    private fun requestNotificationPermissionIfNeeded(){
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            //check users system version, if asking for permission is needed; tiramisu is code for API 33
            val hasPermission = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED //check if used already granted permission

            if (!hasPermission){
                ActivityCompat.requestPermissions( //display window with ask for permission if not already granted
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    1001
                )
            }
        }
    }
}
