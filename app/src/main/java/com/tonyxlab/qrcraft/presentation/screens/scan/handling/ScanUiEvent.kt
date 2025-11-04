package com.tonyxlab.qrcraft.presentation.screens.scan.handling

import com.tonyxlab.qrcraft.presentation.core.base.handling.UiEvent

sealed interface ScanUiEvent : UiEvent {
    data object ToggleTorch: ScanUiEvent


}
