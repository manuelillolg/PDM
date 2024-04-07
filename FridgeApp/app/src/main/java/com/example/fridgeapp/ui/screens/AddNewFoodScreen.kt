package com.example.fridgeapp.ui.screens

import android.app.Activity
import android.graphics.drawable.Icon
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.fridgeapp.R
import com.example.fridgeapp.ui.theme.FridgeAppTheme

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AddNewFoodScreen(
    modifier:Modifier = Modifier,
    possibleFoods: Set<String>,
    possibleContainers: Set<String>,
    possibleSections: Set<String>,
    possibleCategories : Set<String>,
    onContainerValueChanged: (String)->Unit,
    onQuantityValueChanged:(String)->Unit,
    quantity: String,
    isQuantityCorrect : Boolean,
    validateQuantity: ()-> Unit,
    onSectionValueChanged:(String)->Unit,
    onFoodSelected:(String)->Unit,
    onCategorySelected:(String)->Unit,
    onAddFoodClick:()->Unit,

    //Argumentos para AlertDialogs
    foodName :String,
    onAcceptNewFood: ()->Boolean,
    onDismissNewFood: () -> Unit,
    isValidNewFood: Boolean,

){
    var expandedFoodChoice by remember { mutableStateOf(false) }
    var expandedContainerChoice by remember { mutableStateOf(false) }
    var expandedSectionChoice by remember { mutableStateOf(false) }
    var expandedCategoryChoice by remember { mutableStateOf(false) }

    var selectedOptionIndex by remember { mutableStateOf(0)}
    var selectedContainerOptionIndex by remember { mutableStateOf(0)}
    var selectedSectionOptionIndex by remember { mutableStateOf(0) }
    var selectedCategoryOptionIndex by remember { mutableStateOf(0) }

    var addNewFood by remember { mutableStateOf(false) }

    val keyboardController = LocalSoftwareKeyboardController.current

    Column (modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Box {
            Button(
                shape = MaterialTheme.shapes.extraSmall,
                onClick = { expandedFoodChoice = !expandedFoodChoice },
                modifier = Modifier
                    .wrapContentSize()
                    .width(dimensionResource(id = R.dimen.buttom_width_normal))
            ) {
                Row {
                    Text(text = possibleFoods.toList()[selectedOptionIndex])
                    if(expandedFoodChoice)
                        Icon(
                            Icons.Filled.KeyboardArrowUp,contentDescription = stringResource(R.string.plegar)
                        )
                    else{
                        Icon(Icons.Filled.KeyboardArrowDown,contentDescription = stringResource(R.string.desplegar),

                            )
                    }
                }
            }

            DropdownMenu(
                expanded = expandedFoodChoice,
                onDismissRequest = { expandedFoodChoice = false },
                modifier = Modifier.fillMaxWidth(),
            ) {
                possibleFoods.forEachIndexed { index, possibleFood ->
                    DropdownMenuItem(
                        text = { Text(text = possibleFood) },
                        onClick = {
                            selectedOptionIndex = index
                            expandedFoodChoice = false
                            onFoodSelected(possibleFood)

                        }
                    )
                }
                DropdownMenuItem(
                    text = {
                        IconButton(onClick = {
                            addNewFood = true
                            onFoodSelected("")

                        }) {
                            Icon(Icons.Filled.Add, stringResource(R.string.a_adir_nuevo_alimento))
                        }
                    },
                    onClick = {


                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.space_normal)))

        Box {
            Button(
                shape = MaterialTheme.shapes.extraSmall,
                onClick = { expandedCategoryChoice = !expandedCategoryChoice },
                modifier = Modifier
                    .wrapContentSize()
                    .width(dimensionResource(id = R.dimen.buttom_width_normal))
            ) {
                Row {
                    Text(text = possibleCategories.toList()[selectedCategoryOptionIndex])
                    if(expandedCategoryChoice)
                        Icon(
                            Icons.Filled.KeyboardArrowUp,contentDescription = stringResource(R.string.plegar)
                        )
                    else{
                        Icon(Icons.Filled.KeyboardArrowDown,contentDescription = stringResource(R.string.desplegar),

                            )
                    }
                }
            }

            DropdownMenu(
                expanded = expandedCategoryChoice,
                onDismissRequest = { expandedCategoryChoice = false },
                modifier = Modifier.fillMaxWidth(),
            ) {
                possibleCategories.forEachIndexed { index, possibleCategory ->
                    DropdownMenuItem(
                        text = { Text(text = possibleCategory) },
                        onClick = {
                            selectedCategoryOptionIndex = index
                            expandedCategoryChoice = false
                            onCategorySelected(possibleCategory)

                        }
                    )
                }
                DropdownMenuItem(
                    text = {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(Icons.Filled.Add, stringResource(R.string.a_adir_nuevo_alimento))
                        }
                    },
                    onClick = {
                        selectedCategoryOptionIndex = 0
                        expandedCategoryChoice = false
                        // onContainerValueChanged(container)

                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.space_normal)))

        Box {
            Button(
                shape = MaterialTheme.shapes.extraSmall,
                onClick = { expandedContainerChoice = !expandedContainerChoice },
                modifier = Modifier
                    .wrapContentSize()
                    .width(dimensionResource(id = R.dimen.buttom_width_normal))
            ) {
                Row (horizontalArrangement = Arrangement.Center){
                    Text(text = possibleContainers.toList()[selectedContainerOptionIndex])

                    if(expandedContainerChoice)
                        Icon(
                            Icons.Filled.KeyboardArrowUp,contentDescription = stringResource(R.string.plegar)
                        )
                    else{
                        Icon(Icons.Filled.KeyboardArrowDown,contentDescription = stringResource(R.string.desplegar),

                        )
                    }
                }

            }

            DropdownMenu(
                expanded = expandedContainerChoice,
                onDismissRequest = { expandedContainerChoice = false },
                modifier = Modifier.fillMaxWidth(),
            ) {
                possibleContainers.forEachIndexed { index, possibleContainer ->
                    DropdownMenuItem(
                        text = { Text(text = possibleContainer) },
                        onClick = {
                            selectedContainerOptionIndex = index
                            expandedContainerChoice = false
                            onContainerValueChanged(possibleContainer)

                        }
                    )
                }


            }
        }
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.space_normal)))

        Box {
            Button(
                shape = MaterialTheme.shapes.extraSmall,
                onClick = { expandedSectionChoice = !expandedSectionChoice },
                modifier = Modifier
                    .wrapContentSize()
                    .width(dimensionResource(id = R.dimen.buttom_width_normal))
            ) {
                Row {
                    Text(text = possibleSections.toList()[selectedSectionOptionIndex])
                    if(expandedSectionChoice)
                        Icon(
                            Icons.Filled.KeyboardArrowUp,contentDescription = stringResource(R.string.plegar)
                        )
                    else{
                        Icon(Icons.Filled.KeyboardArrowDown,contentDescription = stringResource(R.string.desplegar),

                            )
                    }
                }
            }

            DropdownMenu(
                expanded = expandedSectionChoice,
                onDismissRequest = { expandedSectionChoice = false },
                modifier = Modifier.fillMaxWidth(),
            ) {
                possibleSections.forEachIndexed { index, possibleSection ->
                    DropdownMenuItem(
                        text = { Text(text = possibleSection) },
                        onClick = {
                            selectedSectionOptionIndex = index
                            expandedSectionChoice = false
                            onSectionValueChanged(possibleSection)

                        }
                    )
                }
                DropdownMenuItem(
                    text = {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(Icons.Filled.Add, stringResource(R.string.a_adir_nuevo_alimento))
                        }
                    },
                    onClick = {
                        selectedSectionOptionIndex = 0
                        expandedSectionChoice = false
                        // onContainerValueChanged(container)

                    }
                )

            }
        }
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.space_normal)))


        OutlinedTextField(
            value = quantity.toString(),
            label={Text(stringResource(id = R.string.cantidad))},
            singleLine = true,
            shape = MaterialTheme.shapes.large,

            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                disabledContainerColor = MaterialTheme.colorScheme.surface,
            ),
            onValueChange =   {

                    onQuantityValueChanged(it)

            },

            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    validateQuantity()
                    keyboardController?.hide()
                }
            ),
            isError=!isQuantityCorrect,


        )

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.space_normal)))

        Button(
            shape = MaterialTheme.shapes.extraSmall,
            onClick = onAddFoodClick,
            modifier = Modifier
                .wrapContentSize()

        ) {
            Text(text= stringResource(R.string.a_adir))
        }

    }


//Alert Dialogs para añadir elementos a las listas

    //Añadir nuevo alimento
    if(addNewFood){
        AddNewItem(
            onAcceptClick = {

                addNewFood = !onAcceptNewFood()
            },
            title = stringResource(id = R.string.a_adir_alimento) ,
            name = foodName,
            onChangeName = {onFoodSelected(it)},
            onDismiss = {
                addNewFood = false
                keyboardController?.hide()
                onDismissNewFood()
            },
            isValid = isValidNewFood
        )


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
fun AddNewFoodScreenPreview(){
    FridgeAppTheme {
        AddNewFoodScreen(
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(id = R.dimen.padding_medium)),
            possibleFoods = setOf("Lechuga","Tomate","Cereza"),
            possibleContainers = setOf("Nevera","Congelador"),
            possibleSections = setOf("Cajón 1", "Cajón 2", "Cajón 3"),
            possibleCategories = setOf("Verdura","Fruta"),
            onContainerValueChanged = {},
            onQuantityValueChanged = {},
            quantity = "0",
            isQuantityCorrect = true,
            validateQuantity = {},
            onSectionValueChanged = {},
            onFoodSelected = {},
            onCategorySelected = {},
            onAddFoodClick = {},
            foodName = "",
            onAcceptNewFood = {true},
            onDismissNewFood = {},
            isValidNewFood = true,
        )
    }
}