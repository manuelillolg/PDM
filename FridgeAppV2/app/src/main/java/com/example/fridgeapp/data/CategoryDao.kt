package com.example.fridgeapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CategoryDao {
    @Insert
    suspend fun insertCategory(category: CategoryEntity): Long

    @Query("SELECT * FROM CategoryEntity ORDER BY name")
    suspend fun  getCategories(): List<CategoryEntity>

    @Query("SELECT * FROM CategoryEntity WHERE name = :name ORDER BY name")
    suspend fun  findCategory(name : String): List<CategoryEntity>

    @Query("DELETE FROM CategoryEntity WHERE id  =:id")
    suspend fun deleteCategoryEntity(id:Long)




}