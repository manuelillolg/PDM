package com.example.baywatchworkeredition.model

import com.example.baywatchworkeredition.data.Beach
import com.example.baywatchworkeredition.data.Status

data class ApplicationStateWorker(
    var favLocations : List<String> = listOf(),

    //Variables to db request
    var allBeaches : List<Beach> = listOf(),
    var errorAllBeaches : String? = null,

    //Variables to updateStatus
    var beachSelected: Beach? = null,
    var currentStatus: Status? = null,
    var currentStatusError: String? = null,
    var updatedStatusError: String? = null,

    //Variables to beach status record
    var statusRecordError : String? = null,
    var statusRecord : List<Status>? = null

)
