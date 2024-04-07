package com.example.fridgeapp

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.fridgeapp.model.Food
import com.example.fridgeapp.model.Route
import com.example.fridgeapp.ui.AppViewModel
import com.example.fridgeapp.ui.screens.AddNewFoodScreen
import com.example.fridgeapp.ui.screens.EditFoodScreen
import com.example.fridgeapp.ui.screens.FoodDetailScreen
import com.example.fridgeapp.ui.screens.HomeScreen
import com.example.fridgeapp.ui.screens.SearchFoodScreen


enum class FridgeAppScreen(@StringRes val title: Int){
    Home(title = R.string.inicio),
    FindFood(title= R.string.lista_de_alimentos),
    FoodDetail(title=R.string.detalle),
    FoodEdit(title=R.string.edicion_de_alimento),
    AddFood(title=R.string.a_adir_alimento)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FridgeAppBar(
    currentScreen: FridgeAppScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
)
{
    CenterAlignedTopAppBar(
        title = {
            Text(stringResource(currentScreen.title), fontWeight = FontWeight.Bold)
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        //contentDescription = stringResource(R.string.back_button)
                        contentDescription = "Back"
                    )
                }
            }
        },
        actions={
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Filled.Settings, contentDescription = stringResource(R.string.ajustes) )
            }
        }
    )
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun FridgeApp(
    jsonString: String? = "",
    navController: NavHostController = rememberNavController(),
    viewModel: AppViewModel = viewModel(),
    context : Context?
){

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = FridgeAppScreen.valueOf(
        backStackEntry?.destination?.route ?: FridgeAppScreen.Home.name
    )

    Scaffold(
        topBar = {
            FridgeAppBar(
                canNavigateBack = navController.previousBackStackEntry != null,
                currentScreen = currentScreen,
                navigateUp = { navController.navigateUp()}
            )
        }
    ) { innerPadding ->
        viewModel.initModel(jsonString.toString(), context!!)
        val uiState by viewModel.appState.collectAsState()

        NavHost(
            navController = navController,
            startDestination = FridgeAppScreen.Home.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(
                route = FridgeAppScreen.Home.name
            ) {
                HomeScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding((dimensionResource(id = R.dimen.padding_medium))),
                    onSearchFoodButtonClick = {
                        viewModel.getAllFoods()
                        navController.navigate(FridgeAppScreen.FindFood.name)
                    },
                    onAddFoodButtonClick ={
                        viewModel.getAllPossibleFoods()
                        viewModel.getAllPossibleCategories()
                        viewModel.getAllContainers()
                        viewModel.getAllSectionsOfContainer()
                        navController.navigate(FridgeAppScreen.AddFood.name)
                    }
                )
            }

            composable(
                route = FridgeAppScreen.FindFood.name
            ) {
                SearchFoodScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding((dimensionResource(id = R.dimen.padding_medium))),
                    foods = uiState.allFoods,
                    onFoodClick={
                        viewModel.updateFoodSelected(it)
                        viewModel.getContainersWithFoodSelected()
                        viewModel.getFoodOfContainer()
                        navController.navigate(FridgeAppScreen.FoodDetail.name)
                    }
                )
            }
            
            composable(
                route = FridgeAppScreen.FoodDetail.name
            ){
                FoodDetailScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding((dimensionResource(id = R.dimen.padding_medium))),
                    food = uiState.foodSelected,
                    containers = uiState.containersWithFoodName.toList(),
                    foodListByContainer = uiState.foodByContainer,
                    onContainerValueChanged ={
                        viewModel.updateContainerSelected(it)
                        viewModel.getFoodOfContainer()
                    },
                    onFoodClick = {
                        viewModel.updateSelectedFoodEdit(it)
                        //viewModel.updateModel()
                        navController.navigate(FridgeAppScreen.FoodEdit.name)
                    }
                )
            }

            composable(
                route = FridgeAppScreen.FoodEdit.name
            ){
                EditFoodScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding((dimensionResource(id = R.dimen.padding_medium))),
                    food = uiState.foodEdited!!,
                    onEditNameClick = {
                        viewModel.changeFoodName(it)
                        viewModel.writeJson()
                    },
                    provisionalFoodName = uiState.provisionalFoodNameEdited,
                    onChangeName = {
                        viewModel.updateProvisionalFoodNameEdit(it)
                    },
                    onNameDismiss = {
                        viewModel.updateProvisionalFoodNameEdit(uiState.foodEdited!!.first.name)
                    }
                )
            

            }

            composable(
                route = FridgeAppScreen.AddFood.name
            ){
                AddNewFoodScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding((dimensionResource(id = R.dimen.padding_medium))),
                    possibleFoods = uiState.allFoods,
                    possibleContainers = uiState.allContainers,
                    possibleSections = uiState.sectionsOfContainer ,
                    possibleCategories = uiState.allCategories,
                    onContainerValueChanged = {
                        viewModel.updateContainerSelected(it)
                        viewModel.getAllSectionsOfContainer()
                    },
                    onQuantityValueChanged = {
                        viewModel.updateProvisionalQuantity(it)
                    },
                    quantity = uiState.provisionalQuantity,
                    isQuantityCorrect= uiState.isQuantityCorrect,
                    validateQuantity = { viewModel.validateQuantity() },
                    onCategorySelected = {
                        viewModel.updateCategory(it)
                    },
                    onFoodSelected = {
                        viewModel.updateFoodSelected(it)
                    },
                    onSectionValueChanged = {
                        viewModel.updateSectionNameSelected(it)
                    },
                    onAddFoodClick={
                        var added: Boolean = viewModel.addFood()

                        if(added){
                            navController.navigateUp()
                        }
                        //navController.popBackStack(FridgeAppScreen.Home.name,inclusive = true)
                    },

                    foodName = uiState.foodSelected,
                    onAcceptNewFood = {
                        viewModel.addNewFood()
                    },
                    onDismissNewFood = {
                       viewModel.cancelNewFood()
                    },
                    isValidNewFood = uiState.isValidFood

                )


            }
        }
    }
}


/*
@Preview
@Composable
fun FridgeAppPreview(){
    FridgeApp()
}
*/