package com.example.baywatch.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        FavLocationEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class DataBase : RoomDatabase(){
    abstract val favLocationDao: FavLocationDao
}