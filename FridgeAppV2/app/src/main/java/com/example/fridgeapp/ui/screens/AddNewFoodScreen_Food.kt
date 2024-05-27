package com.example.fridgeapp.ui.screens

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fridgeapp.R
import com.example.fridgeapp.data.ContainerEntity
import com.example.fridgeapp.data.FoodEntity
import com.example.fridgeapp.data.FoodV2
import com.example.fridgeapp.data.SectionEntity
import com.example.fridgeapp.ui.theme.FridgeAppTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AddNewFoodScreen_Food(
    modifier:Modifier = Modifier,
    foodList: List<FoodV2>,
    onAddClicked: ()->Unit,
    location: String,
    onDeleteClick: (Int)->Unit,
    onAcceptClick: () -> Unit
){


    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    if(foodList.size >0)
        Log.d("TAGGGG",foodList[0].toString())
    Column(
        modifier = modifier
    ) {

        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()

        ) {
            Text("AÑADE ALIMENTOS A: $location")
        }

        LazyColumn(
            modifier = modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {
            itemsIndexed(foodList) { index,foodItem ->
                Card(
                    modifier = modifier
                        .wrapContentSize()
                        .fillMaxWidth()

                ) {

                    Column(modifier=modifier,
                        horizontalAlignment = Alignment.Start
                    ) {
                        Row {
                            Text(text="ALIMENTO: ")
                            Text(
                                text = foodItem.food!!.name,
                                textAlign = TextAlign.Center
                            )
                        }

                        Row {
                            Text("CANTIDAD: ")
                            Text(
                                text = foodItem.quantity,
                                textAlign = TextAlign.Center
                            )
                        }
                        Row {
                            Text("COMENTARIOS: ")
                            Text(

                                text = foodItem.comment,
                                textAlign = TextAlign.Center
                            )
                        }

                        val purchaseDate = Date(foodItem.purchaseDate!!)

                        Row(

                        ) {
                            Text(
                                text = "FECHA DE COMPRA: "
                            )
                            Text(
                                text = formatter.format(purchaseDate),
                                textAlign = TextAlign.Center
                            )
                        }

                        IconButton(

                            onClick = {
                                Log.d("TAGGG",index.toString())
                                onDeleteClick(index)
                            }
                        ) {
                            Icon(
                                Icons.Filled.Delete,
                                contentDescription = stringResource(R.string.eliminar)
                            )
                        }
                    }

                }
            }
        }


    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Bottom
    ){
        FloatingActionButton(onClick = onAddClicked) {
            Icon(Icons.Filled.Add, contentDescription = "Añadir")
        }
    }
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.Bottom
    ){
        FloatingActionButton(onClick = { onAcceptClick() }) {
            Icon(Icons.Filled.Done, contentDescription = "Añadir")
        }
    }



}

@Composable
private fun AddNewItem(
    onAcceptClick: () -> Unit,
    title: String,
    name: String,
    onChangeName: (String)-> Unit,
    modifier: Modifier = Modifier,
    onDismiss: ()->Unit,
    isValid: Boolean
) {
    val activity = (LocalContext.current as Activity)

    AlertDialog(
        onDismissRequest = {
            // Dismiss the dialog when the user clicks outside the dialog or on the back
            // button. If you want to disable that functionality, simply use an empty
            // onCloseRequest.
        },
        title = {
            Text(text = title,
                textAlign = TextAlign.Center
            )
        },
        text = {
            Column(){

                OutlinedTextField(
                    value = name,
                    singleLine = true,
                    shape = MaterialTheme.shapes.large,
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        disabledContainerColor = MaterialTheme.colorScheme.surface,
                    ),
                    onValueChange = onChangeName,
                    isError = !isValid,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.None
                    ),
                )

            }

        },
        modifier = modifier,
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text(text = stringResource(R.string.cancelar))
            }
        },
        confirmButton = {
            TextButton(onClick = onAcceptClick) {
                Text(text = stringResource(R.string.aceptar))
            }
        },

        )
}

@Composable
@Preview
fun AddNewFoodScreenFoodPreview(){
    FridgeAppTheme {
        AddNewFoodScreen_Food(
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(id = R.dimen.padding_medium)),
            foodList = listOf(FoodV2(FoodEntity(name="lechuga",category="verdura"), quantity = "1")),
            onAddClicked = {},
            location="",
            onDeleteClick = {},
            onAcceptClick = {}

        )
    }
}