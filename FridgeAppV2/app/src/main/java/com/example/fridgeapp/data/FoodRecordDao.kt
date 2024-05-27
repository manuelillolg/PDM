package com.example.fridgeapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FoodRecordDao {
    @Insert
    suspend fun insertFoodRecord(foodRecord: FoodRecordEntity): Long

    @Query("SELECT * FROM FoodRecordEntity ORDER BY name")
    suspend fun  getFoodRecords(): List<FoodRecordEntity>

    @Query("SELECT * FROM FoodRecordEntity WHERE name= :name ORDER BY name ")
    suspend fun findFoodByName(name: String) :List<FoodRecordEntity>

    @Query("DELETE FROM FoodRecordEntity WHERE id = :id")
    suspend fun deleteFoodById(id: Long)


}