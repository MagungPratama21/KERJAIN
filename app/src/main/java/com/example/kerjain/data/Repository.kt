package com.example.kerjain.data

import kotlinx.coroutines.flow.Flow

class Repository(private val db: AppDatabase) {
    fun bookmarks(): Flow<List<Bookmark>> = db.bookmarkDao().getAllBookmarks()
    suspend fun insertBookmark(b: Bookmark) = db.bookmarkDao().insert(b)
    suspend fun deleteBookmark(b: Bookmark) = db.bookmarkDao().delete(b)
    suspend fun deleteAllBookmarks() = db.bookmarkDao().deleteAll()

    fun notifications(): Flow<List<NotificationItem>> = db.notificationDao().getAllNotifications()
    suspend fun insertNotification(n: NotificationItem) = db.notificationDao().insert(n)
    suspend fun deleteNotification(n: NotificationItem) = db.notificationDao().delete(n)
    suspend fun deleteAllNotifications() = db.notificationDao().deleteAll()
}
