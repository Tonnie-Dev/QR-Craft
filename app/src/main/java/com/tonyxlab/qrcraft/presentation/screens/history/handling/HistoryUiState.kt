package com.tonyxlab.qrcraft.presentation.screens.history.handling

import com.tonyxlab.qrcraft.domain.model.HistoryType
import com.tonyxlab.qrcraft.domain.model.QrData
import com.tonyxlab.qrcraft.presentation.core.base.handling.UiState

data class HistoryUiState(
    val historyTabType: HistoryType = HistoryType.SCANNED,
    val historyList: List<QrData> = emptyList()
) : UiState
