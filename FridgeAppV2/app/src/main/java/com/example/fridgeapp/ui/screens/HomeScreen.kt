package com.example.fridgeapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fridgeapp.R
import com.example.fridgeapp.ui.AppViewModel
import com.example.fridgeapp.ui.theme.FridgeAppTheme

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onSearchFoodButtonClick:  () -> Unit,
    onAddFoodButtonClick: ()-> Unit,
    onFindFoodByContainerClick: ()->Unit
){
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {

            ElevatedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(id = R.dimen.buttom_height_normal)),
                onClick = onSearchFoodButtonClick,
            ) {
                Column(
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier
                        .wrapContentSize()
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = stringResource(R.string.icono_de_buscar)
                    )
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .wrapContentSize()
                        .weight(10f)
                ) {
                    Text(
                        text = stringResource(R.string.buscar_alimentos)
                    )
                }

            }

            Spacer(modifier = Modifier.height(dimensionResource(id =  R.dimen.space_normal)))

            ElevatedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(id = R.dimen.buttom_height_normal)),
                onClick = { onFindFoodByContainerClick() },
            ) {
                Column(
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier
                        .padding()
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = stringResource(R.string.icono_de_buscar)
                    )
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .wrapContentSize()
                        .weight(10f)
                ) {
                    Text(
                        text = stringResource(R.string.buscar_en_contenedor)
                    )
                }
            }

        }

    }
    Row(
        verticalAlignment = Alignment.Bottom,
        modifier = modifier
            .wrapContentSize(align = Alignment.BottomStart)
            .fillMaxWidth()
    ) {

        

        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier
                .weight(1f)

        ){
            FloatingActionButton(onClick = { onAddFoodButtonClick()}) {
                Icon(imageVector = Icons.Outlined.Add, contentDescription = stringResource(R.string.informacion))
            }
        }

    }
}

@Composable
@Preview
fun HomeScreenPreview(){
    FridgeAppTheme {
        HomeScreen(
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(id = R.dimen.padding_medium)),
            onSearchFoodButtonClick = {},
            onAddFoodButtonClick = {},
            onFindFoodByContainerClick={}
        )
    }
}