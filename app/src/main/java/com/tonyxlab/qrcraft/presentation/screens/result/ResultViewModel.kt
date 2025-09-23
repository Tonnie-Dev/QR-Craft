package com.tonyxlab.qrcraft.presentation.screens.result

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.tonyxlab.qrcraft.domain.QrData
import com.tonyxlab.qrcraft.navigation.Destinations
import com.tonyxlab.qrcraft.presentation.core.base.BaseViewModel
import com.tonyxlab.qrcraft.presentation.screens.result.handling.ResultActionEvent
import com.tonyxlab.qrcraft.presentation.screens.result.handling.ResultUiEvent
import com.tonyxlab.qrcraft.presentation.screens.result.handling.ResultUiState

typealias BaseResultViewModel = BaseViewModel<ResultUiState, ResultUiEvent, ResultActionEvent>

class ResultViewModel(savedStateHandle: SavedStateHandle) : BaseResultViewModel() {

    init {
        val navArgs = savedStateHandle.toRoute<Destinations.ResultScreenDestination>()
        updateQrData(
                qrData = QrData(
                        displayName = navArgs.displayName,
                        data = navArgs.data,
                        qrDataType = navArgs.qrDataType
                )
        )
    }

    override val initialState: ResultUiState
        get() = ResultUiState()

    override fun onEvent(event: ResultUiEvent) {
        when (event) {

            ResultUiEvent.ShareContent -> Unit
            ResultUiEvent.CopyContent -> Unit
            ResultUiEvent.ExitResultScreen -> {
                sendActionEvent(ResultActionEvent.NavigateToScanScreen)
            }

        }
    }
    private fun updateQrData(qrData: QrData) {
        updateState { currentState.copy(dataState = ResultUiState.DataState(qrData = qrData)) }
    }
}
