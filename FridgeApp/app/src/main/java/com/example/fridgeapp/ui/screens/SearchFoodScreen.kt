package com.example.fridgeapp.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon.Companion.Text
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fridgeapp.R
import com.example.fridgeapp.model.Application
import com.example.fridgeapp.model.Food
import com.example.fridgeapp.ui.AppViewModel
import com.example.fridgeapp.ui.theme.FridgeAppTheme
import com.google.gson.Gson
import java.io.File
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchFoodScreen(
    modifier: Modifier = Modifier,
    foods: Set<String>,
    onFoodClick: (String)->Unit

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
            ElevatedButton(
                onClick = { /*TODO*/ }
            ) {
                Text(text = stringResource(R.string.categorias))
            }
        }

        LazyColumn(
            modifier = modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,

        ) {
            items(foods.toList()) { food ->
                Card(
                    modifier = modifier
                        .height(dimensionResource(id = R.dimen.buttom_height_normal)),
                    onClick = {onFoodClick(food)}

                ) {
                    Text(
                        text = food,
                        modifier = modifier,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }


    }
}

/*
@Preview
@Composable
fun SearchFoodScreenPreview(){
    FridgeAppTheme {
        SearchFoodScreen(
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(id = R.dimen.padding_medium)),
            foods  = setOf(Food("Manzana","Fruta", 1),Food("Pera","Fruta",2)),
            onFoodClick={}
        )
    }
}*/