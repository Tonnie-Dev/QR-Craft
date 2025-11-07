package com.tonyxlab.qrcraft.presentation.screens.preview.handling

import androidx.annotation.StringRes
import com.tonyxlab.qrcraft.domain.model.QrData
import com.tonyxlab.qrcraft.presentation.core.base.handling.ActionEvent

sealed interface PreviewActionEvent : ActionEvent {
    data class ShowToast(@StringRes val messageRes: Int) : PreviewActionEvent
    data class ShareText(val text: String) : PreviewActionEvent
    data class CopyText(val text: String) : PreviewActionEvent
    data class SaveImage(val qrData: QrData) : PreviewActionEvent
    data object NavigateToEntryScreen : PreviewActionEvent

}
