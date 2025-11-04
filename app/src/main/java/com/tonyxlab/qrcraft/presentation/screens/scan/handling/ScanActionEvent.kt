package com.tonyxlab.qrcraft.presentation.screens.scan.handling

import androidx.annotation.StringRes
import com.tonyxlab.qrcraft.domain.model.QrData
import com.tonyxlab.qrcraft.presentation.core.base.handling.ActionEvent

sealed interface ScanActionEvent : ActionEvent {
    data class NavigateToScanResult(val qrData: QrData) : ScanActionEvent
    data class ShowToast(@StringRes val messageRes: Int) : ScanActionEvent
    data object ShowDialog : ScanActionEvent
}
