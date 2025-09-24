package com.tonyxlab.qrcraft.presentation.screens.scan

import com.tonyxlab.qrcraft.domain.QrData
import com.tonyxlab.qrcraft.presentation.core.base.BaseViewModel
import com.tonyxlab.qrcraft.presentation.screens.scan.handling.ScanActionEvent
import com.tonyxlab.qrcraft.presentation.screens.scan.handling.ScanUiEvent
import com.tonyxlab.qrcraft.presentation.screens.scan.handling.ScanUiState

typealias ScanBaseViewModel = BaseViewModel<ScanUiState, ScanUiEvent, ScanActionEvent>

class ScanViewModel : ScanBaseViewModel() {

    override val initialState: ScanUiState
        get() = ScanUiState()

    override fun onEvent(event: ScanUiEvent) {
        when (event) {
            ScanUiEvent.ExitScanScreen -> {

            }
        }
    }

    fun onAnalyzing(active: Boolean) {
        updateState { it.copy(isLoading = active) }
    }

    fun onScanSuccess(qrData: QrData) {
        updateState { it.copy(isLoading = false) }
        sendActionEvent(ScanActionEvent.NavigateToScanResult(qrData))
    }
}