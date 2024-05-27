package com.example.fridgeapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Date

@Entity
data class SectionFoodEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    var idSection : Long,
    var idFood: Long,
    var quantity: Int,
    var expirationDate: Long? = null,
    var purchaseDate: Long,
    var comment: String,
    var location: String
)
