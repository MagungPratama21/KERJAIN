package com.example.kerjain.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ChatDao {
    @Insert
    fun insert(chat: Chat)

    @Query("""
        SELECT * FROM chat 
        WHERE (sender_id = :userId) OR (receiver_id = :userId)
        ORDER BY timestamp ASC
    """)
    fun getAllForUser(userId: Int): List<Chat>

    @Query("""
        SELECT * FROM chat 
        WHERE (sender_id = :user1 AND receiver_id = :user2)
           OR (sender_id = :user2 AND receiver_id = :user1)
        ORDER BY timestamp ASC
    """)
    fun getChat(user1: Int, user2: Int): LiveData<List<Chat>>
}
