package com.tonyxlab.qrcraft.presentation.screens.scan

import androidx.lifecycle.viewModelScope
import com.tonyxlab.qrcraft.domain.QrData
import com.tonyxlab.qrcraft.domain.repository.QrRepository
import com.tonyxlab.qrcraft.presentation.core.base.BaseViewModel
import com.tonyxlab.qrcraft.presentation.screens.scan.handling.ScanActionEvent
import com.tonyxlab.qrcraft.presentation.screens.scan.handling.ScanUiEvent
import com.tonyxlab.qrcraft.presentation.screens.scan.handling.ScanUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

typealias ScanBaseViewModel = BaseViewModel<ScanUiState, ScanUiEvent, ScanActionEvent>

class ScanViewModel(private val qrRepository: QrRepository) : ScanBaseViewModel() {

    init {
        observeCamSnackbarShownStatus()
    }

    override val initialState: ScanUiState
        get() = ScanUiState()

    override fun onEvent(event: ScanUiEvent) {
        when (event) {

            is ScanUiEvent.FabOptionSelected -> {}
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

    private fun observeCamSnackbarShownStatus() {
        qrRepository
                .getSnackbarShownStatus()
                .distinctUntilChanged()
                .onEach { shown ->
                    updateState { it.copy(camSnackbarShown = shown) }
                }
                .launchIn(viewModelScope)
    }

    fun updateCamSnackbarShownStatus(isShown: Boolean) {

        launch {

            qrRepository.setSnackbarShownStatus(isShown)

        }
    }
}