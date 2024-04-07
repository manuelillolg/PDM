package com.example.fridgeapp.ui.screens

import android.app.Activity
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.fridgeapp.R
import com.example.fridgeapp.model.Food
import com.example.fridgeapp.model.Route
import com.example.fridgeapp.ui.theme.FridgeAppTheme

@Composable
fun EditFoodScreen(
    modifier : Modifier = Modifier,
    food : Pair<Food,Route>,
    onEditNameClick: (String)->Unit,
    provisionalFoodName: String,
    onChangeName: (String)->Unit,
    onNameDismiss: ()->Unit,

){
    var showEditNameDialog by remember { mutableStateOf(false) }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ){


        Row(

        ) {
            Text(
                text = food.first.name.uppercase(),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier .align(Alignment.CenterVertically)
            )
            Spacer(
                modifier = Modifier
                    .width(dimensionResource(id = R.dimen.padding_small))
            )
            IconButton(onClick = {
                showEditNameDialog = true
            }) {
                Icon(
                    imageVector = Icons.Outlined.Edit,
                    //contentDescription = stringResource(R.string.back_button)
                    contentDescription = stringResource(R.string.editar_nombre_de_alimento)
                )
            }
        }

        Spacer(
            modifier = Modifier
                .height(dimensionResource(id = R.dimen.space_normal))
        )



        //A partir de aquí se encuentran los distintos pop-ups

        if(showEditNameDialog) {
            EditNameDialog(
                onAcceptClick = {
                    onEditNameClick(food.first.name)
                    showEditNameDialog = false
                },
                foodName = food.first.name,
                provisionalFoodName = provisionalFoodName,
                onChangeName = onChangeName,
                onDismiss = {
                    onNameDismiss()
                    showEditNameDialog = false
                }
            )
        }
    }

}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun EditNameDialog(
    onAcceptClick: () -> Unit,
    provisionalFoodName: String,
    foodName: String,
    onChangeName: (String)-> Unit,
    modifier: Modifier = Modifier,
    onDismiss: ()->Unit
) {
    val activity = (LocalContext.current as Activity)

    AlertDialog(
        onDismissRequest = {
            // Dismiss the dialog when the user clicks outside the dialog or on the back
            // button. If you want to disable that functionality, simply use an empty
            // onCloseRequest.
        },
        title = {
            Text(text = stringResource(R.string.editar_nombre),
                textAlign = TextAlign.Center
            )
        },
        text = {
            Column(){
                Text(
                    text = stringResource(
                        R.string.el_nombre_que_elijas_se_aplicar_a_todos_los_alimentos_que_actualmente_se_llaman,
                        foodName
                    ),
                    textAlign = TextAlign.Justify
                )
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_small)))
                OutlinedTextField(
                    value = provisionalFoodName,
                    singleLine = true,
                    shape = MaterialTheme.shapes.large,
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        disabledContainerColor = MaterialTheme.colorScheme.surface,
                    ),
                    onValueChange = onChangeName,

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
fun EditFoodScreenPreview(){
    FridgeAppTheme {

        /* EditFoodScreen(
            food = Pair(
                Food(name ="Lechuga",category="Verdura",quantity = 0),
                Route(0,0,0)
            ),
            onEditNameClick = {},
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(id = R.dimen.padding_medium))
        )*/
    }


}