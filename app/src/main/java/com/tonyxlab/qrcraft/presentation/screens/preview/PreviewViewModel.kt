package com.tonyxlab.qrcraft.presentation.screens.preview

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.tonyxlab.qrcraft.navigation.Destinations
import com.tonyxlab.qrcraft.presentation.core.base.BaseViewModel
import com.tonyxlab.qrcraft.presentation.screens.preview.handling.PreviewActionEvent
import com.tonyxlab.qrcraft.presentation.screens.preview.handling.PreviewActionEvent.CopyText
import com.tonyxlab.qrcraft.presentation.screens.preview.handling.PreviewActionEvent.ShareText
import com.tonyxlab.qrcraft.presentation.screens.preview.handling.PreviewUiEvent
import com.tonyxlab.qrcraft.presentation.screens.preview.handling.PreviewUiState
import com.tonyxlab.qrcraft.util.mapToQrData
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.serialization.json.Json

typealias PreviewBaseViewModel = BaseViewModel<PreviewUiState, PreviewUiEvent, PreviewActionEvent>

class PreviewViewModel(savedStateHandle: SavedStateHandle) : PreviewBaseViewModel() {

    init {

        val navArgs = savedStateHandle.toRoute<Destinations.PreviewScreenDestination>()
        val jsonMapString = navArgs.jsonMapString

        val valuesMap = Json.decodeFromString<Map<String, String>>(jsonMapString)

        val qrData = mapToQrData(valuesMap)
        updateState {
            it.copy(
                    valuesMap = valuesMap,
                    qrDataState = PreviewUiState.QrDataState(qrData = qrData)
            )
        }
        observeEditableText()
        updateTextFieldContent(qrData.displayName)

    }

    override val initialState: PreviewUiState
        get() = PreviewUiState()

    override fun onEvent(event: PreviewUiEvent) {

        when (event) {
            PreviewUiEvent.ExitPreviewScreen -> sendActionEvent(
                    actionEvent = PreviewActionEvent.NavigateToEntryScreen
            )

            PreviewUiEvent.ShareContent -> {
                val text = currentState.qrDataState.qrData.prettifiedData
                sendActionEvent(ShareText(text = text))

            }

            PreviewUiEvent.CopyContent -> {
                val text = currentState.qrDataState.qrData.prettifiedData
                sendActionEvent(CopyText(text = text))

            }

            PreviewUiEvent.EditDetectedContent -> {

                updateState {
                    it.copy(
                            previewEditableTextState = currentState.previewEditableTextState.copy(
                                    isEditing = true
                            )
                    )
                }
            }
        }
    }

    @OptIn(FlowPreview::class)
    private fun observeEditableText() {

        val textFlow =
            snapshotFlow { currentState.previewEditableTextState.textFieldState.text }

        textFlow.debounce(3_00)
                .distinctUntilChanged()
                .onEach {

                }
                .launchIn(viewModelScope)
    }

    private fun updateTextFieldContent(value: String) {

        updateState {
            it.copy(
                    previewEditableTextState =
                        currentState.previewEditableTextState.copy(
                                textFieldState = buildTextFieldState(
                                        value = value
                                )
                        )
            )
        }
    }

    private fun buildTextFieldState(value: String): TextFieldState {
        return TextFieldState(initialText = value)
    }
}