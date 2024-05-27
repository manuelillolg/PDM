package com.example.fridgeapp.data

import androidx.room.Query

class FoodRecordRepository(
    private val foodRecordDao: FoodRecordDao
) {
    suspend fun getFoodRecords():List<FoodRecordEntity>{
        val entities = foodRecordDao.getFoodRecords()
        return entities
    }

    suspend fun insertFoodRecord(foodRecord: FoodRecordEntity): Long{
        val id = foodRecordDao.insertFoodRecord(foodRecord)
        return id
    }
    suspend fun findFoodByName(name: String) :List<FoodRecordEntity>{
        return foodRecordDao.findFoodByName(name)
    }

    suspend fun deleteFoodById(id: Long){
        foodRecordDao.deleteFoodById(id)
    }
}