package com.tonyxlab.qrcraft.util

import com.tonyxlab.qrcraft.domain.model.HistoryType
import com.tonyxlab.qrcraft.domain.model.QrData
import com.tonyxlab.qrcraft.domain.model.QrDataType

fun getRandomQrDataItem(): QrData {

    val qrType = QrDataType.entries.random()

    val timestamp = listOf(
            1720870896000,
            1755083241000,
            1738381923000,
            1669773603000,
            1692593299000,
            1746693554000
    ).random()

    return QrData(
           //id =System.nanoTime() + (0..1000).random()
            displayName = generateLoremIpsum(1),
            prettifiedData = generateLoremIpsum(26),
            qrDataType = qrType,
            timestamp = timestamp,
            rawData = "",
            historyType = HistoryType.entries.random()
    )
}

fun getRandomQrDataItems(count: Int = 10): List<QrData> {

    return buildList {

        repeat(count) {
            add(getRandomQrDataItem().copy(id = it + 1L))
        }
    }
}