package com.tonyxlab.qrcraft.presentation.screens.history.handling

import com.tonyxlab.qrcraft.presentation.core.base.handling.ActionEvent

sealed interface HistoryActionEvent : ActionEvent{

    data object ExitHistoryScreen: HistoryActionEvent
}