package com.tonyxlab.qrcraft.presentation.screens.history

import androidx.lifecycle.viewModelScope
import com.tonyxlab.qrcraft.domain.model.HistoryType
import com.tonyxlab.qrcraft.domain.usecase.DeleteHistoryByIdUseCase
import com.tonyxlab.qrcraft.domain.usecase.GetHistoryByIdUseCase
import com.tonyxlab.qrcraft.domain.usecase.GetHistoryUseCase
import com.tonyxlab.qrcraft.presentation.core.base.BaseViewModel
import com.tonyxlab.qrcraft.presentation.screens.history.handling.HistoryActionEvent
import com.tonyxlab.qrcraft.presentation.screens.history.handling.HistoryUiEvent
import com.tonyxlab.qrcraft.presentation.screens.history.handling.HistoryUiState
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn

typealias HistoryBaseViewModel = BaseViewModel<HistoryUiState, HistoryUiEvent, HistoryActionEvent>

class HistoryViewModel(
    private val getHistoryUseCase: GetHistoryUseCase,
    private val getHistoryByIdUseCase: GetHistoryByIdUseCase,
    private val deleteHistoryByIdUseCase: DeleteHistoryByIdUseCase

) : HistoryBaseViewModel() {

    override val initialState: HistoryUiState
        get() = HistoryUiState()

    init {

        observeHistory()
    }

    override fun onEvent(event: HistoryUiEvent) {

        when (event) {

            is HistoryUiEvent.LongPressHistoryItem -> {

               updateState { it.copy(showBottomHistoryBottomSheet = true) }

            }
            HistoryUiEvent.ShareHistoryItem -> {

                updateState { it.copy(showBottomHistoryBottomSheet = false) }
                sendActionEvent(actionEvent = HistoryActionEvent.OpenShareMenu)

            }

            HistoryUiEvent.DeleteHistoryItem -> {

                updateState { it.copy(showBottomHistoryBottomSheet = false) }
            }

            HistoryUiEvent.DismissHistoryBottomSheet -> {
                updateState { it.copy(showBottomHistoryBottomSheet = false) }
            }

            HistoryUiEvent.ExitHistoryScreen -> {
                sendActionEvent(actionEvent = HistoryActionEvent.ExitHistoryScreen)
            }

        }

    }

    @OptIn(FlowPreview::class)
    private fun observeHistory() {

        launch {

            val scannedHistoryFlow = getHistoryUseCase(HistoryType.SCANNED)
            val generatedHistoryFlow = getHistoryUseCase(HistoryType.GENERATED)

            combine(scannedHistoryFlow, generatedHistoryFlow) { scanned, generated ->

                updateState {
                    it.copy(
                            scannedHistoryList = scanned,
                            generatedHistoryList = generated
                    )
                }
            }
                    .debounce(5_00)
                    .distinctUntilChanged()
                    .launchIn(viewModelScope)

        }

    }
}

