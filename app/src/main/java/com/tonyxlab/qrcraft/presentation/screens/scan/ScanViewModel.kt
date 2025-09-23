package com.tonyxlab.qrcraft.presentation.screens.scan

import android.R.attr.x
import com.tonyxlab.qrcraft.domain.QrData
import com.tonyxlab.qrcraft.presentation.core.base.BaseViewModel
import com.tonyxlab.qrcraft.presentation.screens.scan.handling.ScanActionEvent
import com.tonyxlab.qrcraft.presentation.screens.scan.handling.ScanUiEvent
import com.tonyxlab.qrcraft.presentation.screens.scan.handling.ScanUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber

typealias ScanBaseViewModel = BaseViewModel<ScanUiState, ScanUiEvent, ScanActionEvent>

class ScanViewModel : ScanBaseViewModel() {

    private val _scannedQrData = MutableStateFlow<QrData?>(null)
    val scannedData = _scannedQrData.asStateFlow()

    override val initialState: ScanUiState
        get() = ScanUiState()

    override fun onEvent(event: ScanUiEvent) {
        TODO("Not yet implemented")
    }

    fun onAnalyzing(active: Boolean) {
        updateState { it.copy(isLoading = active) }
    }

    fun onScanSuccess(qrData: QrData) {

        val output = buildString {

            appendLine("QR Type: ${qrData.qrDataType}")
            appendLine("displayName: ${qrData.displayName}")
            appendLine("QR Data: ${qrData.data}")
        }
        Timber.tag("ScanViewModel")
                .i(output)
        updateState { it.copy(qrDataState = ScanUiState.DataState(qrData = qrData)) }
        // _scannedQrData.value = qrData
        updateState { it.copy(isLoading = false) }
        sendActionEvent(ScanActionEvent.NavigateToScanResult)
    }
}