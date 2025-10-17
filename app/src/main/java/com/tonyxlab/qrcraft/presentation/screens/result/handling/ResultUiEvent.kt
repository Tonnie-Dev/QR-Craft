package com.tonyxlab.qrcraft.presentation.screens.result.handling

import com.tonyxlab.qrcraft.presentation.core.base.handling.UiEvent

sealed interface ResultUiEvent: UiEvent{
    data object ShareContent: ResultUiEvent
    data object CopyContent: ResultUiEvent
    data object EditDetectedContent: ResultUiEvent
    data object ExitResultScreen: ResultUiEvent
}