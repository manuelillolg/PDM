package com.example.baywatch.userScreens


import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.relocation.bringIntoViewResponder
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
import androidx.compose.ui.res.dimensionResource
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
import com.google.firebase.database.collection.LLRBNode
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun BeachDetailScreen(
    modifier : Modifier = Modifier,
    beachSelected : Beach,
    currentStatus : Status?,
    currentStatusError: String?,
    isFav: Boolean,
    favClicked: (String)->Unit,
    onBackClicked : ()->Unit

){
    Column(modifier = modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){

        if(currentStatusError == null ){


            if(currentStatus != null ){
                Card(modifier  =Modifier .fillMaxWidth()) {
                    Column(
                        modifier = Modifier
                            .padding(15.dp)
                            .fillMaxWidth(),
                    ) {
                        val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                beachSelected.name,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 30.sp
                            )
                        }

                        Spacer(modifier = Modifier.padding(10.dp))

                        Row(horizontalArrangement = Arrangement.Center) {
                            Column(){
                                Text("Temperatura: ", fontWeight = FontWeight.ExtraBold)
                                Text("Viento: ", fontWeight = FontWeight.ExtraBold)
                                Text("Presencia de medusas: ", fontWeight = FontWeight.ExtraBold)
                                Text("Bandera: ", fontWeight = FontWeight.ExtraBold)
                                Text("Estado de la playa: ", fontWeight = FontWeight.ExtraBold)
                                Text("Última actualización: ", fontWeight = FontWeight.ExtraBold)
                            }

                            Spacer(modifier = Modifier.padding(10.dp))

                            Column(){
                                Text("${ currentStatus.Temperature.toString() } ºC" )
                                Text("${ currentStatus.Wind.toString() } " )
                                if(currentStatus.JellyFish)
                                    Text("Si")
                                else
                                    Text("No")
                                if(currentStatus.Flag == Flags.RED.name)
                                    Text("Roja ", color= red )
                                else if(currentStatus.Flag == Flags.YELLOW.name)
                                    Text("Amarilla ", color= yellow )
                                else
                                    Text("Verde", color= green )

                                if(currentStatus.Dirtness == Dirtness.NONE.name)
                                    Text("Muy limpia")
                                else if(currentStatus.Dirtness == Dirtness.LOW.name)
                                    Text("Limpia " )
                                else if (currentStatus.Dirtness == Dirtness.MEDIUM.name)
                                    Text("Sucia" )
                                else
                                    Text("Muy sucia")

                                Text(formatter.format(currentStatus.Date.toDate()))
                            }

                        }

                    }
                }
            }else{
                Text("No hay información disponible sobre la playa")
            }

        }else{
            Text(currentStatusError)
        }
    }
    if(currentStatusError == null ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Top
        ){
            IconButton(
                modifier = Modifier.wrapContentSize(),
                onClick = { favClicked(beachSelected.id!!) }
            ) {
                if (isFav) {

                    Icon(
                        imageVector = Icons.Filled.Favorite,
                        contentDescription = "Favoritos marcado"
                    )
                }else
                    Icon(
                        Icons.Filled.FavoriteBorder,
                        contentDescription = "Favoritos desmarcado")
            }
        }
    }

    BackHandler (onBack = onBackClicked)
}

@Preview
@Composable
fun BeachDetailScreenPreview(){
    BaywatchTheme {
        BeachDetailScreen(
            beachSelected = Beach(name="playa"),
            currentStatus = Status(Flag = "GREEN", Dirtness = Dirtness.HIGH.name) ,
            currentStatusError = null,
            modifier = Modifier
                .fillMaxSize(),
            isFav = false,
            favClicked = {},
            onBackClicked = {}
        )
    }
}