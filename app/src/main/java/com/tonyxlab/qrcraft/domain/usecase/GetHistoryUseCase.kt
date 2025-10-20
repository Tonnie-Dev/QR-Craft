package com.tonyxlab.qrcraft.domain.usecase

import com.tonyxlab.qrcraft.domain.model.HistoryType
import com.tonyxlab.qrcraft.domain.model.QrData
import com.tonyxlab.qrcraft.domain.repository.QrRepository
import kotlinx.coroutines.flow.Flow

class GetHistoryUseCase(private val repository: QrRepository) {
    operator fun invoke(historyType: HistoryType): Flow<List<QrData>> {
        return repository.getHistoryItems(historyType = historyType)
    }
}


/*
operator fun invoke(historyType: HistoryType): Flow<Resource<List<QrData>>> {
    return repository.getHistoryItems(historyType)
            .map { Resource.Success(it) }
            .catch { emit(Resource.Error(it.message ?: "Unknown error")) }
}*/
