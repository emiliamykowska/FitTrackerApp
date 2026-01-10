package com.example.fittracker.notifications

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.fittracker.MainActivity
import com.example.fittracker.R
import java.util.Calendar

//receiver uses the channel, it's "woken up" when it's time to send notification
class NotificationsReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        val preferences = context.getSharedPreferences("FitTrackerPreferences", Context.MODE_PRIVATE) //find xml file
        val lastWorkoutTimestamp = preferences.getLong("last_workout_timestamp", 0L)
        //get lastWorkoutTimestamp, if its null get 0

        val lastWorkoutCalendar = Calendar.getInstance().apply { timeInMillis = lastWorkoutTimestamp }
        val today = Calendar.getInstance()

        val wasWorkoutToday = lastWorkoutCalendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                lastWorkoutCalendar.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)

        val title = if (wasWorkoutToday) "Great job today!" else "FitTracker Reminder"
        val message = if (wasWorkoutToday)
            "Nice work today! Check your stats!"
        else
            "Time for a quick exercise! Your goals are waiting."

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        // manages displaying the notifications

        val tapIntent = Intent(context, MainActivity::class.java)
        // Intent tell which screen should be opened after clicking on notification

        val tapPendingIntent = PendingIntent.getActivity(
            context, 0, tapIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        // PendingIntent allows Android notification system to launch the tapIntent
        // context is connection with the system, tells which app want to do sth
        // requestCode is id of notification (reminder, congratulation etc)
        // flag update tells to only update the notification if its the same as previous
        // flag immutable means that content of notification cant be changes after being sent

        val notification = NotificationCompat.Builder(
            context,
            NotificationsUtils.CHANNEL_ID //uses channel id created it Notifications Utils
        )
            .setSmallIcon(R.drawable.logo_ft)
            .setContentTitle(title)
            .setContentText(message)
            .setContentIntent(tapPendingIntent)
            .setAutoCancel(true) //notification vanishes after clicking
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        notificationManager.notify(1, notification) // show notification with this id
    }
}