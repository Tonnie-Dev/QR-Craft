package com.tonyxlab.qrcraft.presentation.screens.history.handling

import com.tonyxlab.qrcraft.presentation.core.base.handling.UiEvent

sealed interface HistoryUiEvent : UiEvent {
    data class SelectHistoryItem(val id:Long): HistoryUiEvent
    data class LongPressHistoryItem(val id: Long): HistoryUiEvent
    data object ShareHistoryItem : HistoryUiEvent
    data object DeleteHistoryItem : HistoryUiEvent
    data object DismissHistoryBottomSheet : HistoryUiEvent
    data object ExitHistoryScreen : HistoryUiEvent
}
