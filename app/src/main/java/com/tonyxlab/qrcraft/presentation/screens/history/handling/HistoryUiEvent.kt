package com.tonyxlab.qrcraft.presentation.screens.history.handling

import com.tonyxlab.qrcraft.presentation.core.base.handling.UiEvent

sealed interface HistoryUiEvent: UiEvent{

    data object PressBack: HistoryUiEvent
}
