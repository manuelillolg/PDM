package com.example.fridgeapp.ui.screens

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.example.fridgeapp.R
import com.example.fridgeapp.data.ContainerEntity
import com.example.fridgeapp.data.SectionEntity
import com.example.fridgeapp.ui.theme.FridgeAppTheme

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AddNewFoodScreen_Section(
    modifier:Modifier = Modifier,
    possibleSections: List<SectionEntity>,
    onSectionValueChanged: (SectionEntity)->Unit
){

    Column(
        modifier = modifier
    ) {

        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()

        ) {
            Text(stringResource(R.string.selecciona_una_seccion))
        }

        LazyColumn(
            modifier = modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {
            items(possibleSections) { container ->
                Card(
                    modifier = modifier
                        .height(dimensionResource(id = R.dimen.buttom_height_normal)),
                    onClick = {
                        onSectionValueChanged(container)
                    }

                ) {
                    Text(
                        text = container.name,
                        modifier = modifier,
                        textAlign = TextAlign.Center
                    )
                }
            }
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
fun AddNewFoodScreenSectionPreview(){
    FridgeAppTheme {
        AddNewFoodScreen_Section(
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(id = R.dimen.padding_medium)),
            possibleSections = listOf(SectionEntity(name="Nevera"),SectionEntity(name="Congelador")),
            onSectionValueChanged = {},

        )
    }
}