package com.example.fridgeapp.ui.screens


import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.fridgeapp.R
import com.example.fridgeapp.components.PersonalizatedDialog
import com.example.fridgeapp.data.CategoryEntity
import com.example.fridgeapp.data.ContainerEntity
import com.example.fridgeapp.data.FoodRecordEntity
import com.example.fridgeapp.data.SectionEntity
import com.example.fridgeapp.data.SectionRecordEntity
import com.example.fridgeapp.ui.theme.FridgeAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminContainerScreen(
    modifier: Modifier = Modifier,
    containers: List<ContainerEntity>,
    deleteAvailable: Boolean = true,
    isValidContainer: (String)->Boolean,
    isError: Boolean,
    addContainer: (String)->Unit,
    onContainerClicked: (ContainerEntity)->Unit,
    delete: (Long)->Unit,
    reload: ()->Unit,

){

    var addContainer by remember { mutableStateOf(false) }
    var text by remember {
        mutableStateOf("")
    }
    var containerAdded by remember{ mutableStateOf(false) }
    var createContainerStep by remember{ mutableIntStateOf(0) }
    var delete by remember{ mutableStateOf(false) }
    var containerDeleted by remember{ mutableStateOf(false) }
    var containerId:Long by remember{ mutableLongStateOf(-1) }


    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,

        ){
        LazyColumn {
            items(containers){container->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                    ,
                    onClick = {
                        onContainerClicked(container)
                    }
                ) {

                    Row (
                        modifier = modifier,
                        verticalAlignment = Alignment.CenterVertically

                    ){
                        Text(text =container.name)

                        if(deleteAvailable){
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.End
                            ) {
                                IconButton(onClick = {
                                    containerId = container.id!!
                                    delete = !delete
                                }) {
                                    Icon(Icons.Filled.Delete, contentDescription = "Editar")
                                }
                            }
                        }


                    }


                }
                Spacer(modifier = Modifier. height(dimensionResource(id = R.dimen.padding_small)))
            }
        }
    }
    Column(modifier = modifier,
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.Bottom){
        FloatingActionButton(onClick = { addContainer = !addContainer}) {
            Icon(Icons.Filled.Add,contentDescription = "Añadir nuevo elemento")
        }
    }

    if(addContainer){
        PersonalizatedDialog(
            onAcceptClick = {
                if(isValidContainer(text) && !text.isBlank()){
                    addContainer(text)
                    text= ""
                    addContainer = false
                    containerAdded = true
                    createContainerStep = 0
                }
            },
            title = "AÑADIR CONTENEDOR",
            text = {
                Column {
                        Text("Introduce el nombre del nuevo contenedor")
                        OutlinedTextField(
                            value = text,
                            onValueChange = {
                                text = it.uppercase()
                            },
                            singleLine = true,
                        )
                        if (isError) {
                            Text(
                                text = "Ya existe un contenedor con ese nombre",
                                color = MaterialTheme.colorScheme.error,
                            )
                        }

                }

            },
            onDismiss = {
                text = ""
                createContainerStep = 0
                addContainer = !addContainer

            },

        )
    }

    if(delete){
        PersonalizatedDialog(
            onAcceptClick = {
                containerDeleted = true
                delete = !delete
                delete(containerId)

            },
            title = "ELIMINAR CONTENEDOR",
            text = {
                Text("¿Desea borrar el contenedor y todos sus contenidos?")
            },
            onDismiss = {
                delete = !delete
            }

        )
    }
    Column(modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom){


        if (containerAdded){
            Toast.makeText( LocalContext.current ,"Nuevo contenedor añadido", Toast.LENGTH_SHORT).show()
            containerAdded  =false
            reload()
        }
        if (containerDeleted){
            Toast.makeText( LocalContext.current ,"Sección eliminada", Toast.LENGTH_SHORT).show()
            containerDeleted  =false
            reload()
        }
    }


}


/*
@Preview
@Composable
fun FoodRecordEditScreenPreview(){
    FridgeAppTheme {
        FoodRecordEditScreen(
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(id = R.dimen.padding_medium)),
            foodRecord = listOf(FoodRecordEntity(name="LECHUGA", category = "VERDURA")),
            deleteAvailable = true,
            isValidFood = {it ->
                          true
            },
            isError= false,
            addFood= {it1, it2 ->


            },
            reload  ={},
            categorySelected = null,
            selectCategory = {}
        )
    }
}*/