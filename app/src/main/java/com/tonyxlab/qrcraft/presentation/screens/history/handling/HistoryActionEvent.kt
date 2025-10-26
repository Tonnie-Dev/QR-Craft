package com.tonyxlab.qrcraft.presentation.screens.history.handling

import androidx.annotation.StringRes
import com.tonyxlab.qrcraft.presentation.core.base.handling.ActionEvent

sealed interface HistoryActionEvent : ActionEvent{
    data class OpenShareMenu(val text: String): HistoryActionEvent
    data class ShowToast(@StringRes val messageRes: Int): HistoryActionEvent
    data class NavigateToPreview(val id:Long): HistoryActionEvent
    data object ExitHistoryScreen: HistoryActionEvent
}