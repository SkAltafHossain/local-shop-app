package com.example.localshop.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.localshop.core.database.entity.PlaceholderEntity

@Database(
    entities = [PlaceholderEntity::class],
    version = 1
)
abstract class LocalShopDatabase : RoomDatabase() {
    // DAOs will be added here as needed
}