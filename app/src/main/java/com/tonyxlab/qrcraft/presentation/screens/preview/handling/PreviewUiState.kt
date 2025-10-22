package com.tonyxlab.qrcraft.presentation.screens.preview.handling

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Stable
import com.tonyxlab.qrcraft.domain.model.QrData
import com.tonyxlab.qrcraft.domain.model.QrDataType
import com.tonyxlab.qrcraft.presentation.core.base.handling.UiState
import com.tonyxlab.qrcraft.util.generateLoremIpsum

data class PreviewUiState(
    val valuesMap: Map<String, String> = emptyMap(),
    val qrDataState: QrDataState = QrDataState(),
    val previewEditableTextState: PreviewEditableTextState = PreviewEditableTextState()
) : UiState {

    @Stable
    data class QrDataState(
        val qrData: QrData = QrData(
                displayName = "Text",
                prettifiedData = generateLoremIpsum(26),
                qrDataType = QrDataType.TEXT,
                rawDataValue = ""
        )
    )

    @Stable
    data class PreviewEditableTextState(
        val isEditing: Boolean = false,
        val textFieldState: TextFieldState = TextFieldState()
    )
}