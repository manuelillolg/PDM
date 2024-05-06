package com.example.sleeprecorder.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Slider
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.sleeprecorder.R
import com.example.sleeprecorder.components.PersonalizatedDialog


@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    db: Float,
    onDbChange: (Float)->Unit,
    onTestDismiss : () -> Unit,
    onTestClick: ()->Unit,
    isListening: Boolean,
){
    var prueba by remember{ mutableStateOf(false) }
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(text = stringResource(
            R.string.instrucciones),
            textAlign = TextAlign.Justify
        )
        Spacer(modifier = Modifier. height(dimensionResource(id = R.dimen.space_normal)))

        if(db.toInt() == 50 ){
            Text(
                text = stringResource(R.string.decibelios_muyAlta, db.toInt())
            )
        }
        else if(db.toInt() == 57) {
            Text(
                text = stringResource(R.string.decibelios_alta, db.toInt())
            )
        }
        else if(db.toInt() == 65) {
            Text(
                text = stringResource(R.string.decibelios_normal, db.toInt())
            )
        }
        else if(db.toInt() == 72) {
            Text(
                text = stringResource(R.string.decibelios_baja, db.toInt())
            )
        }
        else{
            Text(
                text = stringResource(R.string.decibelios_muyBaja, db.toInt())
            )
        }
        Row (
            modifier = Modifier
                .wrapContentSize()
                .fillMaxWidth()
            ,
            horizontalArrangement = Arrangement.Center
        ){
            Slider(
                value = db,
                onValueChange = onDbChange,
                steps = 3,
                valueRange = 50f .. 80f
            )
        }

        Button(
            onClick = {
                prueba = !prueba
                onTestClick()
            }
        ) {
            Text(text = stringResource(R.string.probar))
        }
    }

    if(prueba){
        PersonalizatedDialog(
            onAcceptClick = {
                prueba = false
                onTestDismiss()
            },
            title = stringResource(R.string.prueba_de_decibelios),
            text = {
                if(isListening){
                    Text(
                        text = "ESCUCHANDO",
                        color = Color.Green,
                        fontWeight = FontWeight.Bold
                    )
                }else{
                    Text(
                        text = "EN SILENCIO",
                        color = Color.Red,
                        fontWeight = FontWeight.Bold
                    )
                }

            },
            onDismiss = {
                prueba = false
                onTestDismiss()
            }
        )
    }
}

@Preview
@Composable
fun SettingsScreenPreview(){
    SettingsScreen(
        modifier = Modifier
            .fillMaxSize()
            .padding((dimensionResource(id = R.dimen.padding_medium))),
        db = 1.2f,
        isListening = false,
        onTestDismiss ={},
        onTestClick={},
        onDbChange = {}
    )
}