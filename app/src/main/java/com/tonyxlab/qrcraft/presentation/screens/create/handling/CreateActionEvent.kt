package com.tonyxlab.qrcraft.presentation.screens.create.handling

import com.tonyxlab.qrcraft.domain.QrDataType
import com.tonyxlab.qrcraft.presentation.core.base.handling.ActionEvent

sealed interface CreateActionEvent: ActionEvent{

    data class NavigateToDataScreen(val qrDataType: QrDataType): CreateActionEvent
}