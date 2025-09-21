package com.tonyxlab.qrcraft.domain

enum class QrDataType {

    TEXT, LINK, CONTACT, PHONE_NUMBER, GEOLOCATION, WIFI
}

data class QrData(
    val displayName: String,
    val data: String,
    val qrDataType: QrDataType
)
