package com.tonyxlab.qrcraft.domain.usecase

import com.tonyxlab.qrcraft.domain.model.QrData
import com.tonyxlab.qrcraft.domain.model.Resource
import com.tonyxlab.qrcraft.domain.repository.QrRepository

class UpsertHistoryUseCase(private val repository: QrRepository) {
    suspend operator fun invoke(qrData: QrData): Long{
        return repository.upsertHistoryItem(qrData = qrData).let {
           result ->
            when(result){

                is Resource.Success -> result.data
                is Resource.Error -> throw result.exception
                else -> throw Exception("Unknown Error while upserting data")
            }
        }

    }
}