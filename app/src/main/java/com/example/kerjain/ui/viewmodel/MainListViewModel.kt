package com.example.kerjain.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.kerjain.data.AppDatabase
import com.example.kerjain.data.Bookmark
import com.example.kerjain.data.NotificationItem
import com.example.kerjain.data.Repository
import kotlinx.coroutines.launch

class MainListViewModel(application: Application) : AndroidViewModel(application) {
    private val repo: Repository

    init {
        val db = AppDatabase.getDatabase(application)
        repo = Repository(db)
    }

    val bookmarks = repo.bookmarks().asLiveData()
    val notifications = repo.notifications().asLiveData()

    fun addBookmark(b: Bookmark) = viewModelScope.launch { repo.insertBookmark(b) }
    fun deleteBookmark(b: Bookmark) = viewModelScope.launch { repo.deleteBookmark(b) }
    fun clearBookmarks() = viewModelScope.launch { repo.deleteAllBookmarks() }

    fun addNotification(n: NotificationItem) = viewModelScope.launch { repo.insertNotification(n) }
    fun deleteNotification(n: NotificationItem) = viewModelScope.launch { repo.deleteNotification(n) }
    fun clearNotifications() = viewModelScope.launch { repo.deleteAllNotifications() }
}
