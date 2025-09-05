package com.example.smssynologychat

import android.content.Context
import android.util.Log
import com.example.smssynologychat.data.AppDatabase
import com.example.smssynologychat.data.SmsStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.*
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

object SynologyWebhookService {
    private val tag = "SynologyWebhookService"
    private val client = OkHttpClient()

    suspend fun sendSmsToWebhook(
        context: Context,
        sender: String,
        message: String,
        timestamp: Long,
        messageId: Long
    ) {
        try {
            val settingsStore = SettingsDataStore(context)
            val webhookUrl = settingsStore.webhookUrl.first()

            if (webhookUrl.isBlank()) {
                Log.w(tag, "Webhook URL not configured")
                return
            }

            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val formattedTime = dateFormat.format(Date(timestamp))

            val messageText = "**SMS Received**\n" +
                    "**From:** $sender\n" +
                    "**Time:** $formattedTime\n" +
                    "**Message:** $message"

            // Synology Chat webhook expects payload parameter
            val formBody = FormBody.Builder()
                .add("payload", "{\"text\":\"$messageText\"}")
                .build()

            val request = Request.Builder()
                .url(webhookUrl)
                .post(formBody)
                .build()

            val db = AppDatabase.getInstance(context)
            val dao = db.smsMessageDao()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.e(tag, "Failed to send webhook: ${e.message}", e)
                    @OptIn(DelicateCoroutinesApi::class)
                    GlobalScope.launch(Dispatchers.IO) {
                        val msg = dao.getMessageById(messageId)
                        if (msg != null) {
                            dao.update(msg.copy(status = SmsStatus.FAILED))
                        }
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    @OptIn(DelicateCoroutinesApi::class)
                    GlobalScope.launch(Dispatchers.IO) {
                        val msg = dao.getMessageById(messageId)
                        if (response.isSuccessful) {
                            Log.d(tag, "Successfully sent SMS to Synology Chat")
                            if (msg != null) {
                                dao.update(msg.copy(status = SmsStatus.SENT))
                            }
                        } else {
                            Log.e(tag, "Webhook request failed: ${response.code} ${response.message}")
                            if (msg != null) {
                                dao.update(msg.copy(status = SmsStatus.FAILED))
                            }
                        }
                    }
                    response.close()
                }
            })

        } catch (e: Exception) {
            Log.e(tag, "Error sending webhook: ${e.message}", e)
            val db = AppDatabase.getInstance(context)
            val dao = db.smsMessageDao()
            @OptIn(DelicateCoroutinesApi::class)
            GlobalScope.launch(Dispatchers.IO) {
                val msg = dao.getMessageById(messageId)
                if (msg != null) {
                    dao.update(msg.copy(status = SmsStatus.FAILED))
                }
            }
        }
    }

    suspend fun testWebhook(context: Context): Result<String> {
        return try {
            val settingsStore = SettingsDataStore(context)
            val webhookUrl = settingsStore.webhookUrl.first()

            if (webhookUrl.isBlank()) {
                return Result.failure(Exception("Webhook URL not configured"))
            }

            val testMessage = "**Test Message**\n" +
                    "This is a test message from your SMS to Synology Chat forwarder app.\n" +
                    "If you can see this message, your webhook is configured correctly!"

            // Synology Chat webhook expects payload parameter
            val formBody = FormBody.Builder()
                .add("payload", "{\"text\":\"$testMessage\"}")
                .build()

            val request = Request.Builder()
                .url(webhookUrl)
                .post(formBody)
                .build()

            val response = client.newCall(request).execute()
            response.use {
                if (it.isSuccessful) {
                    Log.d(tag, "Test webhook sent successfully")
                    Result.success("Test message sent successfully!")
                } else {
                    val errorMsg = "Webhook test failed: ${it.code} ${it.message}"
                    Log.e(tag, errorMsg)
                    Result.failure(Exception(errorMsg))
                }
            }
        } catch (e: Exception) {
            Log.e(tag, "Error testing webhook: ${e.message}", e)
            Result.failure(e)
        }
    }
}
