package com.tonyxlab.qrcraft.domain

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import kotlinx.serialization.Serializable

@Serializable
enum class QrDataType {

    TEXT, LINK, CONTACT, PHONE_NUMBER, GEOLOCATION, WIFI
}

@Serializable
data class QrData(
    val displayName: String,
    val prettifiedData: String,
    val rawDataValue: String,
    val qrDataType: QrDataType,
)


data class QrUiType(
    @StringRes
    val label: Int,
    @DrawableRes
    val iconRes: Int,
    val tint: Color,
    val tintBg: Color
)

