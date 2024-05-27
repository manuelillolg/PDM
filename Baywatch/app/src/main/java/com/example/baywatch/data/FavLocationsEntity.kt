package com.example.baywatch.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FavLocationEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    val locationId : String
)