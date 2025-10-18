package com.tonyxlab.qrcraft.data.local.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {

    @Upsert
    suspend fun insert(historyEntity: HistoryEntity):Long

    @Query("SELECT * FROM history_table ORDER BY timestamp DESC")
    fun getItems(): Flow<List<HistoryEntity>>

    @Query("SELECT * FROM history_table WHERE id = :id")
    suspend fun getItemById(id: Long): HistoryEntity?

    @Query("DELETE FROM history_table WHERE id = :id")
    suspend fun deleteItemById(id: Long): Int

    @Query("DELETE FROM history_table")
    suspend fun clearAllItems()
}