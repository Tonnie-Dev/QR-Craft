package com.tonyxlab.qrcraft.presentation.screens.history.handling

import androidx.compose.runtime.Stable
import com.tonyxlab.qrcraft.domain.model.HistoryType
import com.tonyxlab.qrcraft.domain.model.QrData
import com.tonyxlab.qrcraft.presentation.core.base.handling.UiState
import com.tonyxlab.qrcraft.utils.generateLoremIpsum

@Stable
data class HistoryUiState(
    val scannedHistoryList: List<QrData> = emptyList(),
    val generatedHistoryList: List<QrData> = emptyList(),
    val historyTabType: HistoryType = HistoryType.SCANNED,
    val showBottomHistoryBottomSheet: Boolean = false,
    val selectedQrItem: SelectedQrItem = SelectedQrItem()

) : UiState {

    @Stable
    data class SelectedQrItem(
        val id: Long = -1L,
        val data: String = generateLoremIpsum(26)
    )
}
