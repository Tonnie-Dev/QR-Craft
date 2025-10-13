package com.tonyxlab.qrcraft.presentation.screens.entry.handling

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Stable
import androidx.compose.ui.text.input.KeyboardType
import com.tonyxlab.qrcraft.domain.QrDataType
import com.tonyxlab.qrcraft.presentation.core.base.handling.UiState

data class EntryUiState(
    val selectedQrType: QrDataType = QrDataType.TEXT,
    val formFields: List<FormFieldData> = listOf()
) : UiState {

    @Stable
    data class FormFieldData(
        val key:String = "",
        val textFieldState: TextFieldState = TextFieldState(),
        val placeHolder: String = "",
        val keyboardType: KeyboardType = KeyboardType.Text
    )

}


/*


    val textFieldsState: TextFieldsState = TextFieldsState(),
    val linkFieldsState: LinkFieldsState = LinkFieldsState(),
    val contactFieldsState: ContactFieldsState = ContactFieldsState(),
    val phoneNumberFieldsState: PhoneNumberFieldsState = PhoneNumberFieldsState(),
    val geoFieldsState: GeoFieldsState = GeoFieldsState(),
    val wifiFieldsState: WifiFieldsState = WifiFieldsState(),
 @Stable
    data class TextFieldsState(val textFieldState: TextFieldState = TextFieldState())

    @Stable
    data class LinkFieldsState(val urlFieldState: TextFieldState = TextFieldState())

    @Stable
    data class ContactFieldsState(
        val nameFieldState: TextFieldState = TextFieldState(),
        val emailFieldState: TextFieldState = TextFieldState(),
        val phoneFieldState: TextFieldState = TextFieldState()
    )

    @Stable
    data class PhoneNumberFieldsState(val phoneNumberFieldState: TextFieldState = TextFieldState())

    @Stable
    data class GeoFieldsState(
        val latFieldState: TextFieldState = TextFieldState(),
        val longFieldState: TextFieldState = TextFieldState(),
    )

    @Stable
    data class WifiFieldsState(
        val ssidFieldState: TextFieldState = TextFieldState(),
        val passwordFieldState: TextFieldState = TextFieldState(),
        val encryptionTypeFieldState: TextFieldState = TextFieldState()
    )*/
