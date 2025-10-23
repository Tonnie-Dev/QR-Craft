package com.tonyxlab.qrcraft.util

import androidx.compose.ui.text.input.KeyboardType
import com.tonyxlab.qrcraft.R
import com.tonyxlab.qrcraft.domain.model.QrData
import com.tonyxlab.qrcraft.domain.model.QrDataType
import com.tonyxlab.qrcraft.domain.model.QrUiType
import com.tonyxlab.qrcraft.presentation.screens.entry.handling.EntryUiState.FormFieldData
import com.tonyxlab.qrcraft.presentation.theme.ui.Contact
import com.tonyxlab.qrcraft.presentation.theme.ui.ContactBg
import com.tonyxlab.qrcraft.presentation.theme.ui.Geo
import com.tonyxlab.qrcraft.presentation.theme.ui.GeoBg
import com.tonyxlab.qrcraft.presentation.theme.ui.Link
import com.tonyxlab.qrcraft.presentation.theme.ui.LinkBg
import com.tonyxlab.qrcraft.presentation.theme.ui.Phone
import com.tonyxlab.qrcraft.presentation.theme.ui.PhoneBg
import com.tonyxlab.qrcraft.presentation.theme.ui.TextAccent
import com.tonyxlab.qrcraft.presentation.theme.ui.TextAccentBg
import com.tonyxlab.qrcraft.presentation.theme.ui.WiFi
import com.tonyxlab.qrcraft.presentation.theme.ui.WiFiBg

fun QrDataType.toTopBarString(): Int =
    when (this) {
        QrDataType.TEXT -> R.string.topbar_text_text
        QrDataType.LINK -> R.string.topbar_text_link
        QrDataType.CONTACT -> R.string.topbar_text_contact
        QrDataType.PHONE_NUMBER -> R.string.topbar_text_phone_number
        QrDataType.GEOLOCATION -> R.string.topbar_text_geo
        QrDataType.WIFI -> R.string.topbar_text_wi_fi
    }

fun QrDataType.toUi(): QrUiType {
    return when (this) {
        QrDataType.TEXT -> QrUiType(
                label = R.string.lab_text_text,
                iconRes = R.drawable.icon_text,
                tint = TextAccent,
                tintBg = TextAccentBg
        )

        QrDataType.LINK -> QrUiType(
                label = R.string.lab_text_link,
                iconRes = R.drawable.icon_link,
                tint = Link,
                tintBg = LinkBg
        )

        QrDataType.CONTACT -> QrUiType(
                label = R.string.lab_text_contact,
                iconRes = R.drawable.icon_contact,
                tint = Contact,
                tintBg = ContactBg
        )

        QrDataType.PHONE_NUMBER -> QrUiType(
                label = R.string.lab_text_phone,
                iconRes = R.drawable.icon_phone,
                tint = Phone,
                tintBg = PhoneBg
        )

        QrDataType.GEOLOCATION -> QrUiType(
                label = R.string.lab_text_geo,
                iconRes = R.drawable.icon_geo,
                tint = Geo,
                tintBg = GeoBg
        )

        QrDataType.WIFI -> QrUiType(
                label = R.string.lab_text_wifi,
                iconRes = R.drawable.icon_wifi,
                tint = WiFi,
                tintBg = WiFiBg
        )
    }
}
fun QrDataType.toFormData(): List<FormFieldData> = when (this) {

    QrDataType.TEXT -> listOf(
            FormFieldData(
                    key = "text",
                    placeHolder = "Text"
            )
    )

    QrDataType.LINK -> listOf(
            FormFieldData(
                    key = "link",
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

fun mapToQrData(values: Map<String, String>): QrData {

    val qrType = when {

        values.containsKey("email") && values.containsKey("phone") -> QrDataType.CONTACT
        values.containsKey("ssid") -> QrDataType.WIFI
        values.containsKey("link") -> QrDataType.LINK
        values.containsKey("phone_number") -> QrDataType.PHONE_NUMBER
        values.containsKey("lat") && values.containsKey("long") -> QrDataType.GEOLOCATION
        else -> QrDataType.TEXT
    }

    val prettifiedData = when (qrType) {
        QrDataType.CONTACT -> {

            val name = values["name"].orEmpty()
            val email = values["email"].orEmpty()
            val phone = values["phone"].orEmpty()
            "$name\n$email\n$phone"
        }

        QrDataType.WIFI -> {

            val ssid = values["ssid"].orEmpty()
            val password = values["password"].orEmpty()
            val encryption = values["encryption"].orEmpty()

            "WIFE:S:$ssid;T:$encryption;P:$password;;"
        }

        QrDataType.LINK -> values["link"].orEmpty()
        QrDataType.TEXT -> values["text"].orEmpty()
        QrDataType.GEOLOCATION -> {
            val lat = values["lat"].orEmpty()
            val long = values["long"].orEmpty()
            "geo:$lat,$long"
        }

        QrDataType.PHONE_NUMBER -> values["phone_number"].orEmpty()
    }

    val rawData = prettifiedData

    val displayName = when (qrType) {
        QrDataType.CONTACT -> "Contact"
        QrDataType.WIFI -> "Wi-Fi Network"
        QrDataType.LINK -> "Website"
        QrDataType.TEXT -> "Text"
        QrDataType.GEOLOCATION -> "Location"
        QrDataType.PHONE_NUMBER -> "Phone"
    }

    return QrData(
            displayName = displayName,
            prettifiedData = prettifiedData,
            rawData = rawData,
            qrDataType = qrType
    )
}
