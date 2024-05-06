package com.example.sleeprecorder.ui.screens

import android.app.AlertDialog
import android.security.identity.PersonalizationData
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.sleeprecorder.R
import com.example.sleeprecorder.components.PersonalizatedDialog
import com.example.sleeprecorder.ui.theme.AppTheme


@Composable
fun RecordScreen(
    modifier:Modifier = Modifier,
    isRecording: Boolean,
    tryGoBack: Boolean,
    onAcceptClick: ()->Unit,
    endRecording: ()-> Unit,
    pendingTaskEmpty: Boolean
){
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        if(isRecording)
            Text(text = "GRABANDO" )
        else
            Text(text = "ESPERANDO PARA COMENZAR")

        if(tryGoBack){
            PersonalizatedDialog(
                onAcceptClick = onAcceptClick,
                title = stringResource(R.string.aviso),
                text = { Text("Si vuelves atrás terminará la grabación. ¿Seguro que desea abandonar la página?") }) {

            }

        }

        if(pendingTaskEmpty){
            endRecording()
        }


    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.padding_medium)),
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.Bottom
    ){
        FloatingActionButton(onClick = {
            onAcceptClick()
        }) {
            Icon(painter = painterResource(id = R.drawable.stop), contentDescription = "Terminar Grabacion")
        }
    }


}

@Preview
@Composable
fun RecordScreenPreview(

){
    AppTheme {
        RecordScreen(
            modifier = Modifier
                .fillMaxSize()
                .padding((dimensionResource(id = R.dimen.padding_medium))),
            isRecording = false,
            tryGoBack = false,
            onAcceptClick = {},
            endRecording = {},
            pendingTaskEmpty = false
        )
    }
}