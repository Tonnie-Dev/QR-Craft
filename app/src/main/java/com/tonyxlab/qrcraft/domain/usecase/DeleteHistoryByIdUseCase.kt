package com.tonyxlab.qrcraft.domain.usecase

import com.tonyxlab.qrcraft.domain.model.Resource
import com.tonyxlab.qrcraft.domain.repository.QrRepository

class DeleteHistoryByIdUseCase(private val repository: QrRepository) {
    suspend operator fun invoke(id: Long): Boolean =
        when (val result = repository.deleteHistoryItemById(id = id)) {
            is Resource.Success -> result.data
            is Resource.Error -> throw result.exception
            else -> false
        }
}
