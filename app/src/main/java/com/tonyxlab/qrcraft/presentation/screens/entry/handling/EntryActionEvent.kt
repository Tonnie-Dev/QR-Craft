package com.tonyxlab.qrcraft.presentation.screens.entry.handling

import com.tonyxlab.qrcraft.presentation.core.base.handling.ActionEvent

sealed interface EntryActionEvent: ActionEvent {
    data object NavigateToCreateScreen: EntryActionEvent
    data object NavigateToPreviewScreen: EntryActionEvent
}