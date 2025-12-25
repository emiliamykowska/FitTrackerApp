package com.example.fittracker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context

/**
 * Utility object responsible for creating and managing notification channels.
 *
 * ### Why do we need a notification channel?
 * Starting from Android 8 (API level 26), every notification must belong
 * to a *notification channel*. A channel represents a user-visible category
 * of notifications, such as:
 *
 * - “Messages”
 * - “System alerts”
 * - “Reminders”
 *
 * Users can modify the behavior of each channel (sound, vibration, importance),
 * and once published, the channel's configuration cannot be changed by the app.
 *
 * ### How this class is used in the demo:
 * - `MainActivity` calls [createNotificationChannel] once at startup.
 * - `NotificationReceiver` posts notifications into this channel using [CHANNEL_ID].
 *
 * The channel must exist *before* any notification is shown, otherwise
 * the notification will not appear.
 */
object NotificationUtils {

    /**
     * ID of the notification channel used across the whole demo app.
     *
     * This value:
     * - must remain constant,
     * - must match the channel ID used in NotificationCompat.Builder,
     * - cannot be changed after the app is published.
     */
    const val CHANNEL_ID = "reminders_channel"

    /**
     * Creates the notification channel required for scheduled local notifications.
     *
     * On Android 8+ (API 26+), the system requires apps to register channels
     * before posting any notifications. On lower API levels this method
     * performs no work.
     *
     * @param context Any valid context, typically the application or activity context.
     */
    fun createNotificationChannel(context: Context) {


        val name = "Reminders"
        val descriptionText = "Channel used for scheduled local notifications"
        val importance = NotificationManager.IMPORTANCE_HIGH

        // Define the channel with an ID, a user-visible name, and an importance level.
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }

        // Register the channel with the system.
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.createNotificationChannel(channel)
    }
}
