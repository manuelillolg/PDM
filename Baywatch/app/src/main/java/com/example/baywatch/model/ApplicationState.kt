package com.example.baywatch.model

import com.example.baywatch.data.Beach
import com.example.baywatch.data.Status

data class ApplicationState(
    var favLocations : List<String> = listOf(),

    //Variables to db request
    var allBeaches : List<Beach> = listOf(),
    var errorAllBeaches : String? = null,

    //Variables to beach detail
    var beachCurrentStatus: Status? = null,
    var beachcurrentStatusError: String? = null,
    var beachSelected: Beach? = null,
    var isFavBeach: Boolean = false,

    //Variables to beach status record
    var statusRecordError : String? = null,
    var statusRecord : List<Status>? = null

)
