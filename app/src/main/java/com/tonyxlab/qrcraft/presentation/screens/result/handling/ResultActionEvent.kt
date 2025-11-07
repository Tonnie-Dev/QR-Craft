package com.tonyxlab.qrcraft.presentation.screens.result.handling

import androidx.annotation.StringRes
import com.tonyxlab.qrcraft.domain.model.QrData
import com.tonyxlab.qrcraft.presentation.core.base.handling.ActionEvent

sealed interface ResultActionEvent : ActionEvent {
    data object NavigateToScanScreen : ResultActionEvent
    data class ShareText(val text: String) : ResultActionEvent
    data class CopyText(val text: String) : ResultActionEvent
    data class SaveQrImage(val qrData: QrData) : ResultActionEvent
    data class ShowToastMessage(@StringRes val messageRes: Int) : ResultActionEvent

}