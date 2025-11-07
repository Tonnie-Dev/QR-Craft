package com.tonyxlab.qrcraft.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [HistoryEntity::class], version = 1, exportSchema = false)
abstract class QrCraftDatabase : RoomDatabase(){
    abstract val dao: HistoryDao
}
