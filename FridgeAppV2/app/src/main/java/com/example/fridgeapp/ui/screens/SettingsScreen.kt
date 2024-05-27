package com.example.fridgeapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.fridgeapp.R
import com.example.fridgeapp.ui.theme.FridgeAppTheme

@Composable
fun SettingsScreen(
    modifier : Modifier = Modifier,
    navAddCategory: ()->Unit,
    navAddFood:()->Unit,
    navAdminContainer: ()->Unit,
    navAdminSections: ()->Unit
){
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Button(
            onClick = { navAddFood() },
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(id = R.dimen.buttom_height_normal))
        ) {
            Text(text = "EDITAR LISTA DE ALIMENTOS")
        }
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_small)))

        Button(
            onClick = { navAddCategory() },
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(id = R.dimen.buttom_height_normal))
        ) {
            Text(text = "EDITAR LISTA DE CATEGORIAS")
        }
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_small)))

        Button(
            onClick = { navAdminContainer() },
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(id = R.dimen.buttom_height_normal))
        ) {
            Text(text = "ADMINISTRAR CONTENEDORES")
        }
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_small)))

        Button(
            onClick = { navAdminSections() },
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(id = R.dimen.buttom_height_normal))
        ) {
            Text(text = "ADMINISTRAR SECCIONES")
        }
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_small)))
    }
}

@Preview()
@Composable
fun SettingsScreenPreview(){
    FridgeAppTheme{
        SettingsScreen(
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(id = R.dimen.padding_medium)),
            navAddCategory = {},
            navAddFood = {},
            navAdminContainer = {},
            navAdminSections = {}
        )
    }
}