package com.example.baywatch.userScreens

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.baywatch.data.Beach
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState


@Composable
fun HomeScreen(
    modifier : Modifier = Modifier,
    allBeaches: List<Beach>,
    errorAllBeaches: String?,
    onMarkerClicked:(Beach)->Unit,
    reloadAllBeches: ()->Unit,
    reloadFavBeaches: ()->Unit,
    latitude: Double?,
    longitude: Double?
){
    var favClicked by remember{ mutableStateOf(false) }

    Column(modifier = modifier
        .fillMaxWidth()
        .fillMaxHeight()
    ){

        var infoClicked by remember{ mutableStateOf(false) }
        var beachClicked : Beach? by remember{ mutableStateOf(null) }

        if(errorAllBeaches == null) {

            var  firstPoint : LatLng

            if(latitude != null && longitude != null){
                firstPoint = LatLng(latitude, longitude)
            }else{
                if(allBeaches.isNotEmpty()){
                    firstPoint = LatLng(allBeaches[0].location!!.latitude,allBeaches[0].location!!.longitude)
                }else{
                    firstPoint = LatLng(0.0,0.0)
                }
            }



            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(firstPoint, 10f)
            }
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
            ) {
                Log.d("TAGGGGG", allBeaches.toString())
                for (beach in allBeaches) {
                    Marker(
                        state = MarkerState(
                            position = LatLng(
                                beach.location!!.latitude,
                                beach.location.longitude
                            )
                        ),
                        title = beach.name,
                        snippet = "Pulsar para más información",
                        onInfoWindowClick = {
                            infoClicked = true
                            beachClicked = beach
                        }

                    )
                }
            }

        }else{
            Column(
                modifier = modifier,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ){
                Text(errorAllBeaches.toString())
            }
        }

        if(infoClicked){
            infoClicked = false
            var beach = beachClicked!!.copy()
            beachClicked = null
            onMarkerClicked(beach)
        }


    }
    if(errorAllBeaches == null){
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Bottom
        ){
            FloatingActionButton(
                onClick = {
                    favClicked = !favClicked

                    if(favClicked){
                        reloadFavBeaches()
                    }else{
                        reloadAllBeches()
                    }
                }
            ) {
                if(favClicked)
                    Icon(Icons.Filled.Favorite, contentDescription = "Filtro de playas favoritas encendido")
                else
                    Icon(Icons.Filled.FavoriteBorder, contentDescription = "Filtro de playas favoritas apagado")

            }
        }
    }
}