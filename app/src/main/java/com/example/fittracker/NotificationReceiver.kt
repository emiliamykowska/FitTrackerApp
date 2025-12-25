package com.example.fittracker

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

/**
 * BroadcastReceiver that is triggered when the scheduled alarm fires.
 *
 * This class represents the "entry point" for our local notification:
 * - The alarm is scheduled in [scheduleNotification] using [android.app.AlarmManager].
 * - When the alarm time is reached, Android delivers a broadcast intent to this receiver.
 * - In [onReceive], we build and show the actual notification.
 *
 * This demonstrates how the system can wake up our app at a specific time,
 * even if the UI is not currently visible.
 */
class NotificationReceiver : BroadcastReceiver() {

    /**
     * Called by the system when the alarm fires and the broadcast is delivered.
     *
     * @param context A [Context] we can use to access system services.
     * @param intent  The [Intent] that was originally wrapped in the [PendingIntent]
     *                when we scheduled the alarm. We use it to retrieve the message
     *                string that should be shown in the notification.
     */
    override fun onReceive(context: Context, intent: Intent) {
        // Read the notification message passed from scheduleNotification; fall back to a default.
        val message = intent.getStringExtra("message") ?: "Time to check your reminder"

        // This intent will be launched when the user taps the notification.
        // In this demo we simply reopen MainActivity.
        val tapIntent = Intent(context, MainActivity::class.java)

        val tapPendingIntent = PendingIntent.getActivity(
            context,
            /* requestCode = */ 0,
            tapIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Retrieve the system NotificationManager.
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Build a notification associated with the same channel that we created in MainActivity.
        val notification = NotificationCompat.Builder(
            context,
            NotificationUtils.CHANNEL_ID
        )
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Scheduled notification")
            .setContentText(message)
            .setContentIntent(tapPendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        // In this demo we always use notificationId = 1.
        // In a real app you might want to use different IDs for multiple notifications.
        notificationManager.notify(1, notification)
    }
}
