package com.example.smssynologychat

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SmsReceiver : BroadcastReceiver() {
    private val tag = "SmsReceiver"

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
            val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)

            for (message in messages) {
                val sender = message.originatingAddress ?: "Unknown"
                val messageBody = message.messageBody ?: ""
                val timestamp = message.timestampMillis

                Log.d(tag, "SMS received from: $sender, message: $messageBody")

                // Send to Synology Chat in background
                CoroutineScope(Dispatchers.IO).launch {
                    SynologyWebhookService.sendSmsToWebhook(
                        context = context,
                        sender = sender,
                        message = messageBody,
                        timestamp = timestamp
                    )
                }
            }
        }
    }
}
