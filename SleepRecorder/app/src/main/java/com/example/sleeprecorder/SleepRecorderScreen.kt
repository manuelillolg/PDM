package com.example.sleeprecorder

import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.sleeprecorder.ui.AppViewModel
import com.example.sleeprecorder.ui.screens.HomeScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.navigation
import com.example.sleeprecorder.components.PersonalizatedDialog
import com.example.sleeprecorder.ui.screens.AudioListScreen
import com.example.sleeprecorder.ui.screens.RecordScreen
import com.example.sleeprecorder.ui.screens.SettingsScreen
import java.util.Timer
import kotlin.concurrent.schedule

enum class SleepRecorderScreen (@StringRes val title: Int){
    Home(R.string.inicio),
    AudioGallery(R.string.audios),
    Settings(R.string.ajustes),
    RecordScreen(R.string.grabacion)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SleepRecorderTopBar(
    currentScreen: SleepRecorderScreen,
    canNavigateBack: Boolean,
    navigateUp:  () -> Unit,
    modifier: Modifier = Modifier,
    settings: ()->Unit,
    audioList: ()-> Unit
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
        actions = {
            if(currentScreen!= SleepRecorderScreen.RecordScreen){
                IconButton(onClick = audioList) {
                    Icon(
                        imageVector = Icons.Filled.List,
                        //contentDescription = stringResource(R.string.back_button)
                        contentDescription = stringResource(R.string.lista_de_audios)
                    )
                }

                IconButton(onClick = settings) {
                    Icon(
                        imageVector = Icons.Filled.Settings,
                        //contentDescription = stringResource(R.string.back_button)
                        contentDescription = stringResource(R.string.ajustes)
                    )
                }
            }
        },
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = { navigateUp() }  ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        //contentDescription = stringResource(R.string.back_button)
                        contentDescription = stringResource(R.string.volver_atr_s)
                    )
                }
            }
        },

    )
}

@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun SleepRecorder(
    navController: NavHostController = rememberNavController(),
    viewModel: AppViewModel = viewModel(),
    context: Context

){

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = SleepRecorderScreen.valueOf(
        backStackEntry?.destination?.route ?: SleepRecorderScreen.Home.name
    )

    Scaffold(
        topBar = {
            SleepRecorderTopBar(
                canNavigateBack = navController.previousBackStackEntry != null,
                currentScreen = currentScreen,
                navigateUp = {
                    if(viewModel.isPendingTaskEmpty())
                        navController.navigateUp() 
                    else{
                        viewModel.updateTryGoBack(true)
                    }
                    
                },
                settings = { navController.navigate(SleepRecorderScreen.Settings.name)},
                audioList = {
                    viewModel.getAudioList()
                    navController.navigate(SleepRecorderScreen.AudioGallery.name)

                }
            )
        }
    ) { innerPadding ->
       val uiState by viewModel.appState.collectAsState()
        viewModel.initAppState(context = context)

        NavHost(
            navController = navController,
            startDestination = SleepRecorderScreen.Home.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(
                route = SleepRecorderScreen.Home.name
            ) {
                HomeScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding((dimensionResource(id = R.dimen.padding_medium))),

                    acceptTimeStart = {hour,minute ->
                        viewModel.updateTimeStart(hour,minute)
                    },
                    acceptTimeEnd = {hour,minute ->
                        viewModel.updateTimeEnd(hour,minute)
                    },
                    hourStart = uiState.hourInit,
                    minuteStart = uiState.minuteInit,
                    hourEnd = uiState.hourEnd,
                    minuteEnd = uiState.minuteEnd,
                    startRecording = { viewModel.startRecordingOnThreshold() },
                    stopRecording = {
                        viewModel.stopRecording()
                        viewModel.removePendingTask()
                    },
                    scheduleStart = {task, calendar ->
                        uiState.pendingTasks.add(task)
                        uiState.timerStart = Timer()
                        uiState.timerStart.schedule(task,calendar.time)
                    },
                    scheduleEnd ={ task, timeToEnd ->
                        uiState.pendingTasks.add(task)
                        uiState.timerEnd = Timer()
                        uiState.timerEnd.schedule(task,timeToEnd)
                    },
                    navigateToRecordScreen = {
                        navController.navigate(SleepRecorderScreen.RecordScreen.name)
                    },
                    navigateHome ={
                        navController.navigate(SleepRecorderScreen.Home.name)
                    }
                )

            }
            composable(
                route = SleepRecorderScreen.AudioGallery.name
            ){
                AudioListScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding((dimensionResource(id = R.dimen.padding_medium))),
                    audioList = uiState.audioList,
                    onAudioClick = {
                        val intent = Intent(Intent.ACTION_VIEW)
                        val audioUri = viewModel.getAudioUriByDisplayName(it)
                        if (audioUri != null) {
                            intent.setDataAndType(audioUri, "audio/*")
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            uiState.applicationContext!!.startActivity(intent)
                        } else {
                            Toast.makeText(context, "Archivo de audio no encontrado", Toast.LENGTH_SHORT).show()
                        }
                    },
                    onShareClick = {
                        val audioUri = viewModel.getAudioUriByDisplayName(it)
                        val intent = Intent(Intent.ACTION_SEND)
                            .putExtra(Intent.EXTRA_STREAM, audioUri)
                        if (audioUri != null) {
                            intent.setDataAndType(audioUri, "audio/*")
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            uiState.applicationContext!!.startActivity(intent)
                        } else {
                            Toast.makeText(context, "Archivo de audio no encontrado", Toast.LENGTH_SHORT).show()
                        }
                    },
                    audioTypeSelected = uiState.audioTypeSelected,
                    updateTypeSelected = {
                        viewModel.updateAudioType(it)
                    },
                    onAccept= {displayName, newDisplayName,date->
                        viewModel.editAudioDisplayNameByDisplayName(displayName, newDisplayName,date)
                    },
                    onAcceptDelete={displayName->
                        viewModel.deleteAudioByName(displayName)

                    },
                    getAllAudios= {
                        viewModel.getAudioList()
                    },
                    getFilteredAudios={
                        viewModel.getAudioList(it)
                    }
                )
            }
            composable(
                route = SleepRecorderScreen.RecordScreen.name
            ){
                RecordScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding((dimensionResource(id = R.dimen.padding_medium))),
                    isRecording = uiState.isRecording,
                    tryGoBack = uiState.tryGoBack,
                    onAcceptClick = {
                        viewModel.removePendingTask()
                        viewModel.stopRecording()
                        viewModel.updateTryGoBack(false)
                        navController.navigateUp()
                    },
                    endRecording  ={
                        navController.navigateUp()
                    },
                    pendingTaskEmpty = uiState.pendingTasks.isEmpty()

                )
            }

            composable(
                route = SleepRecorderScreen.Settings.name
            ){
                SettingsScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding((dimensionResource(id = R.dimen.padding_medium))),
                    db = uiState.db,
                    onDbChange = {
                        viewModel.updateDb(it)
                    },
                    onTestDismiss = {
                        viewModel.updateIsRecording(false)
                    },
                    onTestClick = {
                        viewModel.startTest()
                    },
                    isListening = uiState.isListening
                )
            }

        }
    }
}