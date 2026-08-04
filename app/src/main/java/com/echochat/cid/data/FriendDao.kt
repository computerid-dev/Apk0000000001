package com.echochat.cid.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FriendDao {

    @Insert
    suspend fun insert(friend: Friend): Long

    @Query("SELECT * FROM friends ORDER BY addedAt DESC")
    fun observeAll(): Flow<List<Friend>>

    @Query("SELECT * FROM friends WHERE friendUid = :uid LIMIT 1")
    suspend fun findByUid(uid: String): Friend?

    @Query("SELECT * FROM friends WHERE friendUid = :uid LIMIT 1")
    fun observeByUid(uid: String): Flow<Friend?>
}
