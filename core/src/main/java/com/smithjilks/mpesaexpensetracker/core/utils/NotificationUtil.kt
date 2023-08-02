package com.smithjilks.mpesaexpensetracker.core.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import com.smithjilks.mpesaexpensetracker.core.constants.MpesaExpenseTrackerScreens

class NotificationUtil(
    val context: Context,
    private val notificationIcon: Int,
    private val recordId: Int
) {
    companion object {
        private const val CHANNEL_ID = "com.smithjilks.mpesaexpensetracker.NewRecordNotification"
    }

    private val editRecordIntent = Intent(
        Intent.ACTION_VIEW,
        "app://open.smithjilks.mpesaexpensetracker/${MpesaExpenseTrackerScreens.RecordDetailsScreen}?recordId=$recordId".toUri(),
    )

    private val pendingIntent: PendingIntent = TaskStackBuilder.create(context).run {
        addNextIntentWithParentStack(editRecordIntent)
        getPendingIntent(0,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
    }


    private val builder = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(notificationIcon)
        .setContentTitle("New Record!!")
        .setContentText("Tap to edit default details for more meaningful tracking.")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)

    // Register the channel with the system
    private val notificationManager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "New Mpesa Transaction Record Notification"
            val descriptionText = "On new record added to database notification channel"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            notificationManager.createNotificationChannel(channel)
        }
    }


    fun showNotification() {
        createNotificationChannel()
        notificationManager.notify(recordId, builder.build())
    }
}