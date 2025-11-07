package com.tonyxlab.qrcraft.utils

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

private val dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm")

fun Long.toFormattedDate(): String {

    val instant = Instant.ofEpochMilli(this)
    val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
    return dateFormatter.format(localDateTime)
}

fun LocalDateTime.toMillis(): Long {
    return this.atZone(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
}