package com.example.smssynologychat

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class BootReceiver : BroadcastReceiver() {
    private val tag = "BootReceiver"

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            Intent.ACTION_BOOT_COMPLETED,
            Intent.ACTION_MY_PACKAGE_REPLACED,
            Intent.ACTION_PACKAGE_REPLACED -> {
                Log.d(tag, "Boot completed or package updated, starting SMS Forwarder Service")

                val serviceIntent = Intent(context, SmsForwarderService::class.java)
                context.startForegroundService(serviceIntent)
            }
        }
    }
}
