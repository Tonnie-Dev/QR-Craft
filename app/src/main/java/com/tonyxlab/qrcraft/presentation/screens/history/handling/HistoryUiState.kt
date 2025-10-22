package com.tonyxlab.qrcraft.presentation.screens.history.handling

import com.tonyxlab.qrcraft.domain.model.HistoryType
import com.tonyxlab.qrcraft.domain.model.QrData
import com.tonyxlab.qrcraft.presentation.core.base.handling.UiState

data class HistoryUiState(
    val historyTabType: HistoryType = HistoryType.SCANNED,
    val scannedHistoryList: List<QrData> = emptyList(),
    val generatedHistoryList: List<QrData> = emptyList()
) : UiState
