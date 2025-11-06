package com.tonyxlab.qrcraft.data.local.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tonyxlab.qrcraft.domain.model.HistoryType
import com.tonyxlab.qrcraft.domain.model.QrDataType

@Entity(tableName = "history_table")
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0L,
    @ColumnInfo(name = "display_name")
    val displayName: String,
    @ColumnInfo(name = "qr_type")
    val qrDataType: QrDataType,
    @ColumnInfo(name = "history_type")
    val historyType: HistoryType,
    @ColumnInfo(name = "prettified_data")
    val prettifiedData: String,
    @ColumnInfo(name = "raw_data")
    val rawData: String,
    @ColumnInfo(name = "favorite")
    val favorite: Boolean,
    @ColumnInfo(name = "timestamp")
    val timestamp: Long
)
