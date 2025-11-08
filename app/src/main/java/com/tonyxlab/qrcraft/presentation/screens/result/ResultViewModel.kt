@file:OptIn(FlowPreview::class)

package com.tonyxlab.qrcraft.presentation.screens.result

import android.net.Uri
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
import com.tonyxlab.qrcraft.presentation.screens.result.handling.ResultActionEvent.OpenImageLocation
import com.tonyxlab.qrcraft.presentation.screens.result.handling.ResultActionEvent.SaveQrImage
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

class ResultViewModel(
    private val getHistoryByIdUseCase: GetHistoryByIdUseCase,
    private val upsertHistoryUseCase: UpsertHistoryUseCase,
    savedStateHandle: SavedStateHandle
) : BaseResultViewModel() {

    private lateinit var oldDisplayName: String
    private var isTogglingFavorite = false
    private var lastSavedImageUri: Uri? = null

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

            is ResultUiEvent.SaveQrPhoto -> {
                sendActionEvent(SaveQrImage(currentState.qrData))
            }

            ResultUiEvent.OpenImageLocation -> {

                lastSavedImageUri?.let { uri ->
                    sendActionEvent(actionEvent = OpenImageLocation(uri = uri))
                }
                    ?: sendActionEvent(
                            actionEvent = ShowToastMessage(
                                    messageRes = R.string.toast_text_item_not_found
                            )
                    )
            }

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
                        if (displayName.isBlank()) oldDisplayName else displayName.toString()
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
            updateState { it.copy(qrData = currentQrItem.copy(displayName = displayName)) }
            upsertHistoryUseCase(currentQrItem.copy(displayName = displayName))

        }
    }

    private fun toggleFavoriteStatus() {

        if (isTogglingFavorite) return
        isTogglingFavorite = true

        val previousQrData = currentState.qrData
        val updatedQrData = previousQrData.copy(favorite = !previousQrData.favorite)

        updateState { it.copy(qrData = updatedQrData) }
        launchCatching(onError = {
            updateState { it.copy(qrData = previousQrData) }
            sendActionEvent(
                    actionEvent = ShowToastMessage(
                            R.string.toast_text_item_not_saved
                    )
            )
            isTogglingFavorite = false
        }
        ) {
            upsertHistoryUseCase(updatedQrData)
            isTogglingFavorite = false
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

    fun setSavedImageUri(uri: Uri?) {
        lastSavedImageUri = uri
    }
}
