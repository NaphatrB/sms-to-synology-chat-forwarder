package com.example.smssynologychat.data

import androidx.room.*

@Dao
interface SmsMessageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(message: SmsMessageEntity): Long

    @Update
    suspend fun update(message: SmsMessageEntity)

    @Query("SELECT * FROM sms_messages WHERE status = :status")
    suspend fun getMessagesByStatus(status: SmsStatus): List<SmsMessageEntity>

    @Query("SELECT * FROM sms_messages WHERE id = :id")
    suspend fun getMessageById(id: Long): SmsMessageEntity?
}

