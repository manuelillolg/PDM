package com.example.sleeprecorder

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import com.example.sleeprecorder.ui.theme.AppTheme
import java.io.File

class MainActivity : ComponentActivity() {

    private val REQUEST_RECORD_AUDIO_PERMISSION = 123
    private val REQUEST_STORAGE_PERMISSION = 456

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        // Permisos otorgados, continuar con la configuración de la actividad
        setupActivity()

        // Verificar si los permisos de grabación y almacenamiento externo están otorgados
        if (arePermissionsGranted()) {
            val rootDir = Environment.getExternalStoragePublicDirectory("")
            val sleepRecorderDir = File(rootDir, "SleepRecorder")
            Log.d("TAGGGG", rootDir.absolutePath)
            Log.d("TAGGGG", "Creating SleepRecorder folder at: ${sleepRecorderDir.absolutePath}")
            if (!sleepRecorderDir.exists()) {
                sleepRecorderDir.mkdirs()
                Log.d("TAGGGG", "Folder created successfully "+ sleepRecorderDir.exists())
            } else {
                Log.d("TAGGGG", "Folder already exists")
            }

            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

            // Permisos otorgados, continuar con la configuración de la actividad
            setupActivity()
        } else {
            // Solicitar permisos al usuario
            requestPermissions()
        }
    }

    private fun arePermissionsGranted(): Boolean {
        val recordAudioPermission = ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED

        val storagePermission = ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

        val readStoragePermission = ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

        return recordAudioPermission && storagePermission && readStoragePermission
    }


    private fun requestPermissions() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Solicitar permiso de grabación de audio
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                REQUEST_RECORD_AUDIO_PERMISSION
            )
        }

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Solicitar permiso de almacenamiento externo
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_STORAGE_PERMISSION
            )
        }

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Solicitar permiso de almacenamiento externo
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_STORAGE_PERMISSION
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun setupActivity() {
        setContent {
            AppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SleepRecorder(context = applicationContext)
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION || requestCode == REQUEST_STORAGE_PERMISSION) {
            if (arePermissionsGranted()) {
                // Permisos otorgados, continuar con la configuración de la actividad
                setupActivity()
            } else {
                // Permiso denegado, cerrar la actividad
                finish()
            }
        }
    }


}
