package com.example.fridgeapp.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.fridgeapp.data.SectionEntity
import com.example.fridgeapp.data.SectionRecordEntity
import com.example.fridgeapp.ui.theme.FridgeAppTheme
import kotlinx.coroutines.runBlocking

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminSectionsOfContainerScreen(
    modifier: Modifier = Modifier,
    sectionList: List<SectionEntity>,
    deleteAvailable: Boolean = true,
    isValidSection: (String)->Boolean,
    addSection: (String)->Unit,
    reload : ()->Unit,
    sectionRecorList: List<SectionRecordEntity>,
    isErrorSection: Boolean,
    reloadSectionRecordList: ()->Unit,
    updateSectionsOfContainer: (SectionRecordEntity)->Unit,
    delete: (Long)->Unit
){
    var addSection by remember { mutableStateOf(false) }
    var text by remember {
        mutableStateOf("")
    }
    var sectionAdded by remember{ mutableStateOf(false) }
    var delete by remember{ mutableStateOf(false) }
    var sectionDeleted by remember{ mutableStateOf(false) }
    var sectionId:Long by remember{ mutableLongStateOf(-1) }
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,

        ){
        if(sectionList.isNotEmpty()) {
            LazyColumn {
                items(sectionList) { section ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                    ) {

                        Row(
                            modifier = modifier,
                            verticalAlignment = Alignment.CenterVertically

                        ) {
                            Text(text = section.name)

                            if (deleteAvailable && sectionList.size > 1) {
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalAlignment = Alignment.End
                                ) {
                                    IconButton(onClick = {
                                        sectionId = section.id!!
                                        delete = !delete
                                        Log.d("TAGGG", sectionList.toString())
                                        Log.d("TAGGG", sectionId.toString())

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
        }else{
            Text(text="NO HAY NINGÚN CONTENEDOR")
        }
    }
    Column(modifier = modifier,
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.Bottom){
        FloatingActionButton(onClick = {
            addSection = !addSection
            isValidSection("")
            reloadSectionRecordList()
        }) {
            Icon(Icons.Filled.Add,contentDescription = "Añadir nuevo elemento")
        }
    }

    if(addSection){
        PersonalizatedDialog(
            onAcceptClick = {
                if(isValidSection(text)){
                    addSection(text)
                    text= ""
                    addSection = false
                    sectionAdded = true
                }
            },
            title = "AÑADIR SECCIÓN",
            text = {
                AdminSectionsScreen(
                    modifier = modifier,
                    sectionList = sectionRecorList,
                    isValidSection = { isValidSection(it) },
                    isError = isErrorSection,
                    addSection = {
                        addSection(it)
                    },
                    reload = { reloadSectionRecordList() },
                    deleteAvailable = false,
                    onSecionClicked ={
                        updateSectionsOfContainer(it)
                        text= ""
                        addSection = false
                        sectionAdded = true
                    },
                    onDelete = {}
                )

            },
            visibleAccept = false,
            onDismiss = {
                text = ""
                addSection = !addSection
            }

        )
    }

    if(delete){
        PersonalizatedDialog(
            onAcceptClick = {
                sectionDeleted = true
                delete = !delete
                delete(sectionId)

            },
            title = "ELIMINAR SECCIÓN",
            text = {
                Text("¿Desea borrar la sección y todos sus contenidos?")
            },
            onDismiss = {
                delete = !delete
            }

        )
    }




    Column(modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom){


        if (sectionAdded){
            Toast.makeText( LocalContext.current ,"Nueva sección añadida", Toast.LENGTH_SHORT).show()
            sectionAdded  =false
            reload()
        }
        if (sectionDeleted){
            Toast.makeText( LocalContext.current ,"Sección eliminada", Toast.LENGTH_SHORT).show()
            sectionDeleted  =false
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