package com.tonyxlab.qrcraft.presentation.screens.scan

import android.content.Context
import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
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
import com.tonyxlab.qrcraft.util.toQrData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.tasks.await
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
            ScanUiEvent.ShowDialog -> {
                updateState { it.copy(showDialog = true) }
            }

            ScanUiEvent.DismissDialog -> {
                updateState { it.copy(showDialog = false) }
            }
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

    fun updateCameraAnalyzingState(active: Boolean) {
        if (currentState.isGalleryAnalyzing) return
        updateState { it.copy(isLoading = active) }
    }

    fun onScanSuccess(qrData: QrData, delay: Long = 1_000) {
        if (hasScanned) return
        hasScanned = true

        launch {
            delay(delay)
            updateState { it.copy(isLoading = false) }
            upsertQrItem(qrData = qrData)
            sendActionEvent(ScanActionEvent.NavigateToScanResult(qrData))
        }
    }

    fun selectImage(imageUri: Uri) {
        updateState { it.copy(imageUri = imageUri) }
    }

    fun analyzeGalleryImage(context: Context, imageUri: Uri) {

        launchCatching(
                onStart = { updateState { it.copy(isLoading = true, isGalleryAnalyzing = true) } },
                onCompletion = {
                    updateState {
                        it.copy(isLoading = false, isGalleryAnalyzing = false)
                    }
                }
        ) {

            val inputImage = InputImage.fromFilePath(context, imageUri)
            val scanner = BarcodeScanning.getClient()

            val barcodes = scanner.process(inputImage)
                    .await()

            barcodes.ifEmpty {
                sendActionEvent(ScanActionEvent.ShowDialog)
                return@launchCatching
            }

            val qrData = barcodes.first()
                    .toQrData()
            delay(1_000)
            onScanSuccess(qrData = qrData, delay = 0L)
        }
    }

    private fun toggleTorch() {
        updateState { it.copy(isFlashLightOn = !currentState.isFlashLightOn) }
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
}