package com.example.sleeprecorder.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.sleeprecorder.R
import com.example.sleeprecorder.navigation.AppScreens
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavHostController
){
    LaunchedEffect (key1 = true){
        delay(2000)
        navController.popBackStack()
        navController.navigate(AppScreens.MainScreen.route)
    }

    Splash()
}
@Composable
fun Splash(){



    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.beds_3168626),
            contentDescription = "",
            modifier = Modifier .size(140.dp)
            )

    }
}

@Preview(showBackground = true)
@Composable

fun SplashScreenPreview(){
    Splash()
}