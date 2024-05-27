package com.example.baywatch.userScreens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.baywatch.R
import com.example.baywatch.data.Beach
import com.example.baywatch.data.Dirtness
import com.example.baywatch.data.Flags
import com.example.baywatch.data.Status
import com.example.baywatch.ui.theme.BaywatchTheme
import com.example.baywatch.ui.theme.green
import com.example.baywatch.ui.theme.red
import com.example.baywatch.ui.theme.yellow
import java.text.SimpleDateFormat
import java.util.Locale


@Composable
fun StatusRecordScreen(
    modifier : Modifier = Modifier,
    beachSelected : Beach,
    statusRecord : List<Status>?,
    statusRecordError: String?,

){
    Column(modifier = modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ){

        if(statusRecordError == null ){



            if(statusRecord != null  && statusRecord.isNotEmpty()){

                LazyColumn(

                ) {
                    items(statusRecord) { status ->

                        Card() {
                            Column(
                                modifier = Modifier
                                    .padding(15.dp)
                                    .fillMaxWidth()
                            ) {

                                Row {
                                    val formatter =
                                        SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                                    Column(

                                    ){
                                        Text("Temperatura: ", fontWeight = FontWeight.ExtraBold)
                                        Text("Viento: ", fontWeight = FontWeight.ExtraBold)
                                        Text(
                                            "Presencia de medusas: ",
                                            fontWeight = FontWeight.ExtraBold
                                        )
                                        Text("Bandera: ", fontWeight = FontWeight.ExtraBold)
                                        Text("Estado de la playa: ", fontWeight = FontWeight.ExtraBold)

                                        Text(
                                            "Fecha: ",
                                            fontWeight = FontWeight.ExtraBold
                                        )
                                    }
                                    Spacer(modifier = Modifier.padding(10.dp))
                                    Column(

                                    ){

                                        Text("${status.Temperature.toString()} ºC")
                                        Text("${status.Wind.toString()} ")
                                        if (status.JellyFish)
                                            Text("Si")
                                        else
                                            Text("No")

                                        if (status.Flag == Flags.RED.name)
                                            Text("Roja ", color = red)
                                        else if (status.Flag == Flags.YELLOW.name)
                                            Text("Amarilla ", color = yellow)
                                        else
                                            Text("Verde", color = green)

                                        if (status.Dirtness == Dirtness.NONE.name)
                                            Text("Muy limpia")
                                        else if (status.Dirtness == Dirtness.LOW.name)
                                            Text("Limpia ")
                                        else if (status.Dirtness == Dirtness.MEDIUM.name)
                                            Text("Sucia")
                                        else
                                            Text("Muy sucia")
                                        Text(formatter.format(status.Date.toDate()))

                                    }
                                }

                            }
                        }
                        Spacer(modifier = Modifier.padding(10.dp))
                    }
                }
            }else{
                Column(modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center)
                { Text("No hay historial disponible") }
            }

        }else{
            Column(modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center)
            {
                Text(statusRecordError)
            }
        }
    }

}

@Preview
@Composable
fun StatusRecordScreenPreview(){
    BaywatchTheme {
        StatusRecordScreen(
            beachSelected = Beach(name="playa"),
            statusRecord = listOf(Status(Flag = "RED", Dirtness = Dirtness.HIGH.name)) ,
            statusRecordError = null,
            modifier = Modifier
                .fillMaxSize(),

        )
    }
}