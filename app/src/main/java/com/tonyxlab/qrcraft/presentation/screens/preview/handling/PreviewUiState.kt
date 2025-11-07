package com.tonyxlab.qrcraft.presentation.screens.preview.handling

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Stable
import com.tonyxlab.qrcraft.domain.model.QrData
import com.tonyxlab.qrcraft.domain.model.QrDataType
import com.tonyxlab.qrcraft.presentation.core.base.handling.UiState
import com.tonyxlab.qrcraft.utils.generateLoremIpsum

@Stable
data class PreviewUiState(
    val qrData: QrData = QrData(
            id = -1L,
            displayName = "",
            prettifiedData = generateLoremIpsum(26),
            qrDataType = QrDataType.TEXT,
            rawData = generateLoremIpsum(52),
            favorite = false
    ),
    val previewEditableTextState: PreviewEditableTextState = PreviewEditableTextState()
) : UiState

@Stable
data class PreviewEditableTextState(
    val isEditing: Boolean = false,
    val textFieldState: TextFieldState = TextFieldState("")
)
