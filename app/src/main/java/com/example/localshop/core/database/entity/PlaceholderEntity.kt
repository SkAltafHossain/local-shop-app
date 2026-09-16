package com.example.localshop.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "placeholder")
data class PlaceholderEntity(
    @PrimaryKey
    val id: String = "placeholder",
    val createdAt: Long = System.currentTimeMillis()
)
