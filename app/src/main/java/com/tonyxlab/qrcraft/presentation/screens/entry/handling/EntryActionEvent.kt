package com.tonyxlab.qrcraft.presentation.screens.entry.handling

import androidx.annotation.StringRes
import com.tonyxlab.qrcraft.presentation.core.base.handling.ActionEvent

sealed interface EntryActionEvent: ActionEvent {
    data object NavigateToCreateScreen: EntryActionEvent
    data class NavigateToPreviewScreen (val id:Long): EntryActionEvent
    data class ShowToastMessage(@StringRes val messageRes: Int): EntryActionEvent
}