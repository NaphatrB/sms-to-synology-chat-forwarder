package com.example.smssynologychat

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat

class SmsForwarderService : Service() {
    private val tag = "SmsForwarderService"
    private val CHANNEL_ID = "SMS_FORWARDER_CHANNEL"
    private val NOTIFICATION_ID = 1001

    override fun onCreate() {
        super.onCreate()
        Log.d(tag, "SMS Forwarder Service created")
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(tag, "SMS Forwarder Service started")
        startForegroundService()

        // Return START_STICKY to restart the service if killed
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "SMS Forwarder Service",
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "Keeps SMS forwarding active in background"
            setShowBadge(false)
        }

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun startForegroundService() {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, notificationIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("SMS Forwarder Active")
            .setContentText("Forwarding SMS messages to Synology Chat")
            .setSmallIcon(android.R.drawable.ic_dialog_email)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)
            .build()

        startForeground(NOTIFICATION_ID, notification)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(tag, "SMS Forwarder Service destroyed")

        // Restart the service if it gets killed
        val restartIntent = Intent(this, SmsForwarderService::class.java)
        startService(restartIntent)
    }
}
