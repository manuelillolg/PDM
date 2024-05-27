package com.example.fridgeapp.ui.screens

import android.widget.Toast
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
import com.example.fridgeapp.data.FoodRecordEntity
import com.example.fridgeapp.ui.theme.FridgeAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodRecordEditScreen(
    modifier: Modifier = Modifier,
    foodRecord: List<FoodRecordEntity>,
    deleteAvailable: Boolean = true,
    isValidFood: (String)->Boolean,
    isError: Boolean,
    addFood: (String, String)->Unit,
    categorySelected: CategoryEntity?,
    selectCategory: (CategoryEntity)->Unit,
    categoryList: List<CategoryEntity>,
    isValidCategory: (String)->Boolean,
    isErrorCategory: Boolean,
    addCategory: (String)->Unit,
    refreshCategories: ()->Unit,
    reload: ()->Unit,
    delete: (Long)->Unit
){

    var addFoodRecord by remember { mutableStateOf(false) }
    var text by remember {
        mutableStateOf("")
    }
    var foodAdded by remember{ mutableStateOf(false) }
    var createFoodStep by remember{ mutableIntStateOf(0) }
    var visibleAccept by remember{ mutableStateOf(false) }

    var deletePressed by remember{ mutableStateOf(false) }
    var foodId by remember{ mutableLongStateOf(-1) }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,

    ){
        LazyColumn {
           items(foodRecord){foodRecord->
               Card(
                   modifier = Modifier
                       .fillMaxWidth()
                       .wrapContentHeight()
                       ,
               ) {

                   Row (
                       modifier = modifier,
                       verticalAlignment = Alignment.CenterVertically
                       
                   ){
                       Text(text =foodRecord.name)

                       if(deleteAvailable){
                           Column(
                               modifier = Modifier.fillMaxWidth(),
                               horizontalAlignment = Alignment.End
                           ) {
                               IconButton(onClick = {
                                   deletePressed = !deletePressed
                                   foodId = foodRecord.id!!
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
        FloatingActionButton(onClick = { addFoodRecord = !addFoodRecord}) {
            Icon(Icons.Filled.Add,contentDescription = "Añadir nuevo elemento")
        }
    }

    if(addFoodRecord){
        PersonalizatedDialog(
            onAcceptClick = {
                if(isValidFood(text)){
                    addFood(text, categorySelected!!.name)
                    text= ""
                    addFoodRecord = false
                    foodAdded = true
                    createFoodStep = 0
                }
            },
            title = "AÑADIR ALIMENTO",
            text = {
               when(createFoodStep) {
                    0 -> Column {
                            Text("Introduce el nombre del nuevo alimento")
                            OutlinedTextField(
                                value = text,
                                onValueChange = {
                                    text = it.uppercase()
                                },
                                singleLine = true,
                            )
                            if (isError) {
                                Text(
                                    text = "Ya existe un alimento con ese nombre",
                                    color = MaterialTheme.colorScheme.error,
                                )
                            }
                            Button(
                                onClick = {
                                    if(isValidFood(text)){
                                        createFoodStep += 1
                                    }
                                }
                            ) {
                                Text("SELECCIONAR CATEGORIA")
                            }
                        }
                   1 -> EditCategoryScreen(
                       modifier = modifier,
                       categoryList = categoryList,
                       isValidCategory = { isValidCategory(it) },
                       isError = isErrorCategory,
                       addCategory = { addCategory (it)},
                       reload = {refreshCategories() },
                        onCategoryClicked = {
                            selectCategory(it)
                            visibleAccept = true
                            createFoodStep+=1
                        },
                        deleteAvailable = false,
                       onDelete = {}

                   )
                   2->Column(){
                       Text("Va a añadir ${text} de tipo ${categorySelected!!.name}")
                   }

                }

            },
            onDismiss = {
                text = ""
                addFoodRecord = !addFoodRecord
            },
            visibleAccept = visibleAccept
        )

    }
    if(deletePressed){
        PersonalizatedDialog(
            onAcceptClick = {
                deletePressed = !deletePressed
                delete(foodId)
                reload()

            },
            title = "ELIMINAR ALIMENTO",
            text = {
                Text("¿Desea borrar el alimento?")
            },
            onDismiss = {
                deletePressed = !deletePressed
            }

        )
    }
    Column(modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom){


        if (foodAdded){
            Toast.makeText( LocalContext.current ,"Nuevo aimento añadido", Toast.LENGTH_SHORT).show()
            foodAdded  =false
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