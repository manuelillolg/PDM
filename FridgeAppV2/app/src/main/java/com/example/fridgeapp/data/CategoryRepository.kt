package com.example.fridgeapp.data

class CategoryRepository(
    private val categoryDao: CategoryDao
) {
    suspend fun  getCategories(): List<CategoryEntity>{
        return categoryDao.getCategories()
    }

    suspend fun insertCategory(category: CategoryEntity): Long{
        return categoryDao.insertCategory(category)
    }

    suspend fun findCategoryByName(name: String): List<CategoryEntity>{
        return categoryDao.findCategory(name)
    }

    suspend fun deleteCategoryEntity(id:Long){
        categoryDao.deleteCategoryEntity(id)
    }



}