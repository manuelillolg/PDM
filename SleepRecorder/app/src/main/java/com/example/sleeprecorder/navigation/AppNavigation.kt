package com.example.sleeprecorder.navigation

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sleeprecorder.SleepRecorder
import com.example.sleeprecorder.ui.screens.SplashScreen

@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun AppNavigation(context: Context){
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = AppScreens.SplashScreen.route){
        composable(AppScreens.SplashScreen.route){
            SplashScreen(navController)
        }
        composable(AppScreens.MainScreen.route){
            SleepRecorder(context =context )
        }
    }
}