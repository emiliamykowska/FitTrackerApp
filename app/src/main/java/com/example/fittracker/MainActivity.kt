package com.example.fittracker

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.Calendar

/**
 * String literal for the Android 13+ notification permission.
 *
 * In a typical project you would use [Manifest.permission.POST_NOTIFICATIONS].
 * Here we keep the raw string to emphasize that permissions are just well-known
 * string constants under the hood, and to avoid potential SDK version issues.
 */
private const val POST_NOTIFICATIONS_PERMISSION = "android.permission.POST_NOTIFICATIONS"

/**
 * Entry point of the demo application.
 *
 * This activity does three important things related to the lecture:
 *
 * 1. Creates a notification channel (Android 8+ requirement).
 * 2. Requests the POST_NOTIFICATIONS runtime permission on Android 13+.
 * 3. Hosts the Jetpack Compose UI that lets the user configure and schedule
 *    a local notification using [AlarmManager].
 *
 * The goal is to connect the theoretical part of the lecture (local vs push
 * notifications, channels, permissions, AlarmManager) with a minimal
 * but complete working example.
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1) Create the notification channel (required on Android 8+).
        NotificationUtils.createNotificationChannel(this)

        // 2) Ask for the notification permission on Android 13+ (API 33+).
        requestNotificationPermissionIfNeeded()

        // 3) Set up the Compose UI.
        setContent {
            MaterialTheme {
                NotificationScreen()
            }
        }
    }

    /**
     * Requests the POST_NOTIFICATIONS permission on Android 13+ (API 33+).
     *
     * This demonstrates the difference between:
     *  - "declaring" a permission in AndroidManifest.xml, and
     *  - "requesting" it at runtime for dangerous permissions.
     *
     * For a real app you would also handle the case when the user denies
     * the permission, but for this classroom demo we keep it simple.
     */
    private fun requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val hasPermission = ContextCompat.checkSelfPermission(
                this,
                POST_NOTIFICATIONS_PERMISSION
            ) == PackageManager.PERMISSION_GRANTED

            if (!hasPermission) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(POST_NOTIFICATIONS_PERMISSION),
                    /* requestCode = */ 1001
                )
            }
        }
    }
}

/**
 * Main composable screen of the demo.
 *
 * Responsibilities:
 * - Let the user type a notification message.
 * - Let the user choose a specific time (hour and minute).
 * - Trigger scheduling of a local notification via [scheduleNotification].
 *
 * This screen is intentionally simple and has no navigation
 * or ViewModel to keep the focus on notifications.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen() {
    val context = LocalContext.current

    var message by remember {
        mutableStateOf(TextFieldValue("Remember to review the slides"))
    }
    var selectedHour by remember { mutableStateOf(12) }
    var selectedMinute by remember { mutableStateOf(0) }

    val timePickerShown = remember { mutableStateOf(false) }

    if (timePickerShown.value) {
        ShowTimePickerDialog(
            initialHour = selectedHour,
            initialMinute = selectedMinute,
            onTimeSelected = { hour, minute ->
                selectedHour = hour
                selectedMinute = minute
                timePickerShown.value = false
            },
            onDismiss = { timePickerShown.value = false }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Local notification demo") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "This screen schedules a local notification " +
                        "at a specific time using AlarmManager.",
            )

            OutlinedTextField(
                value = message,
                onValueChange = { message = it },
                label = { Text("Notification message") },
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "Selected time: %02d:%02d".format(selectedHour, selectedMinute)
            )

            Button(
                onClick = { timePickerShown.value = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Pick time")
            }

            Button(
                onClick = {
                    scheduleNotification(
                        context = context,
                        hour = selectedHour,
                        minute = selectedMinute,
                        message = message.text
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Schedule local notification")
            }
        }
    }
}

/**
 * Wraps the classic [android.app.TimePickerDialog] in a composable-friendly way.
 *
 * The dialog is created inside [DisposableEffect] so that:
 * - it is shown when [timePickerShown] becomes true,
 * - it is properly dismissed if the composable leaves the composition.
 *
 * This is a good example of interoperating between Jetpack Compose and
 * existing Android UI components.
 */
@Composable
fun ShowTimePickerDialog(
    initialHour: Int,
    initialMinute: Int,
    onTimeSelected: (Int, Int) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    DisposableEffect(Unit) {
        val dialog = android.app.TimePickerDialog(
            context,
            { _, hourOfDay, minute ->
                onTimeSelected(hourOfDay, minute)
            },
            initialHour,
            initialMinute,
            /* is24HourView = */ true
        )

        dialog.setOnDismissListener { onDismiss() }
        dialog.show()

        onDispose {
            dialog.dismiss()
        }
    }
}

/**
 * Schedules a local notification for the given [hour] and [minute].
 *
 * This function connects several concepts from the lecture:
 *
 *  - AlarmManager:
 *      We use the system [AlarmManager] to ask Android to wake up our app
 *      at a specific wall-clock time.
 *
 *  - PendingIntent:
 *      We wrap an [Intent] to [NotificationReceiver] in a [PendingIntent],
 *      so that the system can "call back" into our app in the future,
 *      even if the app is not running in the foreground.
 *
 *  - Inexact alarms vs exact alarms:
 *      We use [AlarmManager.setWindow] rather than exact alarms
 *      (such as setExactAndAllowWhileIdle) to avoid the
 *      SCHEDULE_EXACT_ALARM / USE_EXACT_ALARM permissions.
 *      This keeps the demo simpler while still showing the mechanism.
 *
 * The [NotificationReceiver] will be invoked when the alarm fires
 * and is responsible for actually showing the notification.
 */
fun scheduleNotification(context: Context, hour: Int, minute: Int, message: String) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    // Intent telling Android which component should be invoked when the alarm fires.
    val intent = Intent(context, NotificationReceiver::class.java).apply {
        putExtra("message", message)
    }

    // PendingIntent grants AlarmManager permission to send this broadcast on our behalf.
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        0,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    // Compute the trigger time in wall-clock milliseconds.
    val calendar = Calendar.getInstance().apply {
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
        set(Calendar.HOUR_OF_DAY, hour)
        set(Calendar.MINUTE, minute)

        // If the selected time is already in the past today,
        // schedule the notification for the next day.
        if (before(Calendar.getInstance())) {
            add(Calendar.DAY_OF_YEAR, 1)
        }
    }

    val triggerAtMillis = calendar.timeInMillis

    // Inexact alarm – does not require SCHEDULE_EXACT_ALARM.
    // The system can slightly adjust the exact trigger time within this window
    // to optimize battery usage.
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        val flexWindowMillis = 60_000L // 1 minute window
        alarmManager.setWindow(
            AlarmManager.RTC_WAKEUP,
            triggerAtMillis,
            flexWindowMillis,
            pendingIntent
        )
    } else {
        alarmManager.set(
            AlarmManager.RTC_WAKEUP,
            triggerAtMillis,
            pendingIntent
        )
    }

    Toast.makeText(
        context,
        "Notification scheduled for %02d:%02d".format(hour, minute),
        Toast.LENGTH_LONG
    ).show()
}
