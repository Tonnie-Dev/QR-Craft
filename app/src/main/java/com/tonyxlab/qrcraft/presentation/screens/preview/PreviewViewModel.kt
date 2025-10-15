package com.tonyxlab.qrcraft.presentation.screens.preview

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.tonyxlab.qrcraft.navigation.Destinations
import com.tonyxlab.qrcraft.presentation.core.base.BaseViewModel
import com.tonyxlab.qrcraft.presentation.screens.preview.handling.PreviewActionEvent
import com.tonyxlab.qrcraft.presentation.screens.preview.handling.PreviewUiEvent
import com.tonyxlab.qrcraft.presentation.screens.preview.handling.PreviewUiState
import com.tonyxlab.qrcraft.presentation.screens.result.handling.ResultUiState
import com.tonyxlab.qrcraft.util.mapToQrData
import kotlinx.serialization.json.Json

typealias PreviewBaseViewModel = BaseViewModel<PreviewUiState, PreviewUiEvent, PreviewActionEvent>

class PreviewViewModel(savedStateHandle: SavedStateHandle) : PreviewBaseViewModel() {

    init {

        val navArgs = savedStateHandle.toRoute<Destinations.PreviewScreenDestination>()
        val jsonMapString = navArgs.jsonMapString

        val valuesMap = Json.decodeFromString<Map<String, String>>(jsonMapString)

        val qrData = mapToQrData(valuesMap)
        updateState { it.copy(valuesMap = valuesMap, qrDataState = PreviewUiState.QrDataState(qrData = qrData)) }

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
                sendActionEvent(PreviewActionEvent.ShareText(text = text))

            }

            PreviewUiEvent.CopyContent -> {
                val text = currentState.qrDataState.qrData.prettifiedData
                sendActionEvent(PreviewActionEvent.CopyText(text = text))

            }
        }
    }
}