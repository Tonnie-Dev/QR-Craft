package com.tonyxlab.qrcraft.presentation.screens.result.handling

import androidx.compose.runtime.Stable
import com.tonyxlab.qrcraft.domain.QrData
import com.tonyxlab.qrcraft.domain.QrDataType
import com.tonyxlab.qrcraft.presentation.core.base.handling.UiState
import com.tonyxlab.qrcraft.util.generateLoremIpsum

data class ResultUiState(val dataState: DataState = DataState()) : UiState {
    @Stable
    data class DataState(
        val qrData: QrData = QrData(
                displayName = "Text",
                prettifiedData = generateLoremIpsum(26),
                qrDataType = QrDataType.TEXT,
                rawDataValue = ""
        )
    )
}
