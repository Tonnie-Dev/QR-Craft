package com.tonyxlab.qrcraft.presentation.screens.history

import androidx.lifecycle.viewModelScope
import com.tonyxlab.qrcraft.R
import com.tonyxlab.qrcraft.domain.model.HistoryType
import com.tonyxlab.qrcraft.domain.usecase.DeleteHistoryByIdUseCase
import com.tonyxlab.qrcraft.domain.usecase.GetHistoryByIdUseCase
import com.tonyxlab.qrcraft.domain.usecase.GetHistoryUseCase
import com.tonyxlab.qrcraft.presentation.core.base.BaseViewModel
import com.tonyxlab.qrcraft.presentation.screens.history.handling.HistoryActionEvent
import com.tonyxlab.qrcraft.presentation.screens.history.handling.HistoryActionEvent.OpenShareMenu
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
            is HistoryUiEvent.SelectHistoryItem -> {
                sendActionEvent(HistoryActionEvent.NavigateToPreview(event.id))
            }

            is HistoryUiEvent.LongPressHistoryItem -> {
                findClickedItemById(event.id)
                updateState { it.copy(showBottomHistoryBottomSheet = true) }
            }

            HistoryUiEvent.ShareHistoryItem -> {
                updateState { it.copy(showBottomHistoryBottomSheet = false) }
                val text = currentState.selectedItemState.data
                sendActionEvent(actionEvent = OpenShareMenu(text = text))
            }

            HistoryUiEvent.DeleteHistoryItem -> {
                deleteHistoryItem(id = currentState.selectedItemState.id)
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

    private fun findClickedItemById(id: Long) {
        launchCatching(
                onError = {
                    sendActionEvent(
                            actionEvent = HistoryActionEvent.ShowToast(
                                    messageRes = R.string.toast_text_item_not_found
                            )
                    )
                }
        ) {
            val qrData = getHistoryByIdUseCase(id = id)
            updateState {
                it.copy(
                        selectedItemState = currentState.selectedItemState.copy(
                                id = qrData.id,
                                data = qrData.prettifiedData
                        )
                )
            }
        }
    }

    private fun deleteHistoryItem(id: Long) {

        launchCatching(
                onError = {

                    sendActionEvent(
                            actionEvent = HistoryActionEvent.ShowToast(
                                    messageRes = R.string.toast_text_item_not_found
                            )
                    )
                }
        ) {
            deleteHistoryByIdUseCase(id = id)
        }
    }
}


