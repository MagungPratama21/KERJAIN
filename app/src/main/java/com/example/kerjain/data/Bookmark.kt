package com.example.kerjain.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmarks")
data class Bookmark(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val jobId: String,
    val title: String,
    val company: String,
    val previewJson: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)

