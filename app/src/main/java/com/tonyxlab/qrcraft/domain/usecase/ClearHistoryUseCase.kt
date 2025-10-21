package com.tonyxlab.qrcraft.domain.usecase

import com.tonyxlab.qrcraft.domain.model.Resource
import com.tonyxlab.qrcraft.domain.repository.QrRepository

class ClearHistoryUseCase(private val repository: QrRepository) {
    suspend operator fun invoke(): Boolean =
        when (val result = repository.clearAll()) {
        is Resource.Success -> result.data
        is Resource.Error -> throw result.exception
        else -> false
    }
}