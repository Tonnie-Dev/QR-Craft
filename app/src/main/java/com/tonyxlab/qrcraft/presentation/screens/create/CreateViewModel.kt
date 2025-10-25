package com.tonyxlab.qrcraft.presentation.screens.create

import com.tonyxlab.qrcraft.presentation.core.base.BaseViewModel
import com.tonyxlab.qrcraft.presentation.screens.create.handling.CreateActionEvent
import com.tonyxlab.qrcraft.presentation.screens.create.handling.CreateUiEvent
import com.tonyxlab.qrcraft.presentation.screens.create.handling.CreateUiState

typealias CreateBaseViewModel = BaseViewModel<CreateUiState, CreateUiEvent, CreateActionEvent>

class CreateViewModel : CreateBaseViewModel() {

    override val initialState: CreateUiState
        get() = CreateUiState()

    override fun onEvent(event: CreateUiEvent) {

        when (event) {
            CreateUiEvent.ExitCreateScreen -> {
                sendActionEvent(CreateActionEvent.NavigateToScanScreen)
            }

            is CreateUiEvent.SelectQrTab -> {

                sendActionEvent(
                        actionEvent = CreateActionEvent.NavigateToEntryScreen(
                                qrDataType = event.qrDataType
                        )
                )
            }
        }
    }
}
