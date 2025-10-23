package com.tonyxlab.qrcraft.data.repository

import com.tonyxlab.qrcraft.data.local.database.HistoryDao
import com.tonyxlab.qrcraft.data.local.database.HistoryEntity
import com.tonyxlab.qrcraft.data.local.database.mappers.toEntity
import com.tonyxlab.qrcraft.data.local.database.mappers.toModel
import com.tonyxlab.qrcraft.data.local.datastore.DataStore
import com.tonyxlab.qrcraft.domain.exception.ItemNotFoundException
import com.tonyxlab.qrcraft.domain.model.HistoryType
import com.tonyxlab.qrcraft.domain.model.QrData
import com.tonyxlab.qrcraft.domain.repository.QrRepository
import com.tonyxlab.qrcraft.util.safeIoCall
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber

class QrRepositoryImpl(
    private val datastore: DataStore,
    private val dao: HistoryDao
) : QrRepository {

    override suspend fun upsertHistoryItem(qrData: QrData) =
        safeIoCall { dao.upsertItem(historyEntity = qrData.toEntity()) }

    override fun getHistoryItems(historyType: HistoryType) =
        dao.getHistoryItems(historyType = historyType)
                .map { entities ->
                    entities.map(HistoryEntity::toModel)
                }

    override suspend fun getHistoryItemById(id: Long) = safeIoCall {
        val item =
            dao.getHistoryItemById(id = id) ?: throw ItemNotFoundException(id = id)
        item.toModel()
    }

    override suspend fun deleteHistoryItemById(id: Long) = safeIoCall {
        dao.deleteHistoryItemById(id = id) > 0
    }

    override suspend fun clearAll() = safeIoCall {
        dao.clearAll() > 0

    }

    override suspend fun setSnackbarShownStatus(isShown: Boolean) {
        datastore.setSnackbarShownStatus(isShown = isShown)
    }

    override fun getSnackbarShownStatus(): Flow<Boolean> {
        return datastore.getSnackbarShownStatus()
    }
}