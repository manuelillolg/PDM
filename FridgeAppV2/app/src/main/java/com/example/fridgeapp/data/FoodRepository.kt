package com.example.fridgeapp.data

class FoodRepository(
    private val foodDao: FoodDao
) {
    suspend fun getFoods():List<FoodEntity>{
        val entities = foodDao.getFoods()
        return entities
    }
    suspend fun getFoodsOnce():List<FoodEntity>{
        val entities = foodDao.getFoodsOnce()
        return entities
    }

    suspend fun  getFoodsByName(name:String): List<FoodEntity>{
        val entities = foodDao.getFoodsByName(name)
        return entities
    }

    suspend fun insertFood(food: FoodEntity): Long{
        val id = foodDao.insertFood(food)
        return id
    }
    suspend fun deleteFoodById(id: Long){
        foodDao.deleteFoodById(id)
    }



}