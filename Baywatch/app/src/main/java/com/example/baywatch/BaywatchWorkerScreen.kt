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

import com.example.baywatch.userScreens.StatusRecordScreen
import com.example.baywatch.workerScreens.AddStatusScreen
import com.example.baywatch.workerScreens.HomeScreen


enum class BaywatchWorkerScreen(@StringRes val title: Int){
    Home(title=(R.string.mis_playas)),
    BeachStatus(title= (R.string.informaci_n_actual)),
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBarWorker(
    currentScreen: BaywatchWorkerScreen,
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
fun BaywatchWorkerScreen(
    navController: NavHostController = rememberNavController(),
    viewModel: AppViewModelWorker = viewModel(),
    context: Context

){

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = BaywatchWorkerScreen.valueOf(
        backStackEntry?.destination?.route ?: BaywatchWorkerScreen.Home.name
    )

    Scaffold(
        topBar = {
            AppBarWorker(
                canNavigateBack =false,
                currentScreen = currentScreen,
                navigateUp = { navController.navigateUp()},
                onRecordClick = {

                },
                canAccessRecord =  false
            )
        }
    ) { innerPadding ->
        viewModel.initModel(context)
        val uiState by viewModel.appState.collectAsState()

        NavHost(
            navController = navController,
            startDestination = BaywatchUserScreen.Home.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(
                route=BaywatchWorkerScreen.Home.name
            ){
                HomeScreen(
                    modifier = Modifier.fillMaxSize(),
                    availableBeaches = uiState.allBeaches,
                    onBeachClick = {
                        Log.d("TAGGGGGGG",it.toString())
                        viewModel.updateBeachSelected(it)
                        viewModel.getStatusById(it.idStatus)
                        navController.navigate(BaywatchWorkerScreen.BeachStatus.name)
                    },
                    availableBeachesError = uiState.errorAllBeaches
                )
            }
            composable(
                route=BaywatchWorkerScreen.BeachStatus.name
            ){
                AddStatusScreen(
                    modifier = Modifier.fillMaxSize(),
                    currentStatus = uiState.currentStatus,
                    saveStatus ={
                        viewModel.saveNewStatus(it)
                        viewModel.getUserBeaches()
                        navController.navigateUp()
                    },
                    beachSelected = uiState.beachSelected!!,
                    currentStatusError = uiState.currentStatusError,
                    backPressed = {
                        viewModel.getUserBeaches()
                        navController.navigateUp()
                    }
                )
            }


        }
    }
}

