package com.example.fridgeapp.data

import java.util.Date

data class FoodV2(
    var food : FoodEntity? = null,
    var quantity: String = "0",
    var expirationDate: Long? = null,
    var purchaseDate: Long? = Date().time,
    var comment: String = "",
    var location: String= ""
)
