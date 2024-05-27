package com.example.baywatch.workerScreens

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.baywatch.data.Beach
import com.example.baywatch.data.Dirtness
import com.example.baywatch.data.Flags
import com.example.baywatch.data.Status
import com.example.baywatch.ui.theme.BaywatchTheme



@Composable
fun AddStatusScreen(
    modifier :Modifier = Modifier,
    currentStatus: Status?,
    saveStatus: (Status)->Unit,
    beachSelected: Beach,
    currentStatusError: String?,
    backPressed: ()->Unit
){
    var status: Status by remember{ mutableStateOf(Status(
        Flag = Flags.GREEN.name,
        Dirtness = Dirtness.NONE.name,
        JellyFish = false
    ))}
    var flag: Flags by remember{ mutableStateOf(Flags.GREEN) }
    var dirtness: Dirtness by remember{ mutableStateOf(Dirtness.NONE) }
    var jellyfish: Boolean by remember { mutableStateOf(false) }
    var windError: Boolean by remember { mutableStateOf(false) }
    var temperatureError: Boolean by remember { mutableStateOf(false) }
    var wind : String by remember{ mutableStateOf("") }
    var temperature : String by remember{ mutableStateOf("") }
    var initialize : Boolean by remember{ mutableStateOf(false) }

    if(currentStatus!= null && !initialize){
        status = currentStatus

        if(status.Flag == Flags.GREEN.name){
            flag = Flags.GREEN
        }else if (status.Flag == Flags.YELLOW.name){
            flag = Flags.YELLOW
        }else
            flag = Flags.RED

        if(status.Dirtness == Dirtness.NONE.name){
            dirtness = Dirtness.NONE
        }else if(status.Dirtness == Dirtness.LOW.name){
            dirtness = Dirtness.LOW
        }else if(status.Dirtness == Dirtness.MEDIUM.name){
            dirtness = Dirtness.MEDIUM
        }else{
            dirtness = Dirtness.HIGH
        }

        wind = status.Wind.toString()
        temperature = status.Temperature.toString()
        jellyfish = status.JellyFish
        initialize = true
        
    }


    if(currentStatusError == null){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Bottom
        ) {
            FloatingActionButton(onClick = {
                temperatureError = (temperature.toIntOrNull() == null)
                windError = (wind.toIntOrNull() == null )

                if(!temperatureError && !windError){
                    status.Wind = wind.toInt()
                    status.Temperature = temperature.toInt()
                    saveStatus(status)
                }

            }) {
                Icon(Icons.Filled.Send, contentDescription = "Actualizar estado")
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                beachSelected.name,
                fontSize = 30.sp, fontWeight = FontWeight.ExtraBold
            )
        }
    }

    Column(
        modifier = modifier
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,


    ){

        if(currentStatusError == null){
            OutlinedTextField(
                value = temperature,
                onValueChange = {
                    temperature = it
                },
                label = { Text(text = "Temperatura del agua") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.NumberPassword
                ),
                isError = temperatureError
            )
            Spacer(modifier = Modifier.padding(10.dp))

            OutlinedTextField(
                value = wind,
                onValueChange = {
                    wind = it
                },
                label = { Text(text = "Velocidad del viento") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.NumberPassword
                ),
                isError = windError
            )
            Spacer(modifier = Modifier.padding(10.dp))

            Text("Bandera:")
            Spacer(modifier = Modifier.padding(5.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {

                Button(
                    colors = ButtonDefaults.buttonColors(
                        Color.Green

                    ),
                    onClick = {
                        flag = Flags.GREEN
                        status.Flag = flag.name

                    },
                    border = BorderStroke(
                        4.dp,
                        if (flag == Flags.GREEN) Color.Black else Color.Transparent
                    )

                ) {

                }
                Spacer(modifier = Modifier.padding(10.dp))
                Button(
                    colors = ButtonDefaults.buttonColors(
                        Color.Yellow
                    ),
                    onClick = {
                        flag = Flags.YELLOW
                        status.Flag = flag.name
                    },
                    border = BorderStroke(
                        4.dp,
                        if (flag == Flags.YELLOW) Color.Black else Color.Transparent
                    )
                ) {

                }
                Spacer(modifier = Modifier.padding(10.dp))
                Button(
                    colors = ButtonDefaults.buttonColors(
                        Color.Red
                    ),
                    onClick = {
                        flag = Flags.RED
                        status.Flag = flag.name
                    },
                    border = BorderStroke(
                        4.dp,
                        if (flag == Flags.RED) Color.Black else Color.Transparent
                    )
                ) {

                }

            }
            Spacer(modifier = Modifier.padding(10.dp))
            Text("Suciedad:")
            Spacer(modifier = Modifier.padding(5.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {
                        dirtness = Dirtness.NONE
                        status.Dirtness = dirtness.name
                    },
                    border = BorderStroke(
                        4.dp,
                        if (dirtness == Dirtness.NONE) Color.Black else Color.Transparent
                    )
                ) {
                    Text("- -")
                }
                Spacer(modifier = Modifier.padding(10.dp))
                Button(
                    onClick = {
                        dirtness = Dirtness.LOW
                        status.Dirtness = dirtness.name
                    },
                    border = BorderStroke(
                        4.dp,
                        if (dirtness == Dirtness.LOW) Color.Black else Color.Transparent
                    )

                ) {
                    Text("-")
                }
                Spacer(modifier = Modifier.padding(10.dp))
                Button(
                    onClick = {
                        dirtness = Dirtness.MEDIUM
                        status.Dirtness = dirtness.name
                    },
                    border = BorderStroke(
                        4.dp,
                        if (dirtness == Dirtness.MEDIUM) Color.Black else Color.Transparent
                    )

                ) {
                    Text("+")
                }
                Spacer(modifier = Modifier.padding(10.dp))
                Button(
                    onClick = {
                        dirtness = Dirtness.HIGH
                        status.Dirtness = dirtness.name
                    },
                    border = BorderStroke(
                        4.dp,
                        if (dirtness == Dirtness.HIGH) Color.Black else Color.Transparent
                    )

                ) {
                    Text("+ +")
                }

            }
            Spacer(modifier = Modifier.padding(10.dp))
            Text("Medusas:")
            Spacer(modifier = Modifier.padding(5.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {
                        jellyfish = true
                        status.JellyFish = jellyfish
                    },
                    border = BorderStroke(4.dp, if (jellyfish) Color.Black else Color.Transparent)

                ) {
                    Text("S")
                }
                Spacer(modifier = Modifier.padding(10.dp))
                Button(
                    onClick = {
                        jellyfish = false
                        status.JellyFish = jellyfish
                    },
                    border = BorderStroke(4.dp, if (!jellyfish) Color.Black else Color.Transparent)

                ) {
                    Text("N")
                }

            }
        }else{
            Text(currentStatusError)
        }
    }

    BackHandler (
        onBack = backPressed
    )
}

@Preview
@Composable

fun AddStatusPreview(){
    BaywatchTheme {
        AddStatusScreen(
            modifier = Modifier.fillMaxSize(),
            currentStatus = null,
            saveStatus = {},
            beachSelected = Beach(name="plata"),
            currentStatusError = "EROR",
            backPressed = {}
        )
    }
}