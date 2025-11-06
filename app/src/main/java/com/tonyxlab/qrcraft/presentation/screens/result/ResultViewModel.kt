@file:OptIn(FlowPreview::class)

package com.tonyxlab.qrcraft.presentation.screens.result

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
import timber.log.Timber

typealias BaseResultViewModel = BaseViewModel<ResultUiState, ResultUiEvent, ResultActionEvent>

class ResultViewModel(
    private val getHistoryByIdUseCase: GetHistoryByIdUseCase,
    private val upsertHistoryUseCase: UpsertHistoryUseCase,
    savedStateHandle: SavedStateHandle
) : BaseResultViewModel() {

    private lateinit var oldDisplayName: String

    init {
        val navArgs =
            savedStateHandle.toRoute<Destinations.ResultScreenDestination>()
        loadQrDataItem(navArgs.id)

    }

    override val initialState: ResultUiState
        get() = ResultUiState()

    override fun onEvent(event: ResultUiEvent) {
        when (event) {

            ResultUiEvent.ShareContent -> {
                val text = currentState.qrData.prettifiedData
                sendActionEvent(ShareText(text))
            }

            ResultUiEvent.CopyContent -> {
                val text = currentState.qrData.prettifiedData
                sendActionEvent(CopyText(text))
                sendActionEvent(
                        ShowToastMessage(
                                messageRes = R.string.toast_text_copied
                        )
                )
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

            ResultUiEvent.ToggleFavorite -> toggleFavoriteStatus()

            ResultUiEvent.ExitResultScreen -> {
                sendActionEvent(NavigateToScanScreen)
            }
        }
    }

    private fun observeEditableText() {

        val textFlow = snapshotFlow {
            currentState.resultEditableTextState.textFieldState.text
        }

        textFlow.debounce(3_00)
                .distinctUntilChanged()
                .onEach { displayName ->

                    val newDisplayName =
                        if (displayName.isBlank()) displayName.toString() else oldDisplayName
                    if (isDisplayNameEdited(newDisplayName = newDisplayName))
                        saveDisplayName(displayName = newDisplayName)
                }
                .launchIn(viewModelScope)

    }

    private fun loadQrDataItem(id: Long) {

        launchCatching(
                onError = {
                    sendActionEvent(
                            actionEvent = ShowToastMessage(
                                    messageRes = R.string.toast_text_item_not_found
                            )
                    )
                }
        ) {
            val storedItem = getHistoryByIdUseCase(id = id)
            oldDisplayName = storedItem.displayName
            updateState { it.copy(qrData = storedItem) }
            updateTextContent(value = storedItem.displayName)
            observeEditableText()
        }
    }

    private fun updateTextContent(value: String) {

        updateState {
            it.copy(
                    resultEditableTextState = currentState.resultEditableTextState.copy(
                            textFieldState = buildTextFieldState(value)
                    )
            )
        }
    }

    private fun saveDisplayName(displayName: String) {

        if (displayName.isBlank()) return

        launchCatching(onError = {

            sendActionEvent(
                    actionEvent = ShowToastMessage(
                            R.string.toast_text_item_not_saved
                    )
            )
        }
        ) {
            val currentQrItem = currentState.qrData
            upsertHistoryUseCase(currentQrItem.copy(displayName = displayName))
        }
    }

    private fun toggleFavoriteStatus() {

        launchCatching(onError = {
            sendActionEvent(
                    actionEvent = ShowToastMessage(
                            R.string.toast_text_item_not_saved
                    )
            )
        }
        ) {

            updateState { it.copy(qrData = currentState.qrData.copy(favorite = !currentState.qrData.favorite)) }
            upsertHistoryUseCase(currentState.qrData)
        }
    }

    private fun buildTextFieldState(value: String): TextFieldState {
        return TextFieldState(initialText = value)
    }

    private fun isDisplayNameEdited(newDisplayName: String): Boolean {
        return this::oldDisplayName.isInitialized &&
                oldDisplayName != newDisplayName &&
                newDisplayName.isNotBlank()
    }
}
