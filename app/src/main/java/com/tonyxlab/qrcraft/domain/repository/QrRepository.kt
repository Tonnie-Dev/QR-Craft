package com.tonyxlab.qrcraft.domain.repository

import com.tonyxlab.qrcraft.domain.model.HistoryType
import com.tonyxlab.qrcraft.domain.model.QrData
import com.tonyxlab.qrcraft.domain.model.Resource
import kotlinx.coroutines.flow.Flow

interface QrRepository {

    suspend fun upsertHistoryItem(qrData: QrData): Resource<Long>

    fun getHistoryItems(historyType: HistoryType): Flow<List<QrData>>

    suspend fun getHistoryItemById(id: Long): Resource<QrData>

    suspend fun deleteHistoryItemById(id: Long): Resource<Boolean>

    suspend fun clearAll(): Resource<Boolean>

    suspend fun setSnackbarShownStatus(isShown: Boolean)

    fun getSnackbarShownStatus(): Flow<Boolean>
}