package com.example.fittracker.notifications

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import com.example.fittracker.NotificationUtils

object NotificationsUtils {
    const val CHANNEL_ID = "reminders_channel"

    // it is created only once, when app is launched
    fun createNotificationChannel(context: Context) {
        val name = "Reminders"
        val descriptionText = "Channel used for scheduled local notifications"
        val importance = NotificationManager.IMPORTANCE_DEFAULT

        val channel = NotificationChannel(NotificationUtils.CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        // Defines the channel with an ID, a user-visible name, and an importance level

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.createNotificationChannel(channel)
    }

    fun scheduleNotification(context: Context){
        val intent = Intent(context, NotificationsReceiver::class.java)
        // launch NotificationsReceiver class
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 18)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)

            if(before(Calendar.getInstance())){
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }

        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP, // wakes up the phone if is not used
            calendar.timeInMillis, // sets time calendar
            AlarmManager.INTERVAL_DAY, // wake up again in 24 hours
            pendingIntent
        )
    }


    fun cancelNotification(context: Context){
        val intent = Intent(context, NotificationsReceiver::class.java)
        // launch NotificationsReceiver class
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        ) //no create mean if there is notification with id 0 find it and return, but if not, dont create one but return null

        if (pendingIntent != null){ //if there is not PendingIntent, dont have to cancel it
            alarmManager.cancel(pendingIntent)
        }
    }
}