package com.tonyxlab.qrcraft.util

import androidx.compose.ui.text.input.KeyboardType
import com.tonyxlab.qrcraft.R
import com.tonyxlab.qrcraft.domain.QrDataType
import com.tonyxlab.qrcraft.presentation.screens.entry.handling.EntryUiState.FormFieldData

fun QrDataType.toTopBarString(): Int =
    when (this) {
        QrDataType.TEXT -> R.string.topbar_text_text
        QrDataType.LINK -> R.string.topbar_text_link
        QrDataType.CONTACT -> R.string.topbar_text_contact
        QrDataType.PHONE_NUMBER -> R.string.topbar_text_phone_number
        QrDataType.GEOLOCATION -> R.string.topbar_text_geo
        QrDataType.WIFI -> R.string.topbar_text_wi_fi
    }

fun QrDataType.toFormData(): List<FormFieldData> = when (this) {

    QrDataType.TEXT -> listOf(
            FormFieldData(
                    key = "Text",
                    placeHolder = "Text"
            )
    )

    QrDataType.LINK -> listOf(
            FormFieldData(
                    key = "Link",
                    placeHolder = "Link",
                    keyboardType = KeyboardType.Uri
            )
    )

    QrDataType.CONTACT -> listOf(

            FormFieldData(
                    key = "name",
                    placeHolder = "name",

                    ),
            FormFieldData(
                    key = "email",
                    placeHolder = "Email",
                    keyboardType = KeyboardType.Email
            ),

            FormFieldData(
                    key = "phone",
                    placeHolder = "Phone",
                    keyboardType = KeyboardType.Phone
            )

    )

    QrDataType.PHONE_NUMBER -> listOf(

            FormFieldData(
                    key = "phone_number",
                    placeHolder = "Phone Number",
                    keyboardType = KeyboardType.Phone
            )

    )

    QrDataType.GEOLOCATION -> listOf(
            FormFieldData(
                    key = "lat",
                    placeHolder = "Latitude",
                    keyboardType = KeyboardType.Decimal
            ),

            FormFieldData(
                    key = "long",
                    placeHolder = "Longitude",
                    keyboardType = KeyboardType.Decimal
            )

    )

    QrDataType.WIFI -> listOf(

            FormFieldData(
                    key = "ssid",
                    placeHolder = "SSID",
            ),

            FormFieldData(
                    key = "password",
                    placeHolder = "Password",
            ),

            FormFieldData(
                    key = "encryption",
                    placeHolder = "Encryption",

                    ),

            )
}

/*
data class FormFieldData(
    val key: String,
    val value: String,
    val keyboardType: KeyboardType = KeyboardType.Text
)

fun QrDataType.toFormData(): List<FormFieldData> = when (this) {

    QrDataType.TEXT -> listOf(
            FormFieldData(key = "text", value = "")
    )

    QrDataType.LINK -> listOf(
            FormFieldData(key = "url", value = "", keyboardType = KeyboardType.Uri)
    )

    QrDataType.CONTACT -> listOf(
            FormFieldData(key = "name", value = ""),
            FormFieldData(key = "email", value = "", keyboardType = KeyboardType.Email),
            FormFieldData(key = "phone", value = "", keyboardType = KeyboardType.Phone)
    )

    QrDataType.PHONE_NUMBER -> listOf(
            FormFieldData(key = "phone_number", value = "", keyboardType = KeyboardType.Phone)
    )

    QrDataType.GEOLOCATION -> listOf(

            FormFieldData(key = "lat", value = "", keyboardType = KeyboardType.Decimal),
            FormFieldData(key = "long", value = "", keyboardType = KeyboardType.Decimal)
    )

    QrDataType.WIFI -> listOf(
            FormFieldData(key = "ssid", value = ""),
            FormFieldData(key = "password", value = ""),
            FormFieldData(key = "encryption", value = "")
    )
}*/
