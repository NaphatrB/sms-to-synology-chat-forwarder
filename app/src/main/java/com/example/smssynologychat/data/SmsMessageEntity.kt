package com.example.smssynologychat.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sms_messages")
data class SmsMessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val sender: String,
    val content: String,
    val timestamp: Long,
    val status: SmsStatus
)

enum class SmsStatus {
    PENDING,
    SENT,
    FAILED
}

