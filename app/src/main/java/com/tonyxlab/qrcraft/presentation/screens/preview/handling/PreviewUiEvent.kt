package com.tonyxlab.qrcraft.presentation.screens.preview.handling

import com.tonyxlab.qrcraft.presentation.core.base.handling.UiEvent

sealed interface PreviewUiEvent: UiEvent{

    data object ExitPreviewScreen: PreviewUiEvent
}