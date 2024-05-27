package com.example.fridgeapp.data

import androidx.room.Query
import kotlinx.coroutines.runBlocking

class SectionFoodRepository(
    private val sectionFoodDao: SectionFoodDao
) {
    suspend fun getSectionFoods():List<SectionFoodEntity>{
        val entities = sectionFoodDao.getSectionFoods()
        return entities
    }

    suspend fun insertSectionFood(sectionFood: SectionFoodEntity): Long{
        val id = sectionFoodDao.insertSectionFood(sectionFood)
        return id
    }
    suspend fun getSectionFoodsByFoodId(id: Long): List<SectionFoodEntity>{
        val entities = sectionFoodDao.getSectionFoodsByFoodId(id)
        return entities
    }

    suspend fun deleteSectionFoodByFoodId(id: Long){
        sectionFoodDao.deleteSectionFoodById(id)
    }

    suspend fun getFoodsBySectionId(ids: List<Long>): List<FoodEntity>{
        return sectionFoodDao.getFoodsBySectionId(ids)
    }

    suspend fun  getFoodsIdBySectionId(id:Long): List<Long>{
        return sectionFoodDao.getFoodsIdBySectionId(id)
    }


    suspend fun  deleteFoodInIdList(ids:List<Long>){
        sectionFoodDao.deleteFoodInIdList(ids)
    }


    suspend fun  deleteSectionFoodEntityBySectionId(id:Long){
        sectionFoodDao.deleteSectionFoodEntityBySectionId(id)
    }

    fun deleteReferenceToSection(id:Long){
        var idFoodList: List<Long>
        runBlocking {
            idFoodList = getFoodsIdBySectionId(id)
            deleteFoodInIdList(idFoodList)
            deleteSectionFoodEntityBySectionId(id)
            sectionFoodDao.deleteContainerSectionEntityBySectionId(id)
        }
    }
}