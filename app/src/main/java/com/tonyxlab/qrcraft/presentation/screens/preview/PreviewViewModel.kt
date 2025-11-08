@file:OptIn(FlowPreview::class)

package com.tonyxlab.qrcraft.presentation.screens.preview

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
    private var isTogglingFavorite = false
    private var lastSavedImageUri: Uri? = null

    override val initialState: PreviewUiState
        get() = PreviewUiState()

    init {
        val navArgs =
            savedStateHandle.toRoute<Destinations.PreviewScreenDestination>()
        val id = navArgs.id
        loadQrDataItem(id)
    }

    override fun onEvent(event: PreviewUiEvent) {

        when (event) {
            PreviewUiEvent.ShareContent -> {
                sendActionEvent(ShareText(text = currentState.qrData.prettifiedData))
            }

            PreviewUiEvent.CopyContent -> {
                sendActionEvent(CopyText(text = currentState.qrData.prettifiedData))
            }

            PreviewUiEvent.EditDetectedContent -> {
                updateState {
                    it.copy(
                            previewEditableTextState =
                                it.previewEditableTextState.copy(isEditing = true)
                    )
                }
            }

            PreviewUiEvent.MarkFavorite -> toggleFavoriteStatus()

            PreviewUiEvent.SaveQrPhoto -> {
                sendActionEvent(actionEvent = PreviewActionEvent.SaveImage(currentState.qrData))
            }

            PreviewUiEvent.ExitPreviewScreen -> sendActionEvent(
                    PreviewActionEvent.NavigateToEntryScreen
            )

            PreviewUiEvent.OpenSavedImageLocation -> {
                lastSavedImageUri?.let { uri ->
                    sendActionEvent(
                            PreviewActionEvent.OpenImageLocation(
                                    uri = uri
                            )
                    )
                } ?: sendActionEvent(
                        PreviewActionEvent.ShowToast(R.string.toast_text_item_not_found)
                )
            }
        }
    }

    private fun loadQrDataItem(id: Long) {
        launchCatching(
                onError = {
                    sendActionEvent(
                            PreviewActionEvent.ShowToast(R.string.toast_text_item_not_found)
                    )
                }
        ) {
            val storedItem = getHistoryByIdUseCase(id)
            oldDisplayName = storedItem.displayName
            updateState { it.copy(qrData = storedItem) }
            updateTextFieldContent(storedItem.displayName)
            observeEditableText()
        }
    }

    private fun observeEditableText() {
        val textFlow = snapshotFlow {
            currentState.previewEditableTextState.textFieldState.text
        }
        textFlow
                .debounce(300)
                .distinctUntilChanged()
                .onEach { displayName ->
                    val newDisplayName =
                        if (displayName.isBlank()) oldDisplayName
                        else displayName.toString()
                    if (isDisplayNameEdited(newDisplayName = newDisplayName))
                        saveDisplayName(displayName = newDisplayName)
                }
                .launchIn(viewModelScope)
    }

    private fun updateTextFieldContent(value: String) {
        updateState {
            it.copy(
                    previewEditableTextState = it.previewEditableTextState.copy(
                            textFieldState = buildTextFieldState(value)
                    )
            )
        }
    }

    private fun saveDisplayName(displayName: String) {

        if (displayName.isBlank()) return
        launchCatching(
                onError = {
                    sendActionEvent(
                            PreviewActionEvent.ShowToast(R.string.toast_text_item_not_saved)
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
                    actionEvent = PreviewActionEvent.ShowToast(
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
