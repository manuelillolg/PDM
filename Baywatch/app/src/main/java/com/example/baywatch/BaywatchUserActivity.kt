package com.example.baywatch

import android.Manifest
import android.content.pm.PackageManager
import android.health.connect.datatypes.ExerciseRoute
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.example.baywatch.data.DataBase
import com.example.baywatch.data.FavLocationRepository
import com.example.baywatch.ui.theme.BaywatchTheme
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Granularity
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority


class BaywatchUserActivity : ComponentActivity()  {

    private val CODIGO_PERMISO_SEGUNDO_PLANO = 100
    private var isPermisos = false
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var favLocationRepository: FavLocationRepository
    var lat : Double? = null
    var long: Double?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dbRoom = Room.databaseBuilder(this, DataBase::class.java, "BaywatchDB").build()
        val favLocationEntityDao = dbRoom.favLocationDao
        favLocationRepository = FavLocationRepository(favLocationEntityDao)

        checkPermission()
        enableEdgeToEdge()

    }

    private fun checkPermission(){
        val permissions = arrayListOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION
        )

        val permissonsArray = permissions.toTypedArray()

        if(hasPermissions(permissonsArray)){
            isPermisos = true
            onPermissionsGranted()


        }else{
            requestPermissions(permissonsArray)
        }

    }

    private fun hasPermissions(permissions: Array<String>): Boolean{
        return permissions.all{
            return ContextCompat.checkSelfPermission(
                this,
                it
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun onPermissionsGranted(){
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        try {
            fusedLocationClient.lastLocation.addOnSuccessListener {
                if(it != null ){
                    lat = it.latitude
                    long = it.longitude
                    setContent {
                        BaywatchTheme {
                            BaywatchUser(
                                favLocationRepository = favLocationRepository,
                                latitude  =lat,
                                longitude = long
                            )
                        }
                    }
                }else{
                    Toast.makeText(this,"No se puede obtener la ubicación",Toast.LENGTH_SHORT).show()
                    setContent {
                        BaywatchTheme {
                            BaywatchUser(
                                favLocationRepository = favLocationRepository,
                                latitude  =lat,
                                longitude = long
                            )
                        }
                    }
                }
            }

            val locationRequest = LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY,
                30000
            ).apply {
                setGranularity(Granularity.GRANULARITY_PERMISSION_LEVEL)
                setWaitForAccurateLocation(true)
            }.build()

            locationCallback = object: LocationCallback(){
                override fun onLocationResult(p0: LocationResult) {
                    super.onLocationResult(p0)
                    for(location in p0.locations){
                        lat = location.latitude
                        long = location.longitude
                    }
                }
            }

            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }catch(_: SecurityException){

        }


    }

    private fun requestPermissions(permissions: Array<String>){
        requestPermissions(
            permissions,
            CODIGO_PERMISO_SEGUNDO_PLANO
        )
    }




}
