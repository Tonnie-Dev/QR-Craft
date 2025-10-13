package com.tonyxlab.qrcraft.presentation.screens.entry.handling

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Stable
import androidx.compose.ui.text.input.KeyboardType
import com.tonyxlab.qrcraft.domain.QrDataType
import com.tonyxlab.qrcraft.presentation.core.base.handling.UiState

data class EntryUiState(
    val selectedQrType: QrDataType = QrDataType.TEXT,
    val formFields: List<FormFieldData> = listOf(),
    val isValidForm: Boolean = false
) : UiState {

    @Stable
    data class FormFieldData(
        val key: String = "",
        val textFieldState: TextFieldState = TextFieldState(),
        val placeHolder: String = "",
        val keyboardType: KeyboardType = KeyboardType.Text
    )

}


