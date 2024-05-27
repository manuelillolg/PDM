package com.example.fridgeapp.ui.screens

import android.app.Activity
import android.app.DatePickerDialog
import android.util.Log
import android.widget.Button
import android.widget.DatePicker
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fridgeapp.R
import com.example.fridgeapp.data.ContainerEntity
import com.example.fridgeapp.data.FoodEntity
import com.example.fridgeapp.data.FoodRecordEntity
import com.example.fridgeapp.data.FoodV2
import com.example.fridgeapp.data.SectionEntity
import com.example.fridgeapp.ui.theme.FridgeAppTheme
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AddNewFoodScreen_SelectFood(
    modifier:Modifier = Modifier,
    foodV2: FoodV2?,
    quantity: String,
    isValidQuantity: Boolean,
    onQuantityChanged: (String)->Unit,
    quantityValidation: ()->Unit,
    comment: String,
    onCommentChange: (String)->Unit,
    onAddClicked: ()->Unit,
    recordFoodSelected: FoodRecordEntity?,
    onSaveClick: (String,String)->Unit

){
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    var date: Date by remember{ mutableStateOf(Date()) }
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {


        Row(

        ){
            Column {
                Row {
                    Text(text = "Nombre: ")
                    if (foodV2!!.food != null) {
                        Spacer(modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.padding_medium)))
                        Text(text=foodV2.food!!.name)
                    }else{
                        Log.d("TAGGGG", "NULL")
                    }

                }
                Spacer(modifier = Modifier.padding( dimensionResource(id = R.dimen.padding_small)))

                Button(
                    onClick = { onAddClicked() },
                    modifier = Modifier .fillMaxWidth()
                ) {
                    Text(text="SELECCIONAR")
                }
            }

        }
        Spacer(modifier = Modifier.padding( dimensionResource(id = R.dimen.padding_small)))
        Row(

        ){
            Column {
                Text(text = "Cantidad: ")
                Spacer(modifier = Modifier.padding( dimensionResource(id = R.dimen.padding_small)))
                OutlinedTextField(
                    value = quantity,
                    onValueChange = {onQuantityChanged(it)},
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    ),
                    isError = !isValidQuantity,
                    singleLine = true,
                    modifier = Modifier .fillMaxWidth()
                )
            }


        }
        Spacer(modifier = Modifier.padding( dimensionResource(id = R.dimen.padding_small)))
        Row(

        ){
            Column {
                Text(text = "Comentarios: ")
                Spacer(modifier = Modifier.padding( dimensionResource(id = R.dimen.padding_small)))
                OutlinedTextField(
                    value = comment,
                    onValueChange = {onCommentChange(it)},
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }

        }
        Spacer(modifier = Modifier.padding( dimensionResource(id = R.dimen.padding_small)))
        Row(

        ){

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
                    foodV2!!.expirationDate =date.time
                }, year, month, day)
            Column (modifier = Modifier.fillMaxWidth()){
                Row{
                    Text(text = "Fecha de caducidad: ")
                    if(foodV2!!.expirationDate != null)
                    {
                        Text(text = formatter.format(date))
                    }else{
                        Text(text = "-")
                    }
                }
                Spacer(modifier = Modifier.padding( dimensionResource(id = R.dimen.padding_small)))
                IconButton(onClick = { datePicker.show() }) {
                    Icon(imageVector = Icons.Filled.DateRange, contentDescription = "Elegir Fecha")
                }
            }

        }



    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.Bottom
    ){
        FloatingActionButton(onClick = {
            quantityValidation()
            if(isValidQuantity && foodV2!!.food != null)
                 onSaveClick(quantity,comment)
        }) {
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
fun AddNewFoodScreenSelectFoodPreview(){
    FridgeAppTheme {
        AddNewFoodScreen_SelectFood(
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(id = R.dimen.padding_medium)),
           foodV2 = FoodV2(),
            isValidQuantity = true,
            onQuantityChanged = {},
            quantity = "0",
            quantityValidation = {},
            onAddClicked = {},
            recordFoodSelected = null,
            comment = "",
            onCommentChange = {},
            onSaveClick = {it, it2 ->

            },
            )

    }
}