package com.example.baywatchworkeredition


import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext

import androidx.lifecycle.ViewModel

import com.example.baywatchworkeredition.data.Beach

import com.example.baywatchworkeredition.data.Record
import com.example.baywatchworkeredition.data.Status
import com.example.baywatchworkeredition.data.User

import com.example.baywatchworkeredition.model.ApplicationStateWorker
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
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


class AppViewModelWorker : ViewModel() {

    private lateinit var _appState : MutableStateFlow<ApplicationStateWorker>
    lateinit var appState: StateFlow<ApplicationStateWorker>
    lateinit var db :FirebaseFirestore
    lateinit var auth: FirebaseAuth
    lateinit var context: Context


    fun initModel(
        context: Context
    ){

        if(! ::_appState.isInitialized) {
            _appState = MutableStateFlow(
                ApplicationStateWorker()
            )
            //_appState.value.context =context
            appState = _appState.asStateFlow()
            db = Firebase.firestore
            auth = Firebase.auth

            getUserBeaches()
            this.context = context

        }

    }

    fun getUserBeaches(){
        var userBeaches: MutableList<Beach> = mutableListOf()

        db.collection("User").document(auth.uid!!).get()
            .addOnSuccessListener {user->
                if(user.exists()){
                    user.toObject<User>()!!.Beaches.forEach{ beachId->
                        db.collection("Beach").document(beachId).get()
                            .addOnSuccessListener{beach->
                                if(beach.exists()){
                                    var beachObject : Beach = beach.toObject<Beach>()!!
                                    beachObject.id = beachId
                                    userBeaches.add(beachObject)
                                    _appState.update {
                                        it.copy(
                                            allBeaches =userBeaches,
                                            errorAllBeaches = null
                                        )
                                    }
                                }
                            }
                            .addOnFailureListener{error->
                                _appState.update {
                                    it.copy(
                                        allBeaches = listOf(),
                                        errorAllBeaches = error.toString()
                                    )
                                }
                            }
                    }
                }
            }
            .addOnFailureListener {error->
                _appState.update {
                    it.copy(
                        allBeaches = listOf(),
                        errorAllBeaches = error.toString()
                    )
                }
            }
    }


    fun getStatusById(id: String){
        if(id.isNotBlank()){
            db.collection("Status").document(id).get()
                .addOnSuccessListener { status ->
                    if (status.exists()) {
                        var statusObject: Status = status.toObject<Status>()!!
                        statusObject.id = status.id
                        _appState.update {
                            it.copy(
                                currentStatus = statusObject
                            )
                        }
                    } else {
                        _appState.update {
                            it.copy(
                                currentStatus = null
                            )
                        }
                    }
                }
                .addOnFailureListener { error ->
                    _appState.update {
                        it.copy(
                            currentStatusError = error.toString()
                        )
                    }
                }
        }else{
            _appState.update {
                it.copy(
                    currentStatus = null
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

    fun saveNewStatus(status: Status) {

        var statusFB = mapOf(
            "Dirtness" to status.Dirtness,
            "Wind" to status.Wind,
            "Flag" to status.Flag,
            "Temperature" to status.Temperature,
            "JellyFish" to status.JellyFish,
            "Date" to Timestamp.now()

        )

        runBlocking{
            db.collection("Status").add(statusFB)
                .addOnSuccessListener { documentReference ->
                    var updatedBeach = mapOf(
                        "name" to _appState.value.beachSelected!!.name,
                        "location" to _appState.value.beachSelected!!.location,
                        "idRecord" to _appState.value.beachSelected!!.idRecord,
                        "idStatus" to documentReference.id

                    )
                    db.collection("Beach").document(_appState.value.beachSelected!!.id!!)
                        .update(updatedBeach)
                        .addOnSuccessListener {
                            db.collection("Record").document(_appState.value.beachSelected!!.idRecord).get()
                                .addOnSuccessListener { recordDocument->
                                    if(recordDocument.exists()){
                                        var recordObject = recordDocument.toObject<Record>()
                                        recordObject!!.StatusRecord.add(documentReference.id)

                                        db.collection("Record").document(_appState.value.beachSelected!!.idRecord)
                                            .update("StatusRecord",recordObject.StatusRecord)
                                            .addOnSuccessListener {
                                                _appState.update {
                                                    it.copy(
                                                        updatedStatusError = null
                                                    )
                                                }
                                                Toast.makeText(context,"Estado actualizado con éxito",Toast.LENGTH_SHORT).show()
                                            }
                                            .addOnFailureListener{error->
                                                _appState.update {
                                                    it.copy(
                                                        updatedStatusError = error.toString()
                                                    )
                                                }
                                                Toast.makeText(context,error.toString(),Toast.LENGTH_SHORT).show()
                                            }
                                    }
                                }
                                .addOnFailureListener {error->
                                    _appState.update {
                                        it.copy(
                                            updatedStatusError = error.toString()
                                        )
                                    }
                                    Toast.makeText(context,error.toString(),Toast.LENGTH_SHORT).show()
                                }
                        }
                        .addOnFailureListener { error ->
                            _appState.update {
                                it.copy(
                                    updatedStatusError = error.toString()
                                )
                            }
                            Toast.makeText(context,error.toString(),Toast.LENGTH_SHORT).show()
                        }
                }
                .addOnFailureListener { error ->
                    _appState.update {
                        it.copy(
                            updatedStatusError = error.toString()
                        )
                    }
                    Toast.makeText(context,error.toString(),Toast.LENGTH_SHORT).show()
                }
        }

    }


    fun getStatusRecord() {
        var today = Date()
        today.hours = 0
        today.minutes = 0
        var todayStatusRecord: MutableList<Status> = mutableListOf()


        db.collection("Record").document(_appState.value.beachSelected!!.idRecord).get()
            .addOnSuccessListener {record->
                if(record.exists()){

                    var statusList: List<String> = record.toObject<Record>()!!.StatusRecord

                    statusList.forEach{statusId ->
                        db.collection("Status").document(statusId).get()
                            .addOnSuccessListener {status->

                                if(status.exists()){

                                    var statusObject: Status = status.toObject<Status>()!!
                                    statusObject.id = status.id

                                    if(statusObject.Date.toDate()>today){
                                        todayStatusRecord.add(statusObject)
                                        todayStatusRecord.sortByDescending{  it.Date}
                                    }

                                    _appState.update {
                                        it.copy(
                                            statusRecord = todayStatusRecord,
                                            statusRecordError = null
                                        )
                                    }

                                }
                            }
                            .addOnFailureListener {error->
                                _appState.update {
                                    it.copy(
                                        statusRecordError = error.toString()
                                    )
                                }
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
            .addOnFailureListener {error->
                _appState.update {
                    it.copy(
                        statusRecordError = error.toString()
                    )
                }
            }
    }

}
