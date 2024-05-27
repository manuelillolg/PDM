package com.example.fridgeapp.ui.screens

import android.util.Log
import android.view.ContextMenu
import android.view.Menu
import android.widget.AutoCompleteTextView
import android.widget.TextView
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fridgeapp.R
import com.example.fridgeapp.components.PersonalizatedDialog
import com.example.fridgeapp.data.FoodEntity
import com.example.fridgeapp.data.FoodV2
import com.example.fridgeapp.model.Container
import com.example.fridgeapp.model.Food
import com.example.fridgeapp.model.Route
import com.example.fridgeapp.model.Section
import com.example.fridgeapp.ui.theme.FridgeAppTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodDetailScreen(
    modifier: Modifier = Modifier,
    foods: List<FoodV2>,
    onFoodClick: (FoodV2)->Unit,
    onFoodDelete: (Long,String)->Unit,
    onEmptyFood: ()->Unit,
    title: Boolean = true

){

    var delete by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf(0)}
    if(!foods.isEmpty()){
        Column(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {


            if(title){
                Row(

                ) {
                    Text(
                        text = foods[0].food!!.name,
                        style = MaterialTheme.typography.titleLarge
                    )
                }

                Spacer(
                    modifier = Modifier
                        .height(dimensionResource(id = R.dimen.space_normal))
                )
            }







            LazyColumn(
                modifier = Modifier
                    .padding(vertical = dimensionResource(id = R.dimen.padding_small))
            )
            {
                itemsIndexed(foods.toList())
                { index, food ->
                    Card(
                        modifier = modifier
                            .wrapContentSize()
                            .fillMaxWidth(),
                        onClick = { onFoodClick(food) }

                    )
                    {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .padding(dimensionResource(id = R.dimen.padding_small))
                        )
                        {
                            Column(
                                modifier = Modifier
                                    .wrapContentSize()

                            ) {
                               if(!title) {
                                    Row {
                                        Text(
                                            text = "NOMBRE:",
                                            fontWeight = FontWeight.Bold
                                        )
                                        Spacer(
                                            modifier = Modifier
                                                .width(dimensionResource(id = R.dimen.padding_small))
                                        )
                                        Text(text = food.food!!.name)
                                    }
                                    Spacer(
                                        modifier = Modifier
                                            .height(dimensionResource(id = R.dimen.padding_small))
                                    )
                                }
                                Row {
                                    Text(
                                        text = stringResource(R.string.ubicacion),
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(
                                        modifier = Modifier
                                            .width(dimensionResource(id = R.dimen.padding_small))
                                    )
                                    Text(text = food.location)
                                }
                                Spacer(
                                    modifier = Modifier
                                        .height(dimensionResource(id = R.dimen.padding_small))
                                )



                                Row {
                                    Text(
                                        text = stringResource(R.string.cantidad),
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(
                                        modifier = Modifier
                                            .width(dimensionResource(id = R.dimen.padding_small))
                                    )
                                    Text(text = food.quantity.toString())

                                }


                                Spacer(
                                    modifier = Modifier
                                        .height(dimensionResource(id = R.dimen.padding_small))
                                )
                                IconButton(
                                    onClick = {
                                        delete = !delete
                                        selectedItem = index
                                    }
                                ) {
                                    Icon(Icons.Filled.Delete, contentDescription = "Eliminar")
                                }


                            }
                        }
                    }
                }
            }
        }
    }else{
        onEmptyFood()
        Column(modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center){
            Text("No hay alimentos")
        }
    }
    if(delete){
        PersonalizatedDialog(
            onAcceptClick = {
                Log.d("TAGGGGGG",selectedItem.toString())
                delete = !delete
                onFoodDelete(foods[selectedItem].food!!.id!!,foods[selectedItem].food!!.name)

            },
            title = "ELIMINAR ALIMENTO",
            text = { Text("¿Está seguro de que desea eliminar el alimento permanentemente?") },
            onDismiss = {delete = !delete}
        )
    }
}
/*
@Composable
@Preview

fun FoodDetailPreview(){
    FridgeAppTheme(){
        FoodDetailScreen(
            foods= listOf(FoodV2(FoodEntity(name="Lechuga", category = "verdura"))),
            onFoodClick = {},
            onFoodDelete = { 1," "->},
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(id = R.dimen.padding_medium))
        )
    }
}*/