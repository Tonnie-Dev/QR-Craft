@file:OptIn(FlowPreview::class)

package com.tonyxlab.qrcraft.presentation.screens.preview

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.tonyxlab.qrcraft.R
import com.tonyxlab.qrcraft.domain.usecase.GetHistoryByIdUseCase
import com.tonyxlab.qrcraft.domain.usecase.UpsertHistoryUseCase
import com.tonyxlab.qrcraft.navigation.Destinations
import com.tonyxlab.qrcraft.presentation.core.base.BaseViewModel
import com.tonyxlab.qrcraft.presentation.screens.preview.handling.PreviewActionEvent
import com.tonyxlab.qrcraft.presentation.screens.preview.handling.PreviewActionEvent.CopyText
import com.tonyxlab.qrcraft.presentation.screens.preview.handling.PreviewActionEvent.ShareText
import com.tonyxlab.qrcraft.presentation.screens.preview.handling.PreviewUiEvent
import com.tonyxlab.qrcraft.presentation.screens.preview.handling.PreviewUiState
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

typealias PreviewBaseViewModel = BaseViewModel<PreviewUiState, PreviewUiEvent, PreviewActionEvent>

class PreviewViewModel(
    private val getHistoryByIdUseCase: GetHistoryByIdUseCase,
    private val upsertHistoryUseCase: UpsertHistoryUseCase,
    savedStateHandle: SavedStateHandle
) : PreviewBaseViewModel() {

    private lateinit var oldDisplayName: String

    init {

        val navArgs = savedStateHandle
                .toRoute<Destinations.PreviewScreenDestination>()

        val id = navArgs.id

        loadQrDataItem(id = id)

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

    private fun observeEditableText() {

        val textFlow = snapshotFlow { currentState.previewEditableTextState.textFieldState.text }
        textFlow.debounce(3_00)
                .distinctUntilChanged()
                .onEach { newDisplayName ->

                    if (isDisplayNameEdited(newDisplayName.toString())) {

                        saveDisplayName(newDisplayName.toString())

                    }
                }
                .launchIn(viewModelScope)
    }

    private fun loadQrDataItem(id: Long) {

        launchCatching(
                onError = {
                    sendActionEvent(
                            PreviewActionEvent.ShowToast(
                                    messageRes = R.string.toast_text_item_not_found
                            )
                    )
                }
        ) {

            val storedItem = getHistoryByIdUseCase(id = id)
            oldDisplayName = storedItem.displayName
            updateState { it.copy(qrDataState = currentState.qrDataState.copy(qrData = storedItem)) }

            updateTextFieldContent(currentState.qrDataState.qrData.displayName)

            observeEditableText()
        }
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

    private fun saveDisplayName(displayName: String) {

        val updatedItem = currentState.qrDataState.qrData.copy(displayName = displayName)
        launchCatching(
                onError = {

                    sendActionEvent(
                            actionEvent = PreviewActionEvent.ShowToast(
                                    messageRes = R.string.toast_text_item_not_saved
                            )
                    )
                }
        ) {
            upsertHistoryUseCase(qrData = updatedItem)
        }
    }

    private fun buildTextFieldState(value: String): TextFieldState {
        return TextFieldState(initialText = value)
    }

    private fun isDisplayNameEdited(newDisplayName: String): Boolean {

        return this::oldDisplayName.isInitialized && oldDisplayName != newDisplayName
    }
}