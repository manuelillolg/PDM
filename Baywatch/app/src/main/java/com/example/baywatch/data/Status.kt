package com.example.baywatch.data
import com.google.firebase.Timestamp
data class Status(
    var Date: Timestamp = Timestamp.now(),
    var Dirtness: String = "",
    var Flag: String = "",
    var JellyFish: Boolean = false,
    var Temperature: Int = 0,
    var Wind: Int = 0,
    var id: String? = null
)
