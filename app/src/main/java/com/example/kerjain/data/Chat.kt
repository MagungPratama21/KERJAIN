package com.example.kerjain.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat")
data class Chat(
    @PrimaryKey(autoGenerate = true)
    val chat_id: Int = 0,

    val sender_id: Int,
    val receiver_id: Int,
    val message: String,
    val timestamp: Long
)
