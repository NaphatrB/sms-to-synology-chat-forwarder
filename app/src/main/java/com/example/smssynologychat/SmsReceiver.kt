package com.example.smssynologychat

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.util.Log
import com.example.smssynologychat.data.AppDatabase
import com.example.smssynologychat.data.SmsMessageEntity
import com.example.smssynologychat.data.SmsStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SmsReceiver : BroadcastReceiver() {
    private val tag = "SmsReceiver"

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
            val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
            val db = AppDatabase.getInstance(context)
            val dao = db.smsMessageDao()
            for (message in messages) {
                val sender = message.originatingAddress ?: "Unknown"
                val messageBody = message.messageBody ?: ""
                val timestamp = message.timestampMillis

                Log.d(tag, "SMS received from: $sender, message: $messageBody")

                CoroutineScope(Dispatchers.IO).launch {
                    // Save to DB first
                    val smsEntity = SmsMessageEntity(
                        sender = sender,
                        content = messageBody,
                        timestamp = timestamp,
                        status = SmsStatus.PENDING
                    )
                    val id = dao.insert(smsEntity)
                    // Send to webhook, passing the DB id
                    SynologyWebhookService.sendSmsToWebhook(
                        context = context,
                        sender = sender,
                        message = messageBody,
                        timestamp = timestamp,
                        messageId = id
                    )
                }
            }
        }
    }
}
