package com.mobi.ripple.feature_app.feature_chat.service.util

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.mobi.ripple.MainActivity
import com.mobi.ripple.R
import com.mobi.ripple.core.data.common.AppDatabase
import com.mobi.ripple.feature_app.feature_chat.service.dto.message.ChatEventType
import timber.log.Timber

class NotificationHandler(
    val context: Context,
    val database: AppDatabase
) {
    suspend fun createNewChatMessageNotification(chatId: String) {
        //  No back-stack when launched
        Timber.i("Building notification...")
        val message = database.messageDao.getLatestMessageByChatId(chatId).toMessage()
        if (message.eventType != ChatEventType.NEW_MESSAGE) return

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val chat = database.chatDao.getChatById(message.chatId)

        createNotificationChannel(message.chatId) // This won't create a new channel everytime, safe to call

        val builder = NotificationCompat.Builder(context, chatId)
            .setSmallIcon(R.mipmap.ripple_logo_round)
            .setContentTitle(chat.chatName)
            .setContentText("New message")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent) // For launching the MainActivity
            .setAutoCancel(true) // Remove notification when tapped
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC) // Show on lock screen
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            ActivityCompat.checkSelfPermission(
                context, Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        with(NotificationManagerCompat.from(context)) {
            notify(1, builder.build())
        }
        Timber.i("Notification built, should be visible now")
    }

    /**
     * Required on Android O+
     */
    private fun createNotificationChannel(channelId: String) {
        val name = "New messages"
        val descriptionText = "This channel sends new messages sent in chats"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(channelId, name, importance).apply {
            description = descriptionText
        }
        // Register the channel with the system
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

    }

}