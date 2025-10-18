package com.tonyxlab.qrcraft.data.local.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tonyxlab.qrcraft.domain.QrDataType

@Entity(tableName = "history_table")
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0L,
    @ColumnInfo(name = "qr_type")
    val qrType: QrDataType,
    @ColumnInfo(name = "prettified_data")
    val prettifiedData: String,
    @ColumnInfo(name = "timestamp")
    val timestamp: Long
)
