package com.tonyxlab.qrcraft.presentation.screens.scan

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.tonyxlab.qrcraft.R
import com.tonyxlab.qrcraft.domain.model.HistoryType
import com.tonyxlab.qrcraft.domain.model.QrData
import com.tonyxlab.qrcraft.domain.repository.QrRepository
import com.tonyxlab.qrcraft.domain.usecase.UpsertHistoryUseCase
import com.tonyxlab.qrcraft.presentation.core.base.BaseViewModel
import com.tonyxlab.qrcraft.presentation.screens.scan.handling.ScanActionEvent
import com.tonyxlab.qrcraft.presentation.screens.scan.handling.ScanUiEvent
import com.tonyxlab.qrcraft.presentation.screens.scan.handling.ScanUiState
import com.tonyxlab.qrcraft.util.toMillis
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import java.time.LocalDateTime

typealias ScanBaseViewModel = BaseViewModel<ScanUiState, ScanUiEvent, ScanActionEvent>

class ScanViewModel(
    private val qrRepository: QrRepository,
    private val upsertHistoryUseCase: UpsertHistoryUseCase
) : ScanBaseViewModel() {

    private var hasScanned = false

    init {
        observeCamSnackbarShownStatus()
    }

    override val initialState: ScanUiState
        get() = ScanUiState()

    override fun onEvent(event: ScanUiEvent) {
        when (event) {
            ScanUiEvent.ToggleTorch -> toggleTorch()

        }
    }

    fun onAnalyzing(active: Boolean) {
        updateState { it.copy(isLoading = active) }
    }

    fun onScanSuccess(qrData: QrData) {

        if (hasScanned) return
        hasScanned = true

        launch {
            delay(1_000)
            updateState { it.copy(isLoading = false) }
            upsertQrItem(qrData = qrData)
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

    private fun upsertQrItem(qrData: QrData) {
        launchCatching(
                onError = {
                    sendActionEvent(
                            ScanActionEvent.ShowToast(
                                    messageRes = R.string.toast_text_item_not_saved
                            )
                    )
                }
        ) {
            val now = LocalDateTime.now()
                    .toMillis()
            val historyType = HistoryType.SCANNED

            upsertHistoryUseCase(
                    qrData = qrData.copy(
                            historyType = historyType,
                            timestamp = now
                    )
            )
        }
    }

    private fun toggleTorch() {
        updateState { it.copy(isFlashLightOn = !currentState.isFlashLightOn) }
    }
     fun selectImage(imageUri: Uri) {

        updateState { it.copy(imageUri = imageUri) }
    }
}