package com.tonyxlab.qrcraft.presentation.screens.entry

import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.tonyxlab.qrcraft.domain.QrDataType
import com.tonyxlab.qrcraft.navigation.Destinations
import com.tonyxlab.qrcraft.presentation.core.base.BaseViewModel
import com.tonyxlab.qrcraft.presentation.screens.entry.handling.EntryActionEvent
import com.tonyxlab.qrcraft.presentation.screens.entry.handling.EntryUiEvent
import com.tonyxlab.qrcraft.presentation.screens.entry.handling.EntryUiState
import com.tonyxlab.qrcraft.util.toFormData
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce

typealias EntryBaseViewModel = BaseViewModel<EntryUiState, EntryUiEvent, EntryActionEvent>

class EntryViewModel(savedStateHandle: SavedStateHandle) : EntryBaseViewModel() {

    init {
        val navArgs =
            savedStateHandle.toRoute<Destinations.EntryScreenDestination>()
        val selectedQrType = navArgs.qrDataType
        updateState { it.copy(selectedQrType = selectedQrType) }

        observeFormData()

    }

    override val initialState: EntryUiState
        get() = EntryUiState()

    override fun onEvent(event: EntryUiEvent) {

        when (event) {
            EntryUiEvent.ExitEntryScreen -> {
                sendActionEvent(EntryActionEvent.NavigateToCreateScreen)
            }

            EntryUiEvent.GenerateQrCode -> {

                val values = readFormData()

                when(currentState.selectedQrType){
                    QrDataType.TEXT -> {
                        val text = values["text"].orEmpty()
                    }
                    QrDataType.LINK -> TODO()
                    QrDataType.CONTACT -> TODO()
                    QrDataType.PHONE_NUMBER -> TODO()
                    QrDataType.GEOLOCATION -> TODO()
                    QrDataType.WIFI -> TODO()
                }
                sendActionEvent(EntryActionEvent.NavigateToPreviewScreen)
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
                        snapshotFlow { field.textFieldState.text }
                                .debounce(2_00)
                    }
            ) { textValues ->

                textValues.all { it.isNotBlank() }

            }.collect { isValid ->

                updateState { it.copy(isValidForm = isValid) }
            }
        }
    }

    private fun readFormData(): Map<String, String> {
        return currentState.formFields.associate { it.key to it.textFieldState.text.toString() }
    }
}