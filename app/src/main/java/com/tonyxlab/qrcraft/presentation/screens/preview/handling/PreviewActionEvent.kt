package com.tonyxlab.qrcraft.presentation.screens.preview.handling

import com.tonyxlab.qrcraft.presentation.core.base.handling.ActionEvent

sealed interface PreviewActionEvent : ActionEvent{


    data object NavigateToEntryScreen: PreviewActionEvent

    data class ShareText(val text: String) : PreviewActionEvent
    data class CopyText(val text: String) : PreviewActionEvent
}