package com.example.sleeprecorder.ui.screens

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sleeprecorder.R
import com.example.sleeprecorder.components.PersonalizatedDialog
import java.util.Calendar
import java.util.Timer
import java.util.TimerTask


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier ,
    acceptTimeStart: (Int, Int) -> Unit,
    acceptTimeEnd: (Int, Int) -> Unit,
    hourStart: Int,
    minuteStart:Int,
    hourEnd: Int,
    minuteEnd: Int,
    startRecording: ()->Unit,
    stopRecording:()->Unit,
    scheduleStart:(TimerTask, Calendar)->Unit,
    scheduleEnd:(TimerTask, Long)->Unit,
    navigateToRecordScreen: ()-> Unit,
    navigateHome:()-> Unit

){

    var timeStateInit by remember{ mutableStateOf(TimePickerState(0,0,true)) }
    var timeStateEnd by remember{ mutableStateOf(TimePickerState(0,0,true)) }
    var selectTimeStart by remember{ mutableStateOf(false) }
    var selectTimeEnd by remember{ mutableStateOf(false) }
    Column (
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(id = R.dimen.padding_medium)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){

        Spacer(modifier= Modifier.height(dimensionResource(id = R.dimen.space_normal)))
        Row {
            Button(
                onClick = {
                    selectTimeStart= !selectTimeStart

                },
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
            ) {
                Text(text = stringResource(R.string.seleccionar_hora_de_inicio),
                    textAlign = TextAlign.Center
                )
            }

            Button(
                onClick = {
                    selectTimeEnd = !selectTimeEnd
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
            ) {
                Text(text = stringResource(R.string.seleccionar_hora_de_fin), textAlign = TextAlign.Center)
            }
        }





        if(selectTimeStart){
            PersonalizatedDialog(
                onAcceptClick = {
                    selectTimeStart = false
                    acceptTimeStart(timeStateInit.hour,timeStateInit.minute)
                },
                title = stringResource(R.string.empezar_a_grabar),
                text = {
                    TimePicker(state = timeStateInit)
                },
                onDismiss = {
                    selectTimeStart = false

                }
            )
        }

        if(selectTimeEnd){
            PersonalizatedDialog(
                onAcceptClick = {
                    selectTimeEnd = false
                    acceptTimeEnd(timeStateEnd.hour,timeStateEnd.minute)
                },
                title = stringResource(R.string.hora_de_terminar),
                text = {
                    TimePicker(state = timeStateEnd)
                },
                onDismiss = {
                    selectTimeEnd = false

                }
            )
        }


    }
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.padding_medium)),
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.Bottom
    ){
        FloatingActionButton(onClick = {
            startRecordingIn(hourStart, minuteStart, startRecording,scheduleStart)
            stopRecordingAt(hourEnd,minuteEnd,stopRecording,scheduleEnd)
            navigateToRecordScreen()
        }) {
            Icon(painter= painterResource(id = R.drawable.play), contentDescription = "Comenzar")
        }
    }


}


fun startRecordingIn(hour: Int, minutes: Int, startRecording: () -> Unit, scheduleStart: (TimerTask, Calendar) -> Unit) {
    val currentTime = Calendar.getInstance()
    val totalMinutes = hour*60+minutes
    currentTime.add(Calendar.MINUTE, totalMinutes)

    val task = object : TimerTask() {
        override fun run() {
            startRecording()
        }
    }
    scheduleStart(task, currentTime)
    /*timer.schedule(object : TimerTask() {
        override fun run() {
            startRecording()
        }
    }, currentTime.time)*/
}
fun stopRecordingAt(hour: Int, minute: Int, stopRecording: () -> Unit, scheduleEnd: (TimerTask, Long) -> Unit ) {
    val currentTime = Calendar.getInstance()
    val calendar = Calendar.getInstance()

    // Establecer la hora y los minutos especificados por el usuario
    calendar.set(Calendar.HOUR_OF_DAY, hour)
    calendar.set(Calendar.MINUTE, minute)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)

    // Verificar si la hora especificada es menor que la actual para ajustar al día siguiente
    if (calendar.before(currentTime)) {
        calendar.add(Calendar.DAY_OF_MONTH, 1) // Añadir un día
    }

    val delayInMillis = calendar.timeInMillis - currentTime.timeInMillis

    val task = object : TimerTask() {
        override fun run() {
            stopRecording()
        }
    }
    scheduleEnd(task, delayInMillis)
    /*timer.schedule(object : TimerTask() {
        override fun run() {
            stopRecording()
        }
    }, delayInMillis)*/
}

/*
@Preview
@Composable
fun HomeScreenPreview(){
    SleepRecorderTheme {
        HomeScreen(
            modifier = Modifier
                .fillMaxSize()
                .padding((dimensionResource(id = R.dimen.padding_medium))),
            onDbChange = {},
            db =0f,
            onTestClick = {},
            onTestDismiss = {},
            isListening = false,
            acceptTimeStart = {},

        )
    }
}*/