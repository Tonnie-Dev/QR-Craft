package com.tonyxlab.qrcraft.presentation.screens.preview.handling

import com.tonyxlab.qrcraft.presentation.core.base.handling.UiEvent
import com.tonyxlab.qrcraft.presentation.screens.result.handling.ResultUiEvent

sealed interface PreviewUiEvent: UiEvent{
    data object EditDetectedContent: PreviewUiEvent
    data object ShareContent: PreviewUiEvent
    data object CopyContent: PreviewUiEvent
    data object ExitPreviewScreen: PreviewUiEvent
    data object MarkFavorite : PreviewUiEvent
}