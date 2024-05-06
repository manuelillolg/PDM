package com.example.sleeprecorder.ui


import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.example.sleeprecorder.ui.model.ApplicationState
import com.example.sleeprecorder.ui.model.Audio
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Math.abs
import java.util.Date
import kotlin.experimental.and
import kotlin.time.Duration.Companion.seconds
import com.arthenica.mobileffmpeg.FFmpeg
import com.example.sleeprecorder.ui.model.AudioTypes
import java.io.FileInputStream


class AppViewModel : ViewModel() {

    private var _appState = MutableStateFlow(ApplicationState())
    var appState: StateFlow<ApplicationState> = _appState.asStateFlow()
    private var _timeBetweenSilence : Date = Date()

    var  bufferSize = AudioRecord.getMinBufferSize(
        44100,
        AudioFormat.CHANNEL_IN_MONO,
        AudioFormat.ENCODING_PCM_16BIT
    )

    fun updateDb(newDb : Float){
        _appState.update { ApplicationState->
            ApplicationState.copy(
                db = newDb
            )
        }
    }

    fun initAppState(context: Context){

        if(!_appState.value.isInit){
            _appState.update {
                it.copy(
                    applicationContext = context
                )
            }



        }





    }

    fun createAudioRecordInstance(){

        if (ActivityCompat.checkSelfPermission(
                _appState.value.applicationContext!!,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            //  _requestPermissionEvent.value = Event(Manifest.permission.RECORD_AUDIO)
        }
        _appState.update { applicationState ->

            applicationState.copy(
                audioRecord = AudioRecord(
                     MediaRecorder.AudioSource.MIC,
                    44100,
                    AudioFormat.CHANNEL_IN_MONO,
                    AudioFormat.ENCODING_PCM_16BIT,
                    bufferSize
                )
            )
        }


    }

    fun startTest (){
        startRecording()

        // Actualiza el mensaje en pantalla cuando se detecta un sonido por encima del umbral
        Thread {
            while (_appState.value.isRecording) {
                val buffer = ShortArray(bufferSize)
                val bytesRead = _appState.value.audioRecord?.read(buffer, 0, bufferSize) ?: 0
                if (bytesRead > 0) {
                    val amplitude = buffer.map { Math.abs(it.toDouble()) }.maxOrNull() ?: 0.0
                    val decibelios = 20 * Math.log10(amplitude)
                    if (decibelios > _appState.value.db) {
                        Log.d("TAGGGGG","ESCUCHANDO")
                        _appState.update {
                            it.copy(
                                isListening = true
                            )
                        }
                    } else {
                        Log.d("TAGGGGG","ESCUCHANDO")

                        _appState.update {
                            it.copy(
                                isListening = false
                            )
                        }
                    }
                }
            }

            stopRecording()
        }.start()
    }

     fun startRecording() {
         createAudioRecordInstance()
        _appState.value.audioRecord?.startRecording()

        _appState.update {
            it.copy(
                isRecording = true
            )
        }
    }

     fun stopRecording() {
        _appState.value.audioRecord?.stop()
         _appState.value.audioRecord?.release()
         _appState.update {
             it.copy(
                 isRecording = false,
                 isListening = false
             )
         }
         Log.d("TAGGGGG","Grabacion terminada")
    }

    fun updateIsRecording(recording : Boolean){
        _appState.update {
            it.copy(
                isRecording = recording,
            )
        }
    }

    fun updateTimeStart(hour:Int, minute:Int){
        _appState.update {
            it.copy(
                hourInit = hour,
                minuteInit = minute
            )
        }
    }

    fun updateTimeEnd(hour:Int, minute:Int){
        _appState.update {
            it.copy(
                hourEnd = hour,
                minuteEnd = minute
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    fun startRecordingOnThreshold() {
        startRecording()
        var secondsEnd: Date
        _appState.update {
            it.copy(
                isListening = false
            )
        }

        val recordingBuffer = ArrayList<Short>()
        Log.d("TAGGGGG","Comienza a grabar")
        Log.d("TAGGGGG", _appState.value.pendingTasks.toString())
        while (_appState.value.isRecording) {
            val buffer = ShortArray(bufferSize)
            val bytesRead = _appState.value.audioRecord?.read(buffer, 0, bufferSize) ?: 0
             secondsEnd = Date();


            if (bytesRead > 0) {
                val amplitude = buffer.map { Math.abs(it.toDouble()) }.maxOrNull() ?: 0.0
                val decibelios = 20 * Math.log10(amplitude)

                recordingBuffer.addAll(buffer.toList())
                if (decibelios > _appState.value.db) {

                    _appState.update {
                        it.copy(
                            isListening = true,
                            isNoiseRecorded = true
                        )
                    }
                    Log.d("TAGGGGG","ESUCHANDO")
                    _timeBetweenSilence=Date()
                    // Almacena los datos en el buffer de grabación
                    //recordingBuffer.addAll(buffer.toList())
                }else{
                    _appState.update {
                        it.copy(
                            isListening = false
                        )
                    }
                    Log.d("TAGGGGG","SILENCIO")
                }



                    // Si no se está grabando una sección, pero hay datos en el buffer de grabación,
                    // significa que se debe guardar la sección de audio previa
                    //if (recordingBuffer.isNotEmpty())
                    //Log.d("TAGGGG",_timeBetweenSilence.seconds.toString())

                if (abs(_timeBetweenSilence.seconds - secondsEnd.seconds) == _appState.value.silenceTime && !_appState.value.isListening) {

                    if(_appState.value.isNoiseRecorded){
                        saveRecordingV2(_appState.value.applicationContext!!,recordingBuffer)
                        Log.d("TAGGGGG","GUARDANDO SONIDO")
                        recordingBuffer.clear() // Limpia el buffer para la próxima grabación
                        _timeBetweenSilence=Date()
                    }else{
                        Log.d("TAGGGGG","BORRANDO SILENCIO")
                        recordingBuffer.clear()
                        _timeBetweenSilence=Date()


                    }

                    _appState.update {
                        it.copy(
                            isListening = false,
                            isNoiseRecorded = false
                        )
                    }

                }else if(abs(_timeBetweenSilence.seconds - secondsEnd.seconds) > _appState.value.silenceTime && !_appState.value.isListening){
                    Log.d("TAGGGGG","BORRANDO SILENCIO GRANDE")
                    recordingBuffer.clear()
                    _timeBetweenSilence=Date()
                }


            }
        }

        recordingBuffer.clear()

    }



    fun saveRecording(context: Context, buffer: List<Short>) {

        val contentResolver = context.contentResolver

        val contentValues = ContentValues().apply {
            put(MediaStore.Audio.Media.DISPLAY_NAME, "audio_recording_${System.currentTimeMillis()}.pcm")
            put(MediaStore.Audio.Media.MIME_TYPE, "audio/wav") // Cambia esto según el tipo de archivo que estés guardando
            put(MediaStore.Audio.Media.RELATIVE_PATH, "${Environment.DIRECTORY_MUSIC}/SleepRecorder")
            put(MediaStore.Audio.Media.DATE_ADDED, System.currentTimeMillis() / 1000)
            put(MediaStore.Audio.Media.ALBUM, "")
        }

        val uri = contentResolver.insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, contentValues)

        try {
            uri?.let { uri ->
                contentResolver.openOutputStream(uri)?.use { outputStream ->
                    // Convierte la lista de Short a un ByteArray para escribirlo en el archivo PCM
                    val byteBuffer = ByteArray(buffer.size * 2)
                    for (i in buffer.indices) {
                        val shortValue = buffer[i]
                        byteBuffer[i * 2] = (shortValue and 0xFF).toByte()
                        byteBuffer[i * 2 + 1] = (shortValue.toInt() shr 8 and 0xFF).toByte()
                    }

                    // Escribe los datos en el archivo PCM
                    outputStream.write(byteBuffer)
                }
            }
            Log.d("AppViewModel", "Recording saved successfully.")


        } catch (e: IOException) {
            Log.e("AppViewModel", "Error saving recording: ${e.message}")
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    fun saveRecordingV2(context: Context, buffer: List<Short>){
       /* val contentResolver = context.contentResolver

        // Prepare information for saving the PCM file
        val pcmFileName = "audio_recording_${System.currentTimeMillis()}"
        //val pcmPath = "${Environment.DIRECTORY_MUSIC}/SleepRecorder/$pcmFileName.pcm"
/*
        val pcmPath = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC),
            "SleepRecorder/$pcmFileName.pcm",

        ).absolutePath*/
        val pcmPath = context.filesDir.absolutePath + "/SleepRecorder/$pcmFileName.pcm"
        Log.d("TAGGGG",pcmPath.toString())
        val contentValues = ContentValues().apply {
            put(MediaStore.Audio.Media.DISPLAY_NAME, "$pcmFileName.pcm") // Display name for PCM
            put(MediaStore.Audio.Media.MIME_TYPE, "audio/wav") // Mime type for PCM
            //put(MediaStore.Audio.Media.RELATIVE_PATH, "${Environment.DIRECTORY_MUSIC}/SleepRecorder")
            put(MediaStore.Audio.Media.DATE_ADDED, System.currentTimeMillis() / 1000)
            put(MediaStore.Audio.Media.ALBUM, "")
        }

        try {
            // Save the PCM data to a temporary file
            val uri = contentResolver.insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, contentValues)
            uri?.let { uri ->
                contentResolver.openOutputStream(uri)?.use { outputStream ->
                    val byteBuffer = ByteArray(buffer.size * 2)
                    for (i in buffer.indices) {
                        val shortValue = buffer[i]
                        byteBuffer[i * 2] = (shortValue and 0xFF).toByte()
                        byteBuffer[i * 2 + 1] = (shortValue.toInt() shr 8 and 0xFF).toByte()
                    }
                    outputStream.write(byteBuffer)
                }
            }

            // Convert PCM to MP3 using ffmpeg (replace with your ffmpeg path)
            val ffmpegPath = "/path/to/ffmpeg" // Replace with actual ffmpeg path
            val mp3FileName = "$pcmFileName.mp3"
            val mp3Path = "${Environment.DIRECTORY_MUSIC}/SleepRecorder/$mp3FileName"
            /*val process = Runtime.getRuntime().exec(
                arrayOf(
                    ffmpegPath,
                    "-i", pcmPath,
                    "-c:a", "libmp3lame",
                    "-ab", "128k",  // Adjust bitrate as needed
                    mp3Path
                )
            )
            process.waitFor()*/
            val newPcmPath = findFileByMediaStore(context,"$pcmFileName.pcm")
            Log.d("TAGGG", newPcmPath!!.path!!)
            val command = "-i ${pcmPath} -c:a libmp3lame -ab 128k ${mp3Path}"
            val result = FFmpeg.execute(command.split(" ").toTypedArray())
            if (result== 0) {
                // MP3 conversion successful, update content values for MP3
                contentValues.clear()
                contentValues.put(MediaStore.Audio.Media.DISPLAY_NAME, mp3FileName) // Display name for MP3
                contentValues.put(MediaStore.Audio.Media.MIME_TYPE, "audio/mpeg") // Mime type for MP3
                contentValues.put(MediaStore.Audio.Media.RELATIVE_PATH, "${Environment.DIRECTORY_MUSIC}/SleepRecorder")
                contentValues.put(MediaStore.Audio.Media.DATE_ADDED, System.currentTimeMillis() / 1000)
                contentValues.put(MediaStore.Audio.Media.ALBUM, "")

                // Update MediaStore entry with MP3 information (optional)
                uri?.let { uri ->
                    contentResolver.update(uri, contentValues, null)
                }

                Log.d("AppViewModel", "Recording converted to MP3 and saved successfully.")
            } else {
                Log.e("AppViewModel", "Error converting PCM to MP3 using ffmpeg.")
            }

            // Delete the temporary PCM file
            val pcmFile = File(pcmPath)
            if (pcmFile.exists()) {
                pcmFile.delete()
            }
        } catch (e: IOException) {
            Log.e("AppViewModel", "Error saving recording: ${e.message}")
        }*/

        val contentResolver = context.contentResolver

        val pcmFileName = "audio_recording_${System.currentTimeMillis()}"
        val pcmPath = context.filesDir.absolutePath + "/$pcmFileName.pcm"

        FileOutputStream(pcmPath, true).use { outputStream ->
            val byteBuffer = ByteArray(buffer.size * 2)
            for (i in buffer.indices) {
                val shortValue = buffer[i]
                byteBuffer[i * 2] = (shortValue and 0xFF).toByte()
                byteBuffer[i * 2 + 1] = (shortValue.toInt() shr 8 and 0xFF).toByte()
            }
            outputStream.write(byteBuffer)
        }

        val mp3Path = context.filesDir.absolutePath + "/$pcmFileName.mp3"

        val cmd = arrayOf(
            "-f", "s16le",
            "-ar", "44100",
            "-ac", "1",
            "-i", pcmPath,
            "-codec:a", "libmp3lame",
            "-q:a", "3",
            mp3Path // Archivo de salida MP3
        )



        val result = FFmpeg.execute(cmd)
        val output = File(mp3Path)

        Log.d("LOGGGGGGG", output.exists().toString())
        if (result == 0) {
            // MP3 conversion successful, update content values for MP3
            /*contentValues.clear()
            contentValues.put(MediaStore.Audio.Media.DISPLAY_NAME, mp3FileName) // Display name for MP3
            contentValues.put(MediaStore.Audio.Media.MIME_TYPE, "audio/mpeg") // Mime type for MP3
            contentValues.put(MediaStore.Audio.Media.RELATIVE_PATH, "${Environment.DIRECTORY_MUSIC}/SleepRecorder")
            contentValues.put(MediaStore.Audio.Media.DATE_ADDED, System.currentTimeMillis() / 1000)
            contentValues.put(MediaStore.Audio.Media.ALBUM, "")

            // Update MediaStore entry with MP3 information (optional)
            uri?.let { uri ->
                contentResolver.update(uri, contentValues, null)
            }
*/
            Log.d("AppViewModel", "Recording converted to MP3 and saved successfully.")
            val pcm = File(pcmPath)
            pcm.delete()
            val ret = moveMp3ToSleepRecorder(context,mp3Path)
            Log.d("TAGGGG", ret.toString())
        } else {
            Log.e("AppViewModel", "Error converting PCM to MP3 using ffmpeg.")
        }

    }


    @SuppressLint("Range")
    fun moveMp3ToSleepRecorder(context: Context, mp3FilePath: String): Boolean {
        val contentResolver = context.contentResolver
        val currentTimeMillis = System.currentTimeMillis()
        // Obtener la URI de la carpeta /Music/SleepRecorder en la MediaStore
        val musicDirUri = MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        val sleepRecorderDirUri = contentResolver.insert(musicDirUri, ContentValues().apply {
            put(MediaStore.Audio.Media.DISPLAY_NAME, "${currentTimeMillis}_audio_recording.mp3")
            put(MediaStore.Audio.Media.MIME_TYPE, "audio/mp3")
            put(MediaStore.Audio.Media.YEAR, currentTimeMillis / 1000)
            put(MediaStore.Audio.Media.RELATIVE_PATH, "${Environment.DIRECTORY_MUSIC}/SleepRecorder")
            put(MediaStore.Audio.Media.ALBUM, "Categoría deseada")
            put(MediaStore.Audio.Media.TITLE, "Ronquido")
        })

        sleepRecorderDirUri?.let { newMp3Uri ->
            // Obtener un InputStream del archivo MP3 original
            val inputStream = FileInputStream(File(mp3FilePath))

            // Obtener un OutputStream del nuevo archivo MP3 en la MediaStore
            val outputStream = contentResolver.openOutputStream(newMp3Uri)

            // Copiar el contenido del archivo original al nuevo archivo en la MediaStore
            inputStream.use { input ->
                outputStream?.use { output ->
                    input.copyTo(output)
                }
            }

            // Cerrar los flujos de entrada y salida
            inputStream.close()
            outputStream?.close()

            // Eliminar el archivo MP3 original después de copiarlo
            val originalFile = File(mp3FilePath)
            if (originalFile.exists()) {
                return originalFile.delete()
            }
        }

        return false


    }

    fun getPathFromUri(context: Context, uri: Uri): String? {
        val projection = arrayOf(MediaStore.MediaColumns.DATA)
        val cursor = context.contentResolver.query(uri, projection, null, null, null)

        cursor?.use { cursor ->
            if (cursor.moveToFirst()) {
                val columnIndex = cursor.getColumnIndex(MediaStore.MediaColumns.DATA)
                return cursor.getString(columnIndex)
            }
        }

        return null
    }


    @SuppressLint("Range")

    fun getAudioList(filter: String = "%") {
        var newAudioList: MutableList<Audio> = mutableListOf()
        val contentResolver = _appState.value.applicationContext!!.contentResolver
        val projection = arrayOf(
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.YEAR
        )

        val keywordArray = filter.split(" ").toTypedArray()
        val selectionArgs = Array(keywordArray.size + 1) { i ->
            if (i == 0) {
                "%/SleepRecorder/%"
            } else {
                "%${keywordArray[i - 1]}%"
            }
        }
        val selection = buildString {
            append("${MediaStore.Audio.Media.DATA} LIKE ? AND (")
            for (i in keywordArray.indices) {
                append("${MediaStore.Audio.Media.DISPLAY_NAME} LIKE ?")
                if (i < keywordArray.size - 1) {
                    append(" OR ")
                }
            }
            append(")")
        }


        val cursor = contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            "${MediaStore.Audio.Media.DISPLAY_NAME} DESC"
        )

        cursor?.use { cursor ->
            while (cursor.moveToNext()) {
                val displayName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME))
                val category = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM))
                val dateAdded = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.YEAR))
                val album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
                val audio = Audio(displayName, category, dateAdded)
                newAudioList.add(audio)
            }
        }

        _appState.update {
            it.copy(
                audioList = newAudioList
            )
        }

        Log.d("TAGGGGGG",newAudioList.toString())
    }

    @SuppressLint("Range")
    fun getAudioUriByDisplayName(displayName: String): Uri? {
        val contentResolver = _appState.value.applicationContext!!.contentResolver
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(MediaStore.Audio.Media._ID)
        val selection = "${MediaStore.Audio.Media.DISPLAY_NAME} = ?"
        val selectionArgs = arrayOf(displayName)

        val cursor = contentResolver.query(uri, projection, selection, selectionArgs, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val id = it.getLong(it.getColumnIndex(MediaStore.Audio.Media._ID))
                return ContentUris.withAppendedId(uri, id)
            }
        }
        return null
    }

    fun editAudioDisplayNameByDisplayName(displayName: String, newDisplayName :String, date: Long){
        val contentResolver = _appState.value.applicationContext!!.contentResolver
        val uriAudio: Uri? = getAudioUriByDisplayName(displayName)
        // Crear un nuevo archivo con el nuevo nombre
        val nuevoUri = MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        val values = ContentValues().apply {
            put(MediaStore.Audio.Media.DISPLAY_NAME, "${date}_${newDisplayName}.mp3")
            put(MediaStore.Audio.Media.YEAR,date*1000)
            put(MediaStore.Audio.Media.RELATIVE_PATH,"${Environment.DIRECTORY_MUSIC}/SleepRecorder")
            // Puedes agregar más campos para actualizar si es necesario
        }
        val nuevoUriAudio = contentResolver.insert(nuevoUri, values)

        // Abrir un InputStream desde el archivo original y un OutputStream hacia el nuevo archivo
        val inputStream = contentResolver.openInputStream(uriAudio!!)
        val outputStream = nuevoUriAudio?.let { contentResolver.openOutputStream(it) }

        inputStream?.use { input ->
            outputStream?.use { output ->
                // Copiar el contenido del archivo original al nuevo archivo
                input.copyTo(output)
            }
        }

        // Eliminar el archivo original si así lo deseas
        if (nuevoUriAudio != null) {
            contentResolver.delete(uriAudio, null, null)
            getAudioList()
        }
    }

    fun isPendingTaskEmpty():Boolean{
        return _appState.value.pendingTasks.isEmpty()
    }

    fun removePendingTask(){
        for (task in _appState.value.pendingTasks) {
            task.cancel()
        }
        _appState.value.timerStart.cancel()

        _appState.update {
            it.copy(
                pendingTasks = mutableListOf()
            )
        }
    }

    fun updateTryGoBack(newValue : Boolean){
        _appState.update {
            it.copy(
                tryGoBack = newValue
            )
        }
    }

    fun updateAudioType(newValue: AudioTypes){
        _appState.update {
            it.copy(
                audioTypeSelected = newValue
            )
        }
    }

    fun deleteAudioByName(displayName: String){
        val uri =  getAudioUriByDisplayName(displayName)
        val contentResolver = _appState.value.applicationContext!!.contentResolver
        contentResolver.delete(uri!!, null, null)
        getAudioList()

    }

}





