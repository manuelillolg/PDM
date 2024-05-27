package com.example.fridgeapp.ui.screens

import android.util.Log
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import com.example.fridgeapp.R
import com.example.fridgeapp.components.PersonalizatedDialog
import com.example.fridgeapp.data.CategoryEntity
import com.example.fridgeapp.data.FoodRecordEntity
import com.example.fridgeapp.ui.theme.FridgeAppTheme
import kotlinx.coroutines.runBlocking

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditCategoryScreen(
    modifier: Modifier = Modifier,
    categoryList: List<CategoryEntity>,
    deleteAvailable: Boolean = true,
    isValidCategory: (String)->Boolean,
    isError: Boolean,
    addCategory: (String)->Unit,
    reload : ()->Unit,
    onDelete: (Long)->Unit,
    onCategoryClicked: (CategoryEntity)->Unit
){
    var addCategory by remember { mutableStateOf(false) }
    var text by remember {
        mutableStateOf("")
    }
    var categoryAdded by remember{ mutableStateOf(false) }
    var delete by remember{ mutableStateOf(false) }
    var categoryId by remember{ mutableLongStateOf(-1) }
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,

        ){
        LazyColumn {
            items(categoryList){category->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                    ,
                    onClick = {
                        if(!deleteAvailable){
                            onCategoryClicked(category)
                        }
                    }
                ) {

                    Row (
                        modifier = modifier,
                        verticalAlignment = Alignment.CenterVertically

                    ){
                        Text(text =category.name)

                        if(deleteAvailable){
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.End
                            ) {
                                IconButton(onClick = {
                                    delete = !delete
                                    categoryId =category.id!!
                                }) {
                                    Icon(Icons.Filled.Delete, contentDescription = "Editar")
                                }
                            }
                        }


                    }


                }
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_small)))
            }
        }
    }
    Column(modifier = modifier,
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.Bottom){
        FloatingActionButton(onClick = {
            addCategory = !addCategory
            isValidCategory("")
        }) {
            Icon(Icons.Filled.Add,contentDescription = "Añadir nuevo elemento")
        }
    }

    if(addCategory){
        PersonalizatedDialog(
            onAcceptClick = {
               if(isValidCategory(text)){
                    addCategory(text)
                    text= ""
                    addCategory = false
                    categoryAdded = true
                }
            },
            title = "AÑADIR CATEGORIA",
            text = {
                Column {
                    Text("Introduce el nombre de la nueva categoría")
                    OutlinedTextField(
                        value = text,
                        onValueChange = {
                            text = it.uppercase()
                        },
                        singleLine = true,
                    )
                    if (isError) {
                        Text(
                            text = "Ya existe una categoría con ese nombre",
                            color = MaterialTheme.colorScheme.error,
                        )
                    }
                }

            },
            onDismiss = {
                text = ""
                addCategory = !addCategory
            }
        )
    }

    if(delete) {
        PersonalizatedDialog(
            onAcceptClick = {
                delete = !delete
                onDelete(categoryId)
                reload()

            },
            title = "ELIMINAR CATEGORIA",
            text = {
                Text("¿Desea borrar la categoría?")
            },
            onDismiss = {
                delete = !delete
            }

        )
    }


    Column(modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom){


        if (categoryAdded){
            Toast.makeText( LocalContext.current ,"Nueva categoría añadida", Toast.LENGTH_SHORT).show()
            categoryAdded  =false
            reload()
        }
    }

}
/*
@Preview
@Composable
fun EditCategoryScreenPreview(){
    FridgeAppTheme {
        EditCategoryScreen(
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(id = R.dimen.padding_medium)),
            categoryList = listOf(CategoryEntity(name="VERDURA")),
            isError = true,
           // isValidCategory = {return false},
            addCategory = {},
            reload={}
        )
    }
}*/