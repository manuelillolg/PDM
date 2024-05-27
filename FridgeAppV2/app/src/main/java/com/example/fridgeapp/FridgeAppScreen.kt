package com.example.fridgeapp

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
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
import androidx.navigation.navigation
import com.example.fridgeapp.data.CategoryRepository
import com.example.fridgeapp.data.ContainerRepository
import com.example.fridgeapp.data.ContainerSectionRepository
import com.example.fridgeapp.data.FoodRecordRepository
import com.example.fridgeapp.data.FoodRepository
import com.example.fridgeapp.data.FoodV2
import com.example.fridgeapp.data.SectionFoodRepository
import com.example.fridgeapp.data.SectionRecordRepository
import com.example.fridgeapp.data.SectionRepository
import com.example.fridgeapp.model.Food
import com.example.fridgeapp.model.Route
import com.example.fridgeapp.ui.AppViewModel
import com.example.fridgeapp.ui.screens.AddNewFoodScreen_Container
import com.example.fridgeapp.ui.screens.AddNewFoodScreen_Food
import com.example.fridgeapp.ui.screens.AddNewFoodScreen_Section
import com.example.fridgeapp.ui.screens.AddNewFoodScreen_SelectFood
import com.example.fridgeapp.ui.screens.AdminContainerScreen
import com.example.fridgeapp.ui.screens.AdminSectionsOfContainerScreen
import com.example.fridgeapp.ui.screens.AdminSectionsScreen
import com.example.fridgeapp.ui.screens.EditCategoryScreen
import com.example.fridgeapp.ui.screens.EditFoodScreen
import com.example.fridgeapp.ui.screens.FoodDetailScreen
import com.example.fridgeapp.ui.screens.FoodRecordEditScreen
import com.example.fridgeapp.ui.screens.HomeScreen
import com.example.fridgeapp.ui.screens.ListOfFoodScreen
import com.example.fridgeapp.ui.screens.SearchFoodContainerScreen
import com.example.fridgeapp.ui.screens.SearchFoodScreen
import com.example.fridgeapp.ui.screens.SettingsScreen


enum class FridgeAppScreen(@StringRes val title: Int){
    Home(title = R.string.inicio),
    FindFood(title= R.string.lista_de_alimentos),
    FoodDetail(title=R.string.detalle),
    FoodEdit(title=R.string.edicion_de_alimento),
    AddFoodContainer(title=R.string.a_adir_alimento),
    AddFoodSection(title=R.string.a_adir_alimento),
    AddFood(title=R.string.a_adir_alimento),
    SelectFood(title=R.string.a_adir_alimento),
    ListFood(title=R.string.a_adir_alimento),
    Settings(title=R.string.ajustes),
    AddCategory(title =(R.string.a_adir_categoria)),
    FoodRecordEdit(title=(R.string.historial_de_alimentos)),
    FindFoodByContainer(title=R.string.lista_de_alimentos),
    AdminContainers(title =(R.string.administraci_n_de_contenedores)),
    AdminSections(title = (R.string.administraci_n_de_secciones)),
    AdminSectionsOfContainer(title = (R.string.administraci_n_de_contenedores))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FridgeAppBar(
    currentScreen: FridgeAppScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    navigateSettings: ()->Unit,
    modifier: Modifier = Modifier,
    canAccessSettings: Boolean
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
            if(canAccessSettings){
                IconButton(onClick = { navigateSettings() }) {
                    Icon(
                        imageVector = Icons.Filled.Settings,
                        contentDescription = stringResource(R.string.ajustes)
                    )
                }}
        }
    )
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun FridgeApp(
    jsonString: String? = "",
    navController: NavHostController = rememberNavController(),
    viewModel: AppViewModel = viewModel(),
    context : Context?,
    containerRepository : ContainerRepository,
    sectionRepository : SectionRepository,
    containerSectionRepository : ContainerSectionRepository,
    sectionRecordRepository : SectionRecordRepository,
    foodRecordRepository : FoodRecordRepository,
    foodRepository : FoodRepository,
    sectionFoodRepository: SectionFoodRepository,
    categoryRepository: CategoryRepository
){

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = FridgeAppScreen.valueOf(
        backStackEntry?.destination?.route ?: FridgeAppScreen.Home.name
    )

    Scaffold(
        topBar = {
            FridgeAppBar(
                canNavigateBack =false,
                currentScreen = currentScreen,
                navigateUp = { navController.navigateUp()},
                navigateSettings = {navController.navigate(FridgeAppScreen.Settings.name)},
                canAccessSettings = currentScreen.name == FridgeAppScreen.Home.name
            )
        }
    ) { innerPadding ->
        viewModel.initModel(
            jsonString.toString(),
            context!!,
            containerRepository = containerRepository,
            sectionRepository = sectionRepository,
            containerSectionRepository = containerSectionRepository,
            sectionRecordRepository = sectionRecordRepository,
            foodRecordRepository = foodRecordRepository,
            foodRepository = foodRepository,
            sectionFoodRepository = sectionFoodRepository,
            categoryRepository = categoryRepository
        )
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
                        viewModel.getAllContainers()
                        //viewModel.getAllSectionsOfContainer()
                        navController.navigate(FridgeAppScreen.AddFoodContainer.name)
                    },
                    onFindFoodByContainerClick = {
                        viewModel.getAllContainers()
                        navController.navigate(FridgeAppScreen.FindFoodByContainer.name)
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
                        viewModel.getFoodByName(it)
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
                    foods = uiState.allFoodsByName,

                    onFoodClick = {
                        viewModel.updateSelectedFoodEdit(it)
                        //viewModel.updateModel()
                        viewModel.updateProvisionalQuantity(it.quantity)
                        viewModel.getAllContainers()
                        navController.navigate(FridgeAppScreen.FoodEdit.name)

                    },
                    onFoodDelete = {index,name->
                        viewModel.deleteFoodById(index)
                        viewModel.getFoodByName(name)
                    },
                    onEmptyFood={
                        viewModel.getAllFoods()
                        navController.navigate(FridgeAppScreen.FindFood.name){
                            popUpTo(FridgeAppScreen.FindFood.name){inclusive=true}
                        }

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
                    food = uiState.foodV2,
                    updateFood = {
                        viewModel.updateSelectedFoodEdit(it)
                        viewModel.saveUpdatedFoodV2(it)
                        navController.navigate(FridgeAppScreen.FoodEdit.name){
                            popUpTo(FridgeAppScreen.FoodEdit.name){inclusive=true}
                        }
                    },
                    onFoodDelete = {index,name->
                        viewModel.deleteFoodById(index)
                        viewModel.getFoodByName(name)
                        navController.navigate(FridgeAppScreen.FoodDetail.name){
                            popUpTo(FridgeAppScreen.FoodDetail.name){inclusive=true}
                        }
                    },
                    onContainerChange = {
                        viewModel.updateContainerSelectedV2(it)
                        viewModel.getAllSectionsOfContainer()
                    },
                    onSectionChange = {
                        viewModel.updateSectionSelected(it)
                    },
                    possibleSections = uiState.sectionsOfContainer,
                    possibleConatiners = uiState.allContainers,
                    onLocationChange = {
                        viewModel.saveUpdatedLocationFoodV2(it)
                    },
                    sectionSelected = uiState.sectionSelected!!,
                    containerSelected = uiState.containerSelected!!,
                    navigateBack = {
                        viewModel.getFoodByName(it.food!!.name)
                        navController.navigate(FridgeAppScreen.FoodDetail.name){
                            popUpTo(FridgeAppScreen.FoodDetail.name){inclusive=true}
                        }
                    }

                )
            

            }

            composable(
                route = FridgeAppScreen.AddFoodContainer.name
            ){
                AddNewFoodScreen_Container(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding((dimensionResource(id = R.dimen.padding_medium))),
                    possibleContainers = uiState.allContainers,
                    onContainerValueChanged = {
                        viewModel.updateContainerSelectedV2(it)
                        viewModel.getAllSectionsOfContainer()
                        navController.navigate(FridgeAppScreen.AddFoodSection.name)
                    }
                )


            }

            composable(
                route = FridgeAppScreen.AddFoodSection.name
            ){
                AddNewFoodScreen_Section(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding((dimensionResource(id = R.dimen.padding_medium))),
                    possibleSections = uiState.sectionsOfContainer,
                    onSectionValueChanged = {
                        viewModel.updateSectionSelected(it)
                        viewModel.restartProvisionalFoodToAdd(mutableListOf())
                        navController.navigate(FridgeAppScreen.AddFood.name)
                    }
                )


            }

            composable(
                route = FridgeAppScreen.AddFood.name
            ) {
                AddNewFoodScreen_Food(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding((dimensionResource(id = R.dimen.padding_medium))),
                    foodList = uiState.foodsToAdd,
                    onAddClicked = {
                        viewModel.updateFoodV2(FoodV2())
                        viewModel.updateProvisionalComment("")
                        viewModel.updateProvisionalQuantity("1")
                        viewModel.isValidQuantity()
                        navController.navigate(FridgeAppScreen.SelectFood.name)
                    },
                    location = "${uiState.containerSelected!!.name}/${uiState.sectionSelected!!.name}",
                    onDeleteClick = {
                        viewModel.deleteProvisionalFood(it)
                    },
                    onAcceptClick = {
                        viewModel.saveProvisionalFoodToAddList()
                        Toast.makeText(context,"GUARDADO",Toast.LENGTH_SHORT).show()
                        navController.navigateUp()
                    }
                )
            }

            composable(
                route = FridgeAppScreen.SelectFood.name
            ){
                AddNewFoodScreen_SelectFood(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding((dimensionResource(id = R.dimen.padding_medium))),
                    foodV2 = uiState.foodV2,
                    quantity = uiState.provisionalQuantity,
                    isValidQuantity = uiState.isQuantityCorrect,
                    quantityValidation = { viewModel.isValidQuantity() },
                    onQuantityChanged = {
                        viewModel.updateQuantity(it) },
                    onAddClicked = {
                        viewModel.getAllFoodsV2()
                        navController.navigate(FridgeAppScreen.ListFood.name)
                    },
                    recordFoodSelected = uiState.recordFoodSelected,
                    comment = uiState.provisionalComment,
                    onCommentChange = {viewModel.updateProvisionalComment(it)},
                    onSaveClick = {quantity,comment->
                        var food = uiState.foodV2
                        food.quantity =quantity
                        food.comment  =comment
                        food.location =" ${ uiState.containerSelected!!.name }/${uiState.sectionSelected!!.name}"
                        viewModel.updateFoodV2(food)

                        viewModel.addNewProvisionalFood()
                        navController.navigate(FridgeAppScreen.AddFood.name){
                            popUpTo(FridgeAppScreen.AddFood.name){inclusive=true}
                        }
                    }
                )
            }

            composable(
                route = FridgeAppScreen.ListFood.name
            ){
                ListOfFoodScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding((dimensionResource(id = R.dimen.padding_medium))),
                    allFoodsRecord = uiState.allRecordFoods,
                    onFoodV2Changed = {

                        viewModel.updateFoodV2(FoodV2(food=it))
                        navController.navigate(FridgeAppScreen.SelectFood.name){
                            popUpTo(FridgeAppScreen.SelectFood.name){inclusive=true}
                        }
                    },
                    reload = {
                        viewModel.getAllFoodsV2()
                    },
                    onAddFood = {
                        viewModel.getAllFoodsV2()
                        viewModel.getAllCategories()
                        navController.navigate(FridgeAppScreen.FoodRecordEdit.name)
                    }
                )
            }
            composable(
                route = FridgeAppScreen.Settings.name
            ){
                SettingsScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding((dimensionResource(id = R.dimen.padding_medium))),
                    navAddCategory = {
                        viewModel.getAllCategories()
                        navController.navigate(FridgeAppScreen.AddCategory.name)
                    },
                    navAddFood = {
                        viewModel.getAllFoodsV2()
                        viewModel.getAllCategories()
                        navController.navigate(FridgeAppScreen.FoodRecordEdit.name)
                    },
                    navAdminSections = {
                        viewModel.getAllSectionRecord()
                        navController.navigate(FridgeAppScreen.AdminSections.name)
                    },
                    navAdminContainer = {
                        viewModel.getAllContainers()
                        navController.navigate(FridgeAppScreen.AdminContainers.name)
                    }
                )
            }

            composable(
                route = FridgeAppScreen.AddCategory.name
            ){
                EditCategoryScreen(
                    categoryList = uiState.allCategories,
                    isValidCategory = {viewModel.isValidCategory(it)},
                    isError = !uiState.validCategory,
                    addCategory = {
                        if(viewModel.isValidCategory(it))
                            viewModel.saveCategory(it)
                    },
                    reload = {
                        viewModel.getAllCategories()
                        navController.navigate(FridgeAppScreen.AddCategory.name){
                            popUpTo(FridgeAppScreen.AddCategory.name){inclusive=true}
                        }
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding((dimensionResource(id = R.dimen.padding_medium))),
                    onCategoryClicked = {
                        navController.navigateUp()
                    },
                    onDelete = {
                        viewModel.deleteCategoryRecordById(it)
                    }

                )

            }
            composable(
                route = FridgeAppScreen.FoodRecordEdit.name
            ){
                FoodRecordEditScreen(
                    foodRecord = uiState.allRecordFoods,
                    isValidFood = {viewModel.isValidFood(it)},
                    isError = !uiState.isValidFood,
                    addFood = {name, category->
                        viewModel.saveFood(name,category)
                    },
                    categorySelected = uiState.categorySelected,
                    selectCategory = {
                        viewModel.updateCategorySelected(it)
                    },
                    categoryList = uiState.allCategories,
                    addCategory = {
                        if(viewModel.isValidCategory(it))
                         viewModel.saveCategory(it)
                    },
                    isErrorCategory = !uiState.validCategory,
                    isValidCategory = {viewModel.isValidCategory(it)},
                    refreshCategories = {viewModel.getAllCategories()},
                    modifier = Modifier
                        .fillMaxSize()
                        .padding((dimensionResource(id = R.dimen.padding_medium))),
                    reload = {
                        viewModel.getAllFoodsV2()
                        viewModel.getAllCategories()
                        navController.navigate(FridgeAppScreen.FoodRecordEdit.name){
                            popUpTo(FridgeAppScreen.FoodRecordEdit.name){inclusive=true}
                        }
                    },
                    delete = {
                        viewModel.deleteFoodRecordById(it)
                    }


                )
            }
            composable(
                route  = FridgeAppScreen.FindFoodByContainer.name
            ){
                SearchFoodContainerScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding((dimensionResource(id = R.dimen.padding_medium))),
                    containers = uiState.allContainers,
                    onContainerValueChange = {
                        viewModel.updateContainerSelectedV2(it)
                        viewModel.getAllSectionsOfContainer()
                        viewModel.getAllFoodsOfContainer(it)
                    },
                    onFoodClick = {
                        viewModel.updateSelectedFoodEdit(it)
                        viewModel.updateProvisionalQuantity(it.quantity)
                        viewModel.getAllContainers()
                    },
                    onFoodDelete = {index,name->
                        viewModel.deleteFoodById(index)
                        viewModel.getFoodByName(name)
                    },
                    foods = uiState.allFoodsOfContainer,
                    foodSelected = uiState.foodV2,
                    updateFood = {
                        viewModel.updateSelectedFoodEdit(it)
                        viewModel.saveUpdatedFoodV2(it)
                    },
                    sections = uiState.sectionsOfContainer,
                    onSectionChange = { viewModel.updateSectionSelected(it) },
                    sectionSelected = uiState.sectionSelected!!,
                    newContainerSelected = uiState.containerSelected!!,
                    onLocationChage = {viewModel.saveUpdatedLocationFoodV2(it)},
                    onSearchingContainerUpdate = {
                        viewModel.updateSearchingContainer(it)
                    },
                    reload = {
                        viewModel.getAllFoodsOfContainer(it)
                        navController.navigate(FridgeAppScreen.FindFoodByContainer.name){
                            popUpTo(FridgeAppScreen.FindFoodByContainer.name){inclusive=true}
                        }
                    },
                    searchingContainer =uiState.searchingContainer,
                    step = uiState.findFoodContainerStep,
                    stepUpdate = { viewModel.updateFindFoodContainerStep(it) }
                )
            }

            composable(
                route = FridgeAppScreen.AdminSections.name
            ){
                AdminSectionsScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding((dimensionResource(id = R.dimen.padding_medium))),
                    sectionList = uiState.sectionsRecordList,
                    isValidSection = { viewModel.isValidSection(it) },
                    isError = !uiState.validSection,
                    addSection = {
                           viewModel.saveSection(it)
                    },
                    reload = {
                          viewModel.getAllSectionRecord()
                    },
                    onSecionClicked = {},
                    onDelete = {
                        viewModel.deleteSectionRecordById(it)
                    }
                )

            }

            composable(
                route = FridgeAppScreen.AdminContainers.name
            ){
                AdminContainerScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding((dimensionResource(id = R.dimen.padding_medium))),
                    containers = uiState.allContainers,
                    isValidContainer ={
                      viewModel.isValidContainer(it)
                    },
                    isError = !uiState.validContainer,
                    addContainer = { viewModel.saveContainer(it) },
                    reload={
                        viewModel.getAllContainers()
                        navController.navigate(FridgeAppScreen.AdminContainers.name){
                            popUpTo(FridgeAppScreen.AdminContainers.name){inclusive=true}
                        }
                    },
                    onContainerClicked={
                        viewModel.updateContainerSelectedV2(it)
                        viewModel.getAllSectionsOfContainer()
                        navController.navigate(FridgeAppScreen.AdminSectionsOfContainer.name)
                    },
                    delete = {
                        viewModel.deleteContainer(it)
                    }
                )
            }

            composable(
                route = FridgeAppScreen.AdminSectionsOfContainer.name
            ){
                AdminSectionsOfContainerScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding((dimensionResource(id = R.dimen.padding_medium))),

                    sectionList = uiState.sectionsOfContainer,
                    isValidSection = { viewModel.isValidSection(it) },
                    isErrorSection = !uiState.validSection,
                    addSection = {
                        viewModel.saveSection(it)
                    },
                    reload = {
                        viewModel.getAllSectionsOfContainer()
                    },
                    updateSectionsOfContainer = {
                        Log.d("TAGGGGGF", it.toString())
                           viewModel.updateSectionsOfContainer(it)
                        viewModel.getAllSectionsOfContainer()
                    },
                    reloadSectionRecordList = {
                      viewModel.getAllSectionRecord()
                    },
                    sectionRecorList = uiState.sectionsRecordList,
                    delete = {
                        Log.d("TAGGGG",it.toString())
                        viewModel.deleteSection(it)
                    }

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