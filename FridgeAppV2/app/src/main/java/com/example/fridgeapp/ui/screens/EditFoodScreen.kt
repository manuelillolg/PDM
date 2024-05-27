package com.example.fridgeapp.ui.screens

import android.app.Activity
import android.app.DatePickerDialog
import android.util.Log
import android.widget.DatePicker
import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.fridgeapp.R
import com.example.fridgeapp.components.PersonalizatedDialog
import com.example.fridgeapp.data.FoodEntity
import com.example.fridgeapp.data.FoodV2
import com.example.fridgeapp.model.Food
import com.example.fridgeapp.model.Route
import com.example.fridgeapp.ui.theme.FridgeAppTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.material3.*
import androidx.compose.runtime.*
import com.example.fridgeapp.data.ContainerEntity
import com.example.fridgeapp.data.SectionEntity
import java.util.Calendar

@Composable
fun EditFoodScreen(
    modifier : Modifier = Modifier,
    food : FoodV2,
    onFoodDelete: (Long,String)->Unit,
    updateFood: (FoodV2)->Unit,
    possibleConatiners: List<ContainerEntity>,
    onContainerChange: (ContainerEntity)->Unit,
    possibleSections: List<SectionEntity>,
    onSectionChange: (SectionEntity)->Unit,
    sectionSelected: SectionEntity,
    containerSelected: ContainerEntity,
    onLocationChange: (FoodV2)->Unit,
    navigateBack: (FoodV2)->Unit

){
    var delete by remember{ mutableStateOf(false) }
    var editComment by remember{ mutableStateOf(false) }
    var text by remember{ mutableStateOf(food.comment) }
    var expirationDate by remember{ mutableStateOf(false) }
    var date: Date by remember{ mutableStateOf(Date()) }
    var locationChange by remember{ mutableStateOf(false) }
    var visibleAccept by remember{ mutableStateOf(false) }
    var locationChangeStep by remember{ mutableIntStateOf(0) }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        Row(

        ) {
            Text(
                text = food.food!!.name,
                style = MaterialTheme.typography.titleLarge
            )
        }

        Spacer(
            modifier = Modifier
                .height(dimensionResource(id = R.dimen.space_normal))
        )

        Button(
            onClick = { locationChange = !locationChange },
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(id = R.dimen.buttom_height_normal)),
            ) {
            Text("CAMBIAR UBICACIÓN")
        }
        Spacer(
            modifier = Modifier
                .height(dimensionResource(id = R.dimen.space_normal))
        )

        Button(
            onClick = { editComment = !editComment },
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(id = R.dimen.buttom_height_normal)),

            ) {
            Text("EDITAR COMENTARIO")
        }
        Spacer(
            modifier = Modifier
                .height(dimensionResource(id = R.dimen.space_normal))
        )

        Button(
            onClick = { expirationDate = !expirationDate },
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(id = R.dimen.buttom_height_normal)),

            ) {
            Text("CAMBIAR FECHA DE CADUCIDAD")
        }
        Card(
            modifier = modifier
                .wrapContentSize()
                .fillMaxWidth()
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
                        Spacer(
                            modifier = Modifier
                                .width(dimensionResource(id = R.dimen.padding_small))
                        )

                    }
                    Spacer(
                        modifier = Modifier
                            .height(dimensionResource(id = R.dimen.padding_small))
                    )

                    Row {
                        Text(
                            text = stringResource(R.string.fecha_de_compra),
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(
                            modifier = Modifier
                                .width(dimensionResource(id = R.dimen.padding_small))
                        )
                        Text(text =formatter.format(Date(food.purchaseDate!!)))

                    }
                    Spacer(
                        modifier = Modifier
                            .height(dimensionResource(id = R.dimen.padding_small))
                    )

                    Row {
                        Text(
                            text = stringResource(R.string.fecha_de_caducidad),
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(
                            modifier = Modifier
                                .width(dimensionResource(id = R.dimen.padding_small))
                        )
                        var expirationDate : String
                        if(food.expirationDate == null){
                          expirationDate = "-"
                        }else{
                            expirationDate= formatter.format(Date(food.expirationDate!!))
                        }
                        Text(text = expirationDate)
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
                        Text(text = food.quantity)

                    }

                    Spacer(
                        modifier = Modifier
                            .height(dimensionResource(id = R.dimen.padding_small))
                    )

                    Row {
                        Text(
                            text = "COMENTARIO: ",
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(
                            modifier = Modifier
                                .width(dimensionResource(id = R.dimen.padding_small))
                        )
                        Text(text = food.comment)

                    }
                    Spacer(
                        modifier = Modifier
                            .height(dimensionResource(id = R.dimen.padding_small))
                    )



                }
            }
        }

        Spacer(
            modifier = Modifier
                .height(dimensionResource(id = R.dimen.space_normal))
        )


    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.Bottom
    ){
        FloatingActionButton(
            onClick = {
                food.quantity = (food.quantity.toInt()+1).toString()
                updateFood(food)
            }
        ) {
            Icon( painterResource(id = R.drawable.add), contentDescription ="Aumentar cantidad" )
        }
    }
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Bottom
    ){
        FloatingActionButton(
            onClick = {
                if(food.quantity.toInt() > 1) {
                    food.quantity = (food.quantity.toInt() - 1).toString()
                    updateFood(food)
                }else{
                    delete = !delete
                }
            }
        ) {
            Icon( painterResource(id = R.drawable.reduce), contentDescription ="Reducir cantidad" )
        }
    }
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ){
        FloatingActionButton(onClick = { delete = !delete}) {
            Icon( Icons.Filled.Delete, contentDescription ="Eliminar alimento" )
        }
    }

    if(delete){
        PersonalizatedDialog(
            onAcceptClick = { onFoodDelete(food.food!!.id!!,food.food!!.name)},
            title = "ELIMINAR ALIMENTO",
            text = { Text("¿Está seguro de eliminar el alimento de forma permanente?") },
            onDismiss = {
                delete = !delete
            }
        )
    }

    if(locationChange){
        PersonalizatedDialog(
            onAcceptClick = {
                food.location = "${containerSelected.name}/${sectionSelected.name}"
                onLocationChange(food)
                locationChange = !locationChange
            },
            title = "CAMBIAR UBICACIÓN",
            text = {

                when(locationChangeStep){
                   0-> Column(){
                            AddNewFoodScreen_Container(
                                possibleContainers = possibleConatiners,
                                onContainerValueChanged = {
                                    onContainerChange(it)
                                    locationChangeStep += 1
                                },
                                modifier = modifier
                            )
                        }
                    1-> Column(modifier = modifier){
                        Button(
                            onClick = { locationChangeStep -= 1}
                        ) {
                            Icon(Icons.Filled.ArrowBack,contentDescription = "Atrás")
                        }
                            AddNewFoodScreen_Section(
                                possibleSections = possibleSections,
                                onSectionValueChanged = {
                                    onSectionChange(it)
                                    locationChangeStep += 1
                                    visibleAccept = true
                                },
                                modifier = modifier
                            )

                    }
                    2-> Column(){
                        Button(
                            onClick = { locationChangeStep -= 1
                                visibleAccept = false}
                        ) {
                            Icon(Icons.Filled.ArrowBack,contentDescription = "Atrás")
                        }
                        Text("La nueva localización será: ${containerSelected.name}/${sectionSelected.name}")

                    }
                }
            },
            onDismiss = {
                locationChangeStep = 0
                locationChange = !locationChange
            },
            visibleAccept = visibleAccept
        )
    }

    if(editComment){
        PersonalizatedDialog(
            onAcceptClick = {
                food.comment = text
                editComment = !editComment
                updateFood(food)
            },
            title = "EDITAR COMENTARIO",
            text = {
                Column {
                    Text("El texto escrito modificará el comentario actual")
                    OutlinedTextField(
                        value = text,
                        onValueChange = { text = it },
                        singleLine = true
                    )
                }
            },
            onDismiss = {
                editComment = !editComment
                text = ""
            }
        )
    }

    if(expirationDate){
        PersonalizatedDialog(
            onAcceptClick = {
                food.expirationDate = date.time
                updateFood(food)
                expirationDate=!expirationDate
            },
            title = "CAMBIAR FECHA DE CADUCIDAD",
            text = {
                val context = LocalContext.current
                val calendar = Calendar.getInstance().apply { time = date }
                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH)
                val day = calendar.get(Calendar.DAY_OF_MONTH)
                val datePicker = DatePickerDialog(
                    context,
                    { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDayOfMonth: Int ->
                        val newDate = Calendar.getInstance().apply {
                            set(selectedYear, selectedMonth, selectedDayOfMonth)
                        }
                       date = newDate.time
                    }, year, month, day)
                Column(horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center){
                    IconButton(onClick = { datePicker.show() }) {
                        Icon(imageVector = Icons.Filled.DateRange, contentDescription = "Elegir Fecha")
                    }
                }
            },
            onDismiss = {
                expirationDate = !expirationDate
            }
        )
    }

    BackHandler (onBack = {navigateBack(food)})
        //navigateBack(food)


}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun EditNameDialog(
    onAcceptClick: () -> Unit,
    provisionalFoodName: String,
    food: FoodV2,
    onChangeName: (String)-> Unit,
    modifier: Modifier = Modifier,
    onDismiss: ()->Unit
) {

}
/*
@Composable
@Preview
fun EditFoodScreenPreview(){
    FridgeAppTheme {

         EditFoodScreen(
            food = FoodV2(food= FoodEntity(name="PLATANO", category = "FRUTA")),
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(id = R.dimen.padding_medium)),
             updateFood = {}
        )
    }


}*/