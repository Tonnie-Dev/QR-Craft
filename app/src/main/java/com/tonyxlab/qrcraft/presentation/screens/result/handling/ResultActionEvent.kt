package com.tonyxlab.qrcraft.presentation.screens.result.handling

import com.tonyxlab.qrcraft.presentation.core.base.handling.ActionEvent

sealed class ResultActionEvent: ActionEvent{

    data object NavigateToScanScreen: ResultActionEvent()
}