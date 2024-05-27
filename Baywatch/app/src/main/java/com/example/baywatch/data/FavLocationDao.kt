package com.example.baywatch.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FavLocationDao {
    @Insert
    suspend fun insertFavLocation(location:FavLocationEntity)

    @Query("SELECT locationId FROM FavLocationEntity")
    suspend fun getAllFavLocation():List<String>

    @Query("DELETE FROM FavLocationEntity WHERE locationId = :id")
    suspend fun deleteFavLocation(id:String)

    @Query("SELECT locationId FROM FavLocationEntity WHERE locationId  =:locationId")
    suspend fun getLocation(locationId : String):List<String>
}