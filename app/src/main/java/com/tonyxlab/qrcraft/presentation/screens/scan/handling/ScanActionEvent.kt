package com.tonyxlab.qrcraft.presentation.screens.scan.handling

import com.tonyxlab.qrcraft.domain.model.QrData
import com.tonyxlab.qrcraft.presentation.core.base.handling.ActionEvent

sealed interface ScanActionEvent : ActionEvent {

    data class NavigateToScanResult(val qrData: QrData) : ScanActionEvent

}
