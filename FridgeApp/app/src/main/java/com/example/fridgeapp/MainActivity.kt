package com.example.fridgeapp

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.fridgeapp.model.Application
import com.example.fridgeapp.ui.theme.FridgeAppTheme
import com.example.fridgeapp.utils.readJSONFromAssets
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.IOException
import java.io.InputStream

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            FridgeAppTheme {
                FridgeApp(
                    jsonString =  loadJson(),
                    context = applicationContext
                )
            }
        }
    }

    private fun loadJson(): String? {
        val jsonFileString = readJSONFromAssets(context = applicationContext, fileName="data.json")
        return jsonFileString
    }
}



