package com.tonyxlab.qrcraft.presentation.screens.result.handling

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Stable
import com.tonyxlab.qrcraft.domain.QrData
import com.tonyxlab.qrcraft.domain.QrDataType
import com.tonyxlab.qrcraft.presentation.core.base.handling.UiState
import com.tonyxlab.qrcraft.util.generateLoremIpsum

data class ResultUiState(
    val editableTextState: EditableTextState = EditableTextState(),
    val dataState: DataState = DataState()
) : UiState {
    @Stable
    data class DataState(
        val qrData: QrData = QrData(
                displayName = "Text",
                prettifiedData = generateLoremIpsum(26),
                qrDataType = QrDataType.TEXT,
                rawDataValue = ""
        )
    )

    @Stable
    data class EditableTextState(
        val isEditing: Boolean = false,
        val textFieldState: TextFieldState = TextFieldState( )
    )
}
