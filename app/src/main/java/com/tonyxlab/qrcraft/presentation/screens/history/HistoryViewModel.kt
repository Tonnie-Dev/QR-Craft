@file:OptIn(FlowPreview::class)

package com.tonyxlab.qrcraft.presentation.screens.history

import androidx.lifecycle.viewModelScope
import com.tonyxlab.qrcraft.R
import com.tonyxlab.qrcraft.domain.model.HistoryType
import com.tonyxlab.qrcraft.domain.usecase.DeleteHistoryByIdUseCase
import com.tonyxlab.qrcraft.domain.usecase.GetHistoryByIdUseCase
import com.tonyxlab.qrcraft.domain.usecase.GetHistoryUseCase
import com.tonyxlab.qrcraft.domain.usecase.UpsertHistoryUseCase
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
    private val upsertHistoryUseCase: UpsertHistoryUseCase,
    private val deleteHistoryByIdUseCase: DeleteHistoryByIdUseCase

) : HistoryBaseViewModel() {

    override val initialState: HistoryUiState
        get() = HistoryUiState()

    init {
        getHistoryItems()
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
                val text = currentState.selectedQrItem.data
                sendActionEvent(actionEvent = OpenShareMenu(text = text))
            }

            HistoryUiEvent.DeleteHistoryItem -> {
                deleteHistoryItem(id = currentState.selectedQrItem.id)
                updateState { it.copy(showBottomHistoryBottomSheet = false) }
            }

            is HistoryUiEvent.MarkFavorite -> toggleFavoriteStatus(id = event.id)

            HistoryUiEvent.DismissHistoryBottomSheet -> {
                updateState { it.copy(showBottomHistoryBottomSheet = false) }
            }

            HistoryUiEvent.ExitHistoryScreen -> {
                sendActionEvent(actionEvent = HistoryActionEvent.ExitHistoryScreen)
            }
        }
    }

    private fun getHistoryItems() {

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
                        selectedQrItem = currentState.selectedQrItem.copy(
                                id = qrData.id,
                                data = qrData.prettifiedData
                        )
                )
            }
        }
    }

    private fun toggleFavoriteStatus(id: Long) {
        launchCatching(onError = {
            sendActionEvent(
                    actionEvent = HistoryActionEvent.ShowToast(
                            messageRes = R.string.toast_text_item_not_found
                    )
            )
        }
        ) {
            val currentItem = getHistoryByIdUseCase(id = id)
            upsertHistoryUseCase(qrData = currentItem.copy(favorite = !currentItem.favorite))
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


