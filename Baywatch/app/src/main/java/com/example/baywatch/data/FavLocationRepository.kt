package com.example.baywatch.data

import androidx.room.Insert
import androidx.room.Query

class FavLocationRepository(
    private val favLocationDao: FavLocationDao
) {
    suspend fun insertFavLocation(location:FavLocationEntity){
        favLocationDao.insertFavLocation(location)
    }


    suspend fun getAllFavLocation():List<String>{
        return favLocationDao.getAllFavLocation()
    }


    suspend fun deleteFavLocation(id:String){
        favLocationDao.deleteFavLocation(id)
    }
    suspend fun getLocation(locationId : String):List<String>{
        return favLocationDao.getLocation(locationId)
    }
}