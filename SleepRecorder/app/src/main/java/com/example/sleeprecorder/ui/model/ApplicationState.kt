package com.example.sleeprecorder.ui.model

import android.content.Context
import android.media.AudioRecord
import java.util.Timer
import java.util.TimerTask

data class ApplicationState (
    val db : Float = 57.5f,
    val silenceTime: Int = 3,
    var isRecording :Boolean = false,
    var audioRecord: AudioRecord? = null,
    var applicationContext: Context? = null,
    var isInit : Boolean = false,
    var isListening : Boolean = false,
    var isNoiseRecorded: Boolean = false,
    var hourInit:Int = 0,
    var minuteInit:Int = 0,
    var hourEnd:Int = 0,
    var minuteEnd:Int = 0,
    var pendingTasks: MutableList<TimerTask> = mutableListOf(),
    var audioList: MutableList<Audio> = mutableListOf(),
    var timerStart: Timer = Timer(),
    var timerEnd: Timer = Timer(),
    var tryGoBack:Boolean = false,
    var audioTypeSelected: AudioTypes = AudioTypes.RONQUIDO
)

data class Audio(
    val displayName: String,
    val category: String,
    val dateAdded: Long
)

enum class AudioTypes {
    VOZ,
    RUIDO,
    RONQUIDO
}