package com.echochat.cid.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {

    @Insert
    suspend fun insert(message: Message): Long

    @Query("SELECT * FROM messages WHERE chatWithUid = :chatWithUid ORDER BY timestamp ASC")
    fun observeChat(chatWithUid: String): Flow<List<Message>>

    @Query("SELECT content FROM messages WHERE chatWithUid = :chatWithUid ORDER BY timestamp DESC LIMIT 1")
    suspend fun lastMessagePreview(chatWithUid: String): String?
}
