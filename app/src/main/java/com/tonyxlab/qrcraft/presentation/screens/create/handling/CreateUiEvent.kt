package com.tonyxlab.qrcraft.presentation.screens.create.handling

import com.tonyxlab.qrcraft.domain.QrDataType
import com.tonyxlab.qrcraft.presentation.core.base.handling.UiEvent

sealed interface CreateUiEvent: UiEvent{

    data class SelectQrTab(val qrDataType: QrDataType): CreateUiEvent
    data object ExitCreateScreen: CreateUiEvent
}