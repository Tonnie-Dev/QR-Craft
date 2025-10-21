package com.tonyxlab.qrcraft.domain.usecase

import com.tonyxlab.qrcraft.domain.exception.ItemNotFoundException
import com.tonyxlab.qrcraft.domain.model.QrData
import com.tonyxlab.qrcraft.domain.model.Resource
import com.tonyxlab.qrcraft.domain.repository.QrRepository

class GetHistoryByIdUseCase(private val repository: QrRepository) {
    suspend operator fun invoke(id: Long): QrData {
        return repository.getHistoryItemById(id = id)
                .let {
                    when (it) {

                        is Resource.Success -> it.data
                        is Resource.Error -> throw it.exception
                        else -> throw ItemNotFoundException(id = id)
                    }
                }
    }
}