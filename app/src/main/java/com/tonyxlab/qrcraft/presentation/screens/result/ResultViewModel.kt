@file:OptIn(FlowPreview::class)

package com.tonyxlab.qrcraft.presentation.screens.result

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.tonyxlab.qrcraft.R
import com.tonyxlab.qrcraft.domain.model.QrData
import com.tonyxlab.qrcraft.navigation.Destinations
import com.tonyxlab.qrcraft.presentation.core.base.BaseViewModel
import com.tonyxlab.qrcraft.presentation.screens.result.handling.ResultActionEvent
import com.tonyxlab.qrcraft.presentation.screens.result.handling.ResultActionEvent.CopyText
import com.tonyxlab.qrcraft.presentation.screens.result.handling.ResultActionEvent.NavigateToScanScreen
import com.tonyxlab.qrcraft.presentation.screens.result.handling.ResultActionEvent.ShareText
import com.tonyxlab.qrcraft.presentation.screens.result.handling.ResultActionEvent.ShowToastMessage
import com.tonyxlab.qrcraft.presentation.screens.result.handling.ResultUiEvent
import com.tonyxlab.qrcraft.presentation.screens.result.handling.ResultUiState
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

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

        observeEditableText()
        updateTextContent(value = navArgs.displayName)
    }

    override val initialState: ResultUiState
        get() = ResultUiState()

    override fun onEvent(event: ResultUiEvent) {
        when (event) {

            ResultUiEvent.ShareContent -> {
                val text = currentState.dataState.qrData.prettifiedData
                sendActionEvent(ShareText(text))
            }

            ResultUiEvent.CopyContent -> {
                val text = currentState.dataState.qrData.prettifiedData
                sendActionEvent(CopyText(text))
                sendActionEvent(
                        ShowToastMessage(
                                messageRes = R.string.toast_text_copied
                        )
                )
            }

            ResultUiEvent.ExitResultScreen -> {
                sendActionEvent(NavigateToScanScreen)
            }

            ResultUiEvent.EditDetectedContent -> {
                updateState {
                    it.copy(
                            resultEditableTextState = currentState.resultEditableTextState.copy(
                                    isEditing = true
                            )
                    )
                }
            }
        }
    }

    private fun observeEditableText() {

        val textFlow = snapshotFlow {
            currentState.resultEditableTextState.textFieldState.text
        }

        textFlow.debounce(3_00)
                .distinctUntilChanged()
                .onEach {

                    // perform save
                }
                .launchIn(viewModelScope)

    }

    private fun updateQrData(qrData: QrData) {
        updateState { currentState.copy(dataState = ResultUiState.DataState(qrData = qrData)) }
    }

    private fun updateTextContent(value: String) {

        updateState {
            it.copy(
                    currentState.resultEditableTextState.copy(
                            textFieldState = buildTextFieldState(value)
                    )
            )
        }
    }

    private fun buildTextFieldState(value: String): TextFieldState {
        return TextFieldState(initialText = value)
    }
}
