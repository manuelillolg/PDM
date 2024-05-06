package com.example.sleeprecorder.components

import android.media.MediaRecorder
import java.io.File
import java.io.IOException

class AudioRecorder(private val outputFile: String) {
    private var mediaRecorder: MediaRecorder? = null
    private var isRecording = false

    fun startRecording() {
        if (!isRecording) {
            mediaRecorder = MediaRecorder()
            mediaRecorder?.apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setOutputFile(outputFile)

                try {
                    prepare()
                    start()
                    isRecording = true
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun stopRecording() {
        if (isRecording) {
            mediaRecorder?.apply {
                stop()
                release()
            }
            isRecording = false
            mediaRecorder = null
        }
    }
}
