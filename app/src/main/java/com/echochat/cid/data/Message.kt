package com.echochat.cid.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "messages",
    indices = [Index(value = ["chatWithUid"])]
)
data class Message(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val chatWithUid: String,
    val content: String,
    val isMine: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)
