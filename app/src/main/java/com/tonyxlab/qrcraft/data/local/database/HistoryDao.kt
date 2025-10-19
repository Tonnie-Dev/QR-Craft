package com.tonyxlab.qrcraft.data.local.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.tonyxlab.qrcraft.domain.model.HistoryType
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {
    @Upsert
    suspend fun upsert(historyEntity: HistoryEntity): Long

    @Query(""" 
        SELECT * FROM history_table WHERE history_type =:historyType ORDER BY timestamp DESC
    """)
    fun getScannedItems(historyType: HistoryType): Flow<List<HistoryEntity>>

    @Query("SELECT * FROM history_table WHERE id = :id")
    suspend fun getItemById(id: Long): HistoryEntity?

    @Query("DELETE FROM history_table WHERE id = :id")
    suspend fun deleteItemById(id: Long): Int

    @Query("DELETE FROM history_table")
    suspend fun clearAll()
}