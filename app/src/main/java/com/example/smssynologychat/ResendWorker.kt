package com.example.smssynologychat

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.smssynologychat.data.AppDatabase
import com.example.smssynologychat.data.SmsStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ResendWorker(appContext: Context, workerParams: WorkerParameters) : CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val db = AppDatabase.getInstance(applicationContext)
        val dao = db.smsMessageDao()
        val unsentMessages = dao.getMessagesByStatus(SmsStatus.PENDING) + dao.getMessagesByStatus(SmsStatus.FAILED)
        for (msg in unsentMessages) {
            SynologyWebhookService.sendSmsToWebhook(
                context = applicationContext,
                sender = msg.sender,
                message = msg.content,
                timestamp = msg.timestamp,
                messageId = msg.id
            )
        }
        Result.success()
    }
}

