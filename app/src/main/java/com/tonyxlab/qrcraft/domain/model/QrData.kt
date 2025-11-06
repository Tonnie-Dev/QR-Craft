package com.tonyxlab.qrcraft.domain.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.tonyxlab.qrcraft.util.Constants
import kotlinx.serialization.Serializable

@Serializable
data class QrData(
    val id: Long = Constants.INITIAL_DATABASE_ID,
    val displayName: String,
    val prettifiedData: String,
    val qrDataType: QrDataType,
    val rawData: String,
    val historyType: HistoryType = HistoryType.SCANNED,
    val favorite: Boolean = false,
    val timestamp: Long = 0L,

)

data class QrUiType(
    @StringRes
    val label: Int,
    @DrawableRes
    val iconRes: Int,
    val tint: Color,
    val tintBg: Color
)

@Serializable
enum class QrDataType {
    TEXT, LINK, CONTACT, PHONE_NUMBER, GEOLOCATION, WIFI
}

enum class HistoryType {
    SCANNED, GENERATED
}

