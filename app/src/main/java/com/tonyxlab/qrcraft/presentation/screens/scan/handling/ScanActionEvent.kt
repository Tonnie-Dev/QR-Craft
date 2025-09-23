package com.tonyxlab.qrcraft.presentation.screens.scan.handling

import com.tonyxlab.qrcraft.presentation.core.base.handling.ActionEvent

sealed interface ScanActionEvent: ActionEvent{


    data object NavigateToScanResult: ScanActionEvent
}
