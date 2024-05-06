package com.example.sleeprecorder.ui.screens

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import com.example.sleeprecorder.R
import com.example.sleeprecorder.components.AudioTypeDropdown
import com.example.sleeprecorder.components.PersonalizatedDialog
import com.example.sleeprecorder.ui.model.Audio
import com.example.sleeprecorder.ui.model.AudioTypes
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AudioListScreen(
    modifier: Modifier,
    audioList: List<Audio>,
    onAudioClick:(String)->Unit,
    onShareClick:(String)->Unit,
    audioTypeSelected: AudioTypes,
    updateTypeSelected: (AudioTypes)-> Unit,
    onAccept: (String,String,Long)-> Unit,
    onAcceptDelete: (String)->Unit,
    getAllAudios: ()-> Unit,
    getFilteredAudios:(String)->Unit
){
    var editName by remember{ mutableStateOf(false) }
    var deleteAudio by remember{ mutableStateOf(false) }
    var audioSelected : Audio? by remember{ mutableStateOf(null) }
    var filterSelected by remember { mutableStateOf(false) }
    var voiceSelected by remember { mutableStateOf(false) }
    var noiseSelected by remember { mutableStateOf(false) }
    var snoreSelected by remember { mutableStateOf(false) }
    var allSelected by remember { mutableStateOf(false) }
    LazyColumn (
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,

    ){
         if(!audioList.isEmpty()) {
             items(audioList) { data ->
                 Card(
                     modifier = Modifier
                         .fillMaxWidth(),
                     onClick = {}
                 ) {
                     Column(
                         modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))
                     ) {
                         Column(
                             modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))
                         ) {
                             Text(text = data.displayName)
                             val instant = Instant.ofEpochSecond(
                                 data.displayName.substringBefore("_").toLong() / 1000
                             )
                             val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
                             val formattedDate =
                                 formatter.format(instant.atZone(ZoneId.systemDefault()))
                             Text(text = formattedDate)

                             Row(
                                 modifier = Modifier.fillMaxWidth(),
                                 horizontalArrangement = Arrangement.SpaceBetween
                             ) {
                                 IconButton(onClick = { onAudioClick(data.displayName) }) {
                                     Icon(Icons.Default.PlayArrow, contentDescription = "Check")
                                 }
                                 IconButton(onClick = {
                                     deleteAudio = true
                                     audioSelected = data
                                 }) {
                                     Icon(Icons.Default.Delete, contentDescription = "Delete")

                                 }
                                 IconButton(onClick = {
                                     editName = true
                                     audioSelected = data
                                 }) {
                                     Icon(Icons.Default.Edit, contentDescription = "Edit")

                                 }
                                 IconButton(onClick = {
                                     audioSelected = data
                                     onShareClick(data.displayName)
                                 }) {
                                     Icon(Icons.Default.Share, contentDescription = "Share")

                                 }
                             }
                         }
                     }


                 }
                 Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_small))) // Espacio entre tarjetas
             }
         }

    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(dimensionResource(id = R.dimen.padding_medium)),
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.Bottom
    ){
        FloatingActionButton(onClick = { filterSelected= true }) {
            Icon(painterResource(id = R.drawable.ic_action_name),"Filter")
        }
    }

    if(editName) {

        PersonalizatedDialog(
            onAcceptClick = {
                onAccept(audioSelected!!.displayName,audioTypeSelected.name,audioSelected!!.displayName.substringBefore("_").toLong())
                updateTypeSelected(AudioTypes.RONQUIDO)
                editName = false
            },
            title = "EDICION DE AUDIO",
            onDismiss = {
                editName = false
                updateTypeSelected(AudioTypes.RONQUIDO)
            },
            text = {
                Text("Seleccione el tipo de audio registrado.")
                AudioTypeDropdown(
                    selectedType = audioTypeSelected,
                    onTypeSelected = {
                        updateTypeSelected(it)
                    }
                )
            },

            )
    }
    if(deleteAudio) {

        PersonalizatedDialog(
            onAcceptClick = {
                onAcceptDelete(audioSelected!!.displayName)
                deleteAudio = false
            },
            title = "ELIMINAR AUDIO",
            onDismiss = {
                deleteAudio = false

            },
            text = {
                Text("¿Está seguro de eliminar ${audioSelected!!.displayName}?")
            },
        )
    }

    if (filterSelected){
        PersonalizatedDialog(
            onAcceptClick = {
                var petition : String = ""
                if(allSelected){
                    getAllAudios()
                }
                else{
                    if(voiceSelected){
                        petition+="VOZ"
                    }
                    if(snoreSelected){
                        if(voiceSelected){
                            petition+= " "
                        }
                        petition+="RONQUIDO"
                    }
                    if(noiseSelected){
                        if(voiceSelected || snoreSelected){
                            petition+= " "
                        }
                        petition+="RUIDO"
                    }

                    getFilteredAudios(petition)
                }
                filterSelected = false
            },
            title = "FILTRAR AUDIOS",
            onDismiss = {
                filterSelected = false
            },
            text = {
                Column (
                    horizontalAlignment = Alignment.CenterHorizontally

                ){
                    Text("Seleccione el tipo de audio para filtrar.")
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_medium)))
                    //AudioTypes.values().forEach { type ->
                    Button(
                        onClick = { voiceSelected = !voiceSelected },
                        border = if(voiceSelected) BorderStroke(color = Color.Black, width =4.dp) else BorderStroke(color = Color.Transparent, width =0.dp),
                        modifier = Modifier
                            .width(dimensionResource(id = R.dimen.buttom_width_normal))
                            .height(dimensionResource(id = R.dimen.buttom_height_normal))
                    ) {
                        Text(text = "VOZ")
                    }

                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_small)))
                    Button(
                        onClick = { noiseSelected = !noiseSelected },
                        border = if(noiseSelected) BorderStroke(color = Color.Black, width =4.dp) else BorderStroke(color = Color.Transparent, width =0.dp),
                        modifier = Modifier
                            .width(dimensionResource(id = R.dimen.buttom_width_normal))
                            .height(dimensionResource(id = R.dimen.buttom_height_normal))
                    ) {
                        Text(text = "RUIDO")
                    }
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_small)))
                    Button(
                        onClick = { snoreSelected = !snoreSelected },
                        border = if(snoreSelected) BorderStroke(color = Color.Black, width =4.dp) else BorderStroke(color = Color.Transparent, width =0.dp),
                        modifier = Modifier
                            .width(dimensionResource(id = R.dimen.buttom_width_normal))
                            .height(dimensionResource(id = R.dimen.buttom_height_normal))
                    ) {
                        Text(text = "RONQUIDO")
                    }
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_small)))
                   // }
                    Button(
                        onClick = {
                            allSelected = !allSelected
                            voiceSelected = allSelected
                            snoreSelected = allSelected
                            noiseSelected = allSelected
                        },
                        modifier = Modifier
                            .width(dimensionResource(id = R.dimen.buttom_width_normal))
                            .height(dimensionResource(id = R.dimen.buttom_height_normal)),
                        border = if(allSelected) BorderStroke(color = Color.Black, width =4.dp) else BorderStroke(color = Color.Transparent, width =0.dp)
                        ) {
                        Text(text = "TODOS")
                    }
                }
            },

            )
    }


}
/*
@Preview
@Composable
fun AudioListScreenPreview(

){
    SleepRecorderTheme {
        AudioListScreen(
            modifier = Modifier
                .fillMaxSize()
                .padding((dimensionResource(id = R.dimen.padding_medium))),
            audioList = listOf( Audio("ejemplo.mp3", "unknown", 1714305001) ),
            onAudioClick = {},
            audioTypeSelected = AudioTypes.RONQUIDO,
            updateTypeSelected= {},
            onAccept={
                it, it2
            }
        )
    }
}
*/
