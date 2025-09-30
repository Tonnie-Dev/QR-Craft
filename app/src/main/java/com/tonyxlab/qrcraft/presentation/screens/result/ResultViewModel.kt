package com.tonyxlab.qrcraft.presentation.screens.result

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.tonyxlab.qrcraft.R
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
                        prettifiedData = navArgs.prettifiedData,
                        qrDataType = navArgs.qrDataType,
                        rawDataValue = navArgs.rawDataValue
                )
        )
    }

    override val initialState: ResultUiState
        get() = ResultUiState()

    override fun onEvent(event: ResultUiEvent) {
        when (event) {

            ResultUiEvent.ShareContent -> {
                val text = currentState.dataState.qrData.prettifiedData
                sendActionEvent(ResultActionEvent.ShareText(text))
            }

            ResultUiEvent.CopyContent -> {
                val text = currentState.dataState.qrData.prettifiedData
                sendActionEvent(ResultActionEvent.CopyText(text))
                sendActionEvent(
                        ResultActionEvent.ShowToastMessage(
                                messageRes = R.string.toast_text_copied
                        )
                )
            }

            ResultUiEvent.ExitResultScreen -> {
                sendActionEvent(ResultActionEvent.NavigateToScanScreen)
            }

        }
    }

    private fun updateQrData(qrData: QrData) {
        updateState { currentState.copy(dataState = ResultUiState.DataState(qrData = qrData)) }
    }
}
