package com.tonyxlab.qrcraft.domain

import kotlinx.serialization.Serializable

@Serializable
enum class QrDataType {

    TEXT, LINK, CONTACT, PHONE_NUMBER, GEOLOCATION, WIFI
}

@Serializable
data class QrData(
    val displayName: String,
    val data: String,
    val qrDataType: QrDataType
)
