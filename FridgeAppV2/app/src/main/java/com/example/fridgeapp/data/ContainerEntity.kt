package com.example.fridgeapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ContainerEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    val name: String,
)
