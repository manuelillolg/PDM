package com.example.fridgeapp.ui.screens

import android.util.Log
import android.view.ContextMenu
import android.view.Menu
import android.widget.AutoCompleteTextView
import android.widget.TextView
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fridgeapp.R
import com.example.fridgeapp.model.Container
import com.example.fridgeapp.model.Food
import com.example.fridgeapp.model.Route
import com.example.fridgeapp.model.Section
import com.example.fridgeapp.ui.theme.FridgeAppTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodDetailScreen(
    modifier: Modifier = Modifier,
    food : String = " ",
    containers: List<String>,
    foodListByContainer: List<Pair<Food, Route>>,
    onContainerValueChanged : (String)->Unit,
    onFoodClick: (Pair<Food,Route>)->Unit
){


    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ){


        Row(

        ) {
            Text(
                text = food.uppercase(),
                style = MaterialTheme.typography.titleLarge
            )
        }

        Spacer(
            modifier = Modifier
                .height(dimensionResource(id = R.dimen.space_normal))
        )

        var expanded by remember { mutableStateOf(false) }
        var selectedOptionIndex by remember { mutableStateOf(0)
        }

        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Button(
                shape = MaterialTheme.shapes.extraSmall,
                onClick = { expanded = !expanded },
                modifier = Modifier
                    .wrapContentSize()
                    .width(dimensionResource(id = R.dimen.buttom_width_normal))
            ) {
                Text(text = containers[selectedOptionIndex])
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth(),
            ) {
                containers.forEachIndexed { index, container ->
                    DropdownMenuItem(
                        text = { Text(text = container) },
                        onClick = {
                            selectedOptionIndex = index
                            expanded = false
                            onContainerValueChanged(container)

                        }
                    )
                }
            }
        }

        Spacer(
            modifier = Modifier
                .height(dimensionResource(id = R.dimen.space_normal))
        )

        LazyColumn(
            modifier = Modifier
                .padding(vertical = dimensionResource(id = R.dimen.padding_small))
        ) {
            items(foodListByContainer.toList()) {food ->
                Card(
                    modifier = modifier
                        .wrapContentSize()
                        .fillMaxWidth(),
                    onClick = {onFoodClick(food)}

                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .padding(dimensionResource(id = R.dimen.padding_small))
                    ) {
                        Column(
                            modifier = Modifier
                                .wrapContentSize()

                        ) {
                            Row {
                                Text(
                                    text = stringResource(R.string.ubicacion),
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(
                                    modifier = Modifier
                                        .width(dimensionResource(id = R.dimen.padding_small))
                                )
                                Text(text = food.first.location)
                            }
                            Spacer(
                                modifier = Modifier
                                    .height(dimensionResource(id = R.dimen.padding_small))
                            )

                            Row {
                                Text(
                                    text = stringResource(R.string.fecha_de_compra),
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(
                                    modifier = Modifier
                                        .width(dimensionResource(id = R.dimen.padding_small))
                                )
                                Text(text =food.first.category)

                            }
                            Spacer(
                                modifier = Modifier
                                    .height(dimensionResource(id = R.dimen.padding_small))
                            )

                            Row {
                                Text(
                                    text = stringResource(R.string.fecha_de_caducidad),
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(
                                    modifier = Modifier
                                        .width(dimensionResource(id = R.dimen.padding_small))
                                )
                                Text(text = food.first.category)

                            }

                            Spacer(
                                modifier = Modifier
                                    .height(dimensionResource(id = R.dimen.padding_small))
                            )

                            Row {
                                Text(
                                    text = stringResource(R.string.cantidad),
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(
                                    modifier = Modifier
                                        .width(dimensionResource(id = R.dimen.padding_small))
                                )
                                Text(text = food.first.quantity.toString())

                            }

                            Spacer(
                                modifier = Modifier
                                    .height(dimensionResource(id = R.dimen.padding_small))
                            )

                            Row {
                                Text(
                                    text = stringResource(R.string.categoria),
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(
                                    modifier = Modifier
                                        .width(dimensionResource(id = R.dimen.padding_small))
                                )
                                Text(text = food.first.category)

                            }


                        }
                    }
                }
            }
        }
    }
}
/*
@Composable
@Preview

fun FoodDetailPreview(){
    FridgeAppTheme(){
        FoodDetailScreen(food ="Lechuga",containers = listOf(
            Container(name="frigo1",sections = listOf(Section(name="Seccion1", foods=listOf(
                Food("Manzana","Fruta", 1),
                Food("Pera","Fruta",2)
            )))),
            Container(name="frig2",sections = listOf(Section(name="Seccion1", foods=listOf(
                Food("Lechuga","Verdura", 1),
                Food("Pera","Fruta",2)
            ))))
        ),
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(id = R.dimen.padding_medium))
        )
    }
}*/