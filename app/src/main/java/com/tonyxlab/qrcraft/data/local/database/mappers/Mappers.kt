package com.tonyxlab.qrcraft.data.local.database.mappers

import com.tonyxlab.qrcraft.data.local.database.HistoryEntity
import com.tonyxlab.qrcraft.domain.model.QrData

fun HistoryEntity.toModel() = QrData(
        id = id,
        displayName = displayName,
        prettifiedData = prettifiedData,
        rawData = rawData,
        qrDataType = qrDataType,
        historyType = historyType,
        timestamp = timestamp
)

fun QrData.toEntity() = HistoryEntity(
        id = id,
        displayName = displayName,
        qrDataType = qrDataType,
        historyType = historyType,
        prettifiedData = prettifiedData,
        rawData = rawData,
        timestamp = timestamp
)


