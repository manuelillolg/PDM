package com.example.fridgeapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FoodDao {
    @Insert
    suspend fun insertFood(food: FoodEntity): Long

    @Query("SELECT * FROM FoodEntity")
    suspend fun  getFoods(): List<FoodEntity>

    @Query("SELECT * FROM FoodEntity WHERE id IN (SELECT MIN(id) FROM FoodEntity GROUP BY name )ORDER BY name")
    suspend fun getFoodsOnce():List<FoodEntity>

    @Query("SELECT * FROM FoodEntity WHERE name = :name ORDER BY id")
    suspend fun  getFoodsByName(name:String): List<FoodEntity>

    @Query("DELETE FROM FoodEntity WHERE id = :id")
    suspend fun deleteFoodById(id: Long)


}