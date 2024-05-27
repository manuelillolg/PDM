package com.example.baywatch


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.room.util.copy
import com.example.baywatch.data.Beach
import com.example.baywatch.data.FavLocationEntity
import com.example.baywatch.data.FavLocationRepository
import com.example.baywatch.data.Record
import com.example.baywatch.data.Status
import com.example.baywatch.model.ApplicationState
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.runBlocking
import java.util.Date


class AppViewModel (): ViewModel() {

    private lateinit var _appState : MutableStateFlow<ApplicationState>
    lateinit var appState: StateFlow<ApplicationState>
    lateinit var db :FirebaseFirestore
    private  lateinit var favLocationRepository: FavLocationRepository

    fun initModel(
        favLocationRepository: FavLocationRepository
    ){

        if(! ::_appState.isInitialized) {
            _appState = MutableStateFlow(
                ApplicationState()
            )
            //_appState.value.context =context
            appState = _appState.asStateFlow()
            db = Firebase.firestore
            this.favLocationRepository = favLocationRepository

            getAllBeaches()

        }

    }

    fun getAllBeaches(){
        var allBeaches : MutableList<Beach> = mutableListOf()

        db.collection("Beach").get()
            .addOnSuccessListener { beaches ->
                Log.d("TAGGGGG","funciona")


                for (beach in beaches) {
                    var beach2 : Beach= beach.toObject<Beach>()
                    beach2.id = beach.id
                    allBeaches.add(beach2)
                }
                _appState.update {
                    it.copy(
                        allBeaches = allBeaches,
                        errorAllBeaches = null

                    )
                }
            }
            .addOnFailureListener { error ->
                Log.d("TAGGGGG","no funciona")
                _appState.update {
                    it.copy(
                        errorAllBeaches = error.toString()
                    )
                }
            }

    }

    fun updateStatus(status : Status?){
        _appState.update {
            it.copy(
                beachCurrentStatus = null
            )
        }
    }

    fun getStatusById(id: String){
        Log.d("TAGGGGGGG2", id)
        db.collection("Status").document(id).get()
            .addOnSuccessListener {status->
                Log.d("TAGGGGGGG2", status.toString())
                if(status.exists()){
                    var statusObject:Status = status.toObject<Status>()!!
                    statusObject.id = status.id
                    _appState.update {
                        it.copy(
                            beachCurrentStatus =statusObject
                        )
                    }
                }else{
                    _appState.update {
                        it.copy(
                            beachCurrentStatus = null
                        )
                    }
                }
            }
            .addOnFailureListener {error->
                _appState.update {
                    it.copy(
                        beachcurrentStatusError =error.toString()
                    )
                }
            }
    }

    fun updateBeachSelected(beach: Beach){
        _appState.update {
            it.copy(
                beachSelected = beach
            )
        }
    }

    fun existsLocationId(id: String): Boolean{
        var locationList : List<String>

        runBlocking {
           locationList = favLocationRepository.getLocation(id)
        }

        var fav = locationList.isNotEmpty()

        _appState.update {
            it.copy(
                isFavBeach = fav
            )
        }
        return fav
    }

    fun updateFavLocation(id: String){
        if(existsLocationId(id)){
            runBlocking{ favLocationRepository.deleteFavLocation(id) }
        }else{
            runBlocking{ favLocationRepository.insertFavLocation(FavLocationEntity(locationId = id)) }
        }
        existsLocationId(id)
    }

    fun getAllFavBeaches(){
        var favBeachIds : List<String>

        runBlocking {
            favBeachIds = favLocationRepository.getAllFavLocation()
        }

        var allFavBeaches : MutableList<Beach> = mutableListOf()

        db.collection("Beach").get()
            .addOnSuccessListener { beaches ->


                for (beach in beaches) {
                    if(favBeachIds.contains(beach.id)){
                        var beach2: Beach = beach.toObject<Beach>()
                        beach2.id = beach.id
                        allFavBeaches.add(beach2)
                    }
                }
                _appState.update {
                    it.copy(
                        allBeaches = allFavBeaches,
                        errorAllBeaches = null

                    )
                }
            }
            .addOnFailureListener { error ->
                _appState.update {
                    it.copy(
                        errorAllBeaches = error.toString()
                    )
                }
            }
    }

    fun getStatusRecord() {
        var today = Date()
        today.hours = 0
        today.minutes = 0
        var todayStatusRecord: MutableList<Status> = mutableListOf()


        if(_appState.value.beachSelected!!.idRecord.isNotBlank()){
            db.collection("Record").document(_appState.value.beachSelected!!.idRecord).get()
                .addOnSuccessListener { record ->
                    if (record.exists()) {

                        var statusList: List<String> = record.toObject<Record>()!!.StatusRecord

                        statusList.forEach { statusId ->
                            Log.d("TAGGGGGGG",statusList.toString())
                            db.collection("Status").document(statusId).get()
                                .addOnSuccessListener { status ->

                                    if (status.exists()) {

                                        var statusObject: Status = status.toObject<Status>()!!
                                        statusObject.id = status.id

                                        if (statusObject.Date.toDate() > today) {
                                            todayStatusRecord.add(statusObject)
                                            todayStatusRecord.sortByDescending { it.Date }
                                        }

                                        _appState.update {
                                            it.copy(
                                                statusRecord = todayStatusRecord,
                                                statusRecordError = null
                                            )
                                        }

                                    }
                                }
                                .addOnFailureListener { error ->
                                    _appState.update {
                                        it.copy(
                                            statusRecordError = error.toString()
                                        )
                                    }
                                }
                        }


                    } else {
                        _appState.update {
                            it.copy(
                                statusRecord = null
                            )
                        }
                    }
                }
                .addOnFailureListener { error ->
                    _appState.update {
                        it.copy(
                            statusRecordError = error.toString()
                        )
                    }
                }
        }else{
            _appState.update {
                it.copy(
                    statusRecord = null
                )
            }
        }
    }

}
