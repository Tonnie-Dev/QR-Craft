package com.tonyxlab.qrcraft.presentation.screens.scan.handling

import androidx.compose.runtime.Stable
import com.tonyxlab.qrcraft.domain.QrData
import com.tonyxlab.qrcraft.domain.QrDataType
import com.tonyxlab.qrcraft.presentation.core.base.handling.UiState
import com.tonyxlab.qrcraft.util.generateLoremIpsum

data class ScanUiState(
    val isLoading: Boolean = false,
    val qrDataState: DataState = DataState()
) : UiState {

    @Stable
    data class DataState(
        val qrData: QrData = QrData(
                displayName = "Text",
                data = generateLoremIpsum(26),
                qrDataType = QrDataType.TEXT
        )
    )
}
