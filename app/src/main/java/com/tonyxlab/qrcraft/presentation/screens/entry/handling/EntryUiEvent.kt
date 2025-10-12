package com.tonyxlab.qrcraft.presentation.screens.entry.handling

import com.tonyxlab.qrcraft.presentation.core.base.handling.UiEvent

sealed interface EntryUiEvent: UiEvent {
    data object ExitEntryScreen: EntryUiEvent
    data object GenerateQrCode: EntryUiEvent
}