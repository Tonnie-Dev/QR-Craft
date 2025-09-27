package com.tonyxlab.qrcraft.data.repository

import com.tonyxlab.qrcraft.data.local.datastore.DataStore
import com.tonyxlab.qrcraft.domain.repository.QrRepository
import kotlinx.coroutines.flow.Flow

class QrRepositoryImpl(private val datastore: DataStore) : QrRepository {

    override suspend fun setSnackbarShownStatus(isShown: Boolean) {
        datastore.setSnackbarShownStatus(isShown = isShown)
    }

    override  fun getSnackbarShownStatus(): Flow<Boolean> {
        return datastore.getSnackbarShownStatus()
    }
}