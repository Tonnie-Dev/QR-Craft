package com.tonyxlab.qrcraft.presentation.screens.scan

import com.tonyxlab.qrcraft.domain.QrData
import com.tonyxlab.qrcraft.presentation.core.base.BaseViewModel
import com.tonyxlab.qrcraft.presentation.screens.scan.handling.ScanActionEvent
import com.tonyxlab.qrcraft.presentation.screens.scan.handling.ScanUiEvent
import com.tonyxlab.qrcraft.presentation.screens.scan.handling.ScanUiState
import kotlinx.coroutines.delay

typealias ScanBaseViewModel = BaseViewModel<ScanUiState, ScanUiEvent, ScanActionEvent>

class ScanViewModel : ScanBaseViewModel() {

    private var loadingSinceMs: Long? = null
    private val MIN_SPINNER_MS = 2050L

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
        launch {
            delay(1_000)
            updateState { it.copy(isLoading = false) }
            sendActionEvent(ScanActionEvent.NavigateToScanResult(qrData))
        }
    }
}