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
import timber.log.Timber

typealias ScanBaseViewModel = BaseViewModel<ScanUiState, ScanUiEvent, ScanActionEvent>

class ScanViewModel(private val qrRepository: QrRepository) : ScanBaseViewModel() {

    init {
        observeCamSnackbarShownStatus()
    }

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

    private fun observeCamSnackbarShownStatus() {
        qrRepository
                .getSnackbarShownStatus()
                .distinctUntilChanged()
                .onEach { shown ->

                    updateState { it.copy(camSnackbarShown = shown) }

                    Timber.tag("ScanViewModel").i("VW State Ejection: $shown")
                }
                .launchIn(viewModelScope)

    }

    fun updateCamSnackbarShownStatus(isShown: Boolean) {

        launch {

            Timber.tag("ScanViewModel").i("VW Update Cam-Shown-Status: $isShown")
            qrRepository.setSnackbarShownStatus(isShown)

        }
    }
}