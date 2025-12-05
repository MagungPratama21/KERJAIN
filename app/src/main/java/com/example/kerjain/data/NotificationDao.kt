package com.example.kerjain.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {
    @Query("SELECT * FROM notifications ORDER BY createdAt DESC")
    fun getAllNotifications(): Flow<List<NotificationItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(notification: NotificationItem): Long

    @Delete
    suspend fun delete(notification: NotificationItem)

    @Query("DELETE FROM notifications")
    suspend fun deleteAll()
}
