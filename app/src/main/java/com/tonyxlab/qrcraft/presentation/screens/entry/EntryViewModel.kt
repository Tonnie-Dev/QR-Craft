package com.tonyxlab.qrcraft.presentation.screens.entry

import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.tonyxlab.qrcraft.R
import com.tonyxlab.qrcraft.domain.model.HistoryType
import com.tonyxlab.qrcraft.domain.usecase.UpsertHistoryUseCase
import com.tonyxlab.qrcraft.navigation.Destinations
import com.tonyxlab.qrcraft.presentation.core.base.BaseViewModel
import com.tonyxlab.qrcraft.presentation.screens.entry.handling.EntryActionEvent
import com.tonyxlab.qrcraft.presentation.screens.entry.handling.EntryUiEvent
import com.tonyxlab.qrcraft.presentation.screens.entry.handling.EntryUiState
import com.tonyxlab.qrcraft.util.mapToQrData
import com.tonyxlab.qrcraft.util.toFormData
import com.tonyxlab.qrcraft.util.toMillis
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import java.time.LocalDateTime

typealias EntryBaseViewModel = BaseViewModel<EntryUiState, EntryUiEvent, EntryActionEvent>

class EntryViewModel(
    private val upsertHistoryUseCase: UpsertHistoryUseCase, savedStateHandle: SavedStateHandle
) : EntryBaseViewModel() {

    init {

        val navArgs = savedStateHandle.toRoute<Destinations.EntryScreenDestination>()
        val selectedQrType = navArgs.qrDataType

        updateState { it.copy(selectedQrType = selectedQrType) }
        observeFormData()
    }

    override val initialState: EntryUiState
        get() = EntryUiState()

    override fun onEvent(event: EntryUiEvent) {

        when (event) {
            EntryUiEvent.GenerateQrCode -> upsertHistoryItem()

            EntryUiEvent.ExitEntryScreen -> {
                sendActionEvent(EntryActionEvent.NavigateToCreateScreen)
            }
        }
    }

    @OptIn(FlowPreview::class)
    private fun observeFormData() {
        val fields = currentState.selectedQrType.toFormData()
        updateState { it.copy(formFields = fields) }

        launch {
            combine(
                    fields.map { field ->
                        snapshotFlow { field.textFieldState.text }.debounce(2_00)
                    }) { textValues ->

                textValues.all { it.isNotBlank() }

            }.collect { isValid ->

                updateState { it.copy(isValidForm = isValid) }
            }
        }
    }

    private fun readFormData(): Map<String, String> =
        currentState.formFields.associate { it.key to it.textFieldState.text.toString() }

    private fun upsertHistoryItem() {

        val now = LocalDateTime.now()
        val formData = readFormData()
        val qrData = mapToQrData(formData).copy(
                historyType = HistoryType.GENERATED,
                timestamp = now.toMillis()
        )

        launchCatching(
                onError = {
                    sendActionEvent(
                            actionEvent = EntryActionEvent.ShowToastMessage(
                                    R.string.snack_text_not_saved
                            )
                    )

                }
        ) {
            val id = upsertHistoryUseCase(qrData = qrData)
            sendActionEvent(actionEvent = EntryActionEvent.NavigateToPreviewScreen(id = id))
        }
    }
}