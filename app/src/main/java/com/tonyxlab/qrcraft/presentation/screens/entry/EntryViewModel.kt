package com.tonyxlab.qrcraft.presentation.screens.entry

import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.tonyxlab.qrcraft.navigation.Destinations
import com.tonyxlab.qrcraft.presentation.core.base.BaseViewModel
import com.tonyxlab.qrcraft.presentation.screens.entry.handling.EntryActionEvent
import com.tonyxlab.qrcraft.presentation.screens.entry.handling.EntryUiEvent
import com.tonyxlab.qrcraft.presentation.screens.entry.handling.EntryUiState
import com.tonyxlab.qrcraft.util.toFormData
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import timber.log.Timber

typealias EntryBaseViewModel = BaseViewModel<EntryUiState, EntryUiEvent, EntryActionEvent>

class EntryViewModel(savedStateHandle: SavedStateHandle) : EntryBaseViewModel() {

    init {
        val navArgs =
            savedStateHandle.toRoute<Destinations.EntryScreenDestination>()
        val selectedQrType = navArgs.qrDataType
        updateState { it.copy(selectedQrType = selectedQrType) }

        readFields()

    }

    override val initialState: EntryUiState
        get() = EntryUiState()

    override fun onEvent(event: EntryUiEvent) {

        when(event){
            EntryUiEvent.ExitEntryScreen ->{
                sendActionEvent(EntryActionEvent.NavigateToCreateScreen)
            }
            EntryUiEvent.GenerateQrCode -> {
                sendActionEvent(EntryActionEvent.NavigateToPreviewScreen)
            }
        }
    }

    @OptIn(FlowPreview::class)
    private fun readFields() {



            val fields = currentState.selectedQrType.toFormData()

            updateState { it.copy(formFields = fields) }

            fields.forEach {  field ->

                launch {


                    snapshotFlow { field.textFieldState.text   }
                            .debounce(300)
                            .collect { newText ->
                                Timber.tag("EntryViewModel").i("Field ${field.key} changed to $newText")
                            }
                }

            }


    }
}