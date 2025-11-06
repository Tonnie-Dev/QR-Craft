package com.tonyxlab.qrcraft.presentation.screens.result.handling

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Stable
import com.tonyxlab.qrcraft.domain.model.QrData
import com.tonyxlab.qrcraft.domain.model.QrDataType
import com.tonyxlab.qrcraft.presentation.core.base.handling.UiState
import com.tonyxlab.qrcraft.util.generateLoremIpsum

@Stable
data class ResultUiState(
    val resultEditableTextState: ResultEditableTextState = ResultEditableTextState(),
    val qrData: QrData = QrData(
            id = -1L,
            displayName = "",
            prettifiedData = generateLoremIpsum(26),
            qrDataType = QrDataType.TEXT,
            rawData = generateLoremIpsum(26),
            favorite = false
    )

) : UiState {
    @Stable
    data class ResultEditableTextState(
        val isEditing: Boolean = false,
        val textFieldState: TextFieldState = TextFieldState()
    )
}
