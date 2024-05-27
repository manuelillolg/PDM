package com.example.fridgeapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ContainerSectionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    val idContainer: Long,
    val idSection: Long
)
