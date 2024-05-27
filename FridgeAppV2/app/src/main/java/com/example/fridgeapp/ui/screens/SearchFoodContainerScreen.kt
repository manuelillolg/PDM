package com.example.fridgeapp.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.fridgeapp.R
import com.example.fridgeapp.data.ContainerEntity
import com.example.fridgeapp.data.FoodEntity
import com.example.fridgeapp.data.FoodV2
import com.example.fridgeapp.data.SectionEntity
import com.example.fridgeapp.ui.theme.FridgeAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchFoodContainerScreen(
    modifier: Modifier = Modifier,
    containers: List<ContainerEntity>,
    onContainerValueChange: (ContainerEntity)->Unit,
    onFoodClick: (FoodV2)->Unit,
    onFoodDelete:(Long,String)->Unit,
    foods: List<FoodV2>,
    foodSelected: FoodV2,
    updateFood: (FoodV2)->Unit,
    sections: List<SectionEntity>,
    onSectionChange: (SectionEntity)->Unit,
    sectionSelected: SectionEntity,
    newContainerSelected: ContainerEntity,
    onLocationChage: (FoodV2)->Unit,
    step: Int,
    stepUpdate:(Int)->Unit,
    reload:(ContainerEntity)->Unit,
    searchingContainer: ContainerEntity?,
    onSearchingContainerUpdate: (ContainerEntity)->Unit

) {



    when (step) {

        0->    AddNewFoodScreen_Container(
                modifier = modifier,
                possibleContainers = containers,
                onContainerValueChanged = {
                    onContainerValueChange(it)
                    onSearchingContainerUpdate(it)
                    stepUpdate(step+1)
                }
                )
        1-> {
            FoodDetailScreen(
                modifier = modifier,
                foods = foods,
                onFoodClick = {
                    onFoodClick(it)
                    stepUpdate(step+1)
                },
                onFoodDelete = {id, name->
                    onFoodDelete(id,name)
                    reload(searchingContainer!!)
                },
                onEmptyFood = {

                },
                title = false
            )
            BackHandler(onBack = { stepUpdate(step-1) })
        }
        2->{
            EditFoodScreen(
                modifier = modifier,
                food = foodSelected,
                onFoodDelete = { id, name ->
                    onFoodDelete(id,name)
                    stepUpdate(step-1)
                    reload(searchingContainer!!)
                },
                updateFood = {
                    updateFood(it)
                    reload(searchingContainer!!)
                },
                possibleConatiners = containers,
                onContainerChange = onContainerValueChange,
                possibleSections = sections,
                onSectionChange = onSectionChange,
                sectionSelected = sectionSelected,
                containerSelected = newContainerSelected,
                onLocationChange = {onLocationChage(it)},
                navigateBack={
                    stepUpdate(step-1)
                    reload(searchingContainer!!)
                }

            )
        }

    }
}
/*
@Preview
@Composable
fun SearchFoodContainerPreviewScreen(

){
    FridgeAppTheme {
        SearchFoodContainerScreen(
            modifier = Modifier
                .fillMaxSize()
                .padding((dimensionResource(id = R.dimen.padding_medium))),

        )
    }
}*/