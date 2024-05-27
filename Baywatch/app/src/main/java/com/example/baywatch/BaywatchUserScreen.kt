package com.example.baywatch


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
import androidx.compose.material.icons.filled.List
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
import com.example.baywatch.data.FavLocationRepository
import com.example.baywatch.userScreens.BeachDetailScreen
import com.example.baywatch.userScreens.HomeScreen
import com.example.baywatch.userScreens.StatusRecordScreen


enum class BaywatchUserScreen(@StringRes val title: Int){
    Home(title=(R.string.inicio)),
    BeachDetail(title= (R.string.informaci_n_actual)),
    BeachRecord(title = (R.string.historial_de_informaci_n_del_d_a))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    currentScreen: BaywatchUserScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    canAccessRecord: Boolean,
    onRecordClick: ()->Unit
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
            if(canAccessRecord){
                IconButton(onClick = onRecordClick) {
                    Icon(
                        imageVector = Icons.Filled.List,
                        contentDescription = "Historial"
                    )
                }}
        }
    )
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun BaywatchUser(
    navController: NavHostController = rememberNavController(),
    viewModel: AppViewModel = viewModel(),
    favLocationRepository: FavLocationRepository,
    latitude: Double?,
    longitude: Double?
){

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = BaywatchUserScreen.valueOf(
        backStackEntry?.destination?.route ?: BaywatchUserScreen.Home.name
    )

    Scaffold(
        topBar = {
            AppBar(
                canNavigateBack =false,
                currentScreen = currentScreen,
                navigateUp = { navController.navigateUp()},
                onRecordClick = {
                   viewModel.getStatusRecord()
                    navController.navigate(BaywatchUserScreen.BeachRecord.name)
                },
                canAccessRecord =  currentScreen == BaywatchUserScreen.BeachDetail
            )
        }
    ) { innerPadding ->
        viewModel.initModel(favLocationRepository)
        val uiState by viewModel.appState.collectAsState()

        NavHost(
            navController = navController,
            startDestination = BaywatchUserScreen.Home.name,
            modifier = Modifier.padding(innerPadding)
        ) {

            composable(
                route = BaywatchUserScreen.Home.name
            ){
                HomeScreen(
                    modifier = Modifier
                        .fillMaxSize(),
                    allBeaches = uiState.allBeaches,
                    errorAllBeaches = uiState.errorAllBeaches,
                    onMarkerClicked = {
                        if(it.idStatus.isNotBlank()) {
                            viewModel.getStatusById(it.idStatus)
                        }else{
                            viewModel.updateStatus(null)
                        }
                        viewModel.updateBeachSelected(it)
                        viewModel.existsLocationId(it.id!!)
                        navController.navigate(BaywatchUserScreen.BeachDetail.name)
                    },
                    reloadAllBeches = {
                        viewModel.getAllBeaches()
                    },
                    reloadFavBeaches = {
                        viewModel.getAllFavBeaches()

                   },
                    latitude = latitude,
                    longitude = longitude
                )
            }

            composable(
                route = BaywatchUserScreen.BeachDetail.name
            ){
                BeachDetailScreen(
                    modifier = Modifier
                        .fillMaxSize(),
                    beachSelected = uiState.beachSelected!!,
                    currentStatus = uiState.beachCurrentStatus,
                    currentStatusError = uiState.beachcurrentStatusError,
                    isFav = uiState.isFavBeach,
                    favClicked = {
                        viewModel.updateFavLocation(it)
                    },
                    onBackClicked ={
                        viewModel.getAllBeaches()
                        navController.navigate(BaywatchUserScreen.Home.name){
                            popUpTo(BaywatchUserScreen.Home.name){inclusive=true}
                        }
                    }
                )
            }

            composable(
                route = BaywatchUserScreen.BeachRecord.name
            ){
                StatusRecordScreen(
                    modifier = Modifier
                        .fillMaxSize(),
                    beachSelected = uiState.beachSelected!!,
                    statusRecord = uiState.statusRecord,
                    statusRecordError = uiState.statusRecordError,

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