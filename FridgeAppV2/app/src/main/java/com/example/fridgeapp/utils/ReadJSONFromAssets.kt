package com.example.fridgeapp.utils

import android.content.Context
import android.util.Log
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStreamReader

fun readJSONFromAssets(context: Context, fileName: String) : String?{
    val jsonString: String
    try{
        jsonString = context.assets.open("$fileName").bufferedReader().use{it.readText()}
    }catch(ioException :IOException){
        ioException.printStackTrace()
        return null
    }

    return jsonString
}