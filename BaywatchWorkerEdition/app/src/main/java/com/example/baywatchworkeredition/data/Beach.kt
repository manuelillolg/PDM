package com.example.baywatchworkeredition.data

import com.google.firebase.firestore.GeoPoint
data class Beach(
    var id: String? = null,
    val idRecord: String = "",
    val idStatus: String = "",
    val location: GeoPoint? = null,
    val name : String = ""
)
