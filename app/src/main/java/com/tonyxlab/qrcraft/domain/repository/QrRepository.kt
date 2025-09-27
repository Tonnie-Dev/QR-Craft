package com.tonyxlab.qrcraft.domain.repository

import kotlinx.coroutines.flow.Flow

interface QrRepository {
    suspend fun setSnackbarShownStatus(isShown: Boolean)
     fun getSnackbarShownStatus(): Flow<Boolean>
}