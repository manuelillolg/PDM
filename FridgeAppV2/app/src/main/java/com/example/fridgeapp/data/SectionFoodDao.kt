package com.example.fridgeapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SectionFoodDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSectionFood(sectionFood: SectionFoodEntity): Long

    @Query("SELECT * FROM SectionFoodEntity")
    suspend fun  getSectionFoods(): List<SectionFoodEntity>

    @Query("SELECT * FROM SectionFoodEntity WHERE idFood = :id")
    suspend fun  getSectionFoodsByFoodId(id: Long): List<SectionFoodEntity>

    @Query("DELETE FROM SectionFoodEntity WHERE idFood = :id")
    suspend fun deleteSectionFoodById(id: Long)

    @Query("""
        SELECT * FROM FoodEntity 
        INNER JOIN SectionFoodEntity ON FoodEntity.id = SectionFoodEntity.idFood
        WHERE idSection IN (:ids)
        ORDER BY name
        """)
    suspend fun getFoodsBySectionId(ids: List<Long>): List<FoodEntity>

    @Query(
        "SELECT idFood FROM SectionFoodEntity WHERE idSection = :id"
    )

    suspend fun  getFoodsIdBySectionId(id:Long): List<Long>

    @Query(
        "DELETE  FROM FoodEntity WHERE id in (:ids)"
    )

    suspend fun  deleteFoodInIdList(ids:List<Long>)

    @Query(
        "DELETE  FROM SectionFoodEntity WHERE idSection =:id"
    )

    suspend fun  deleteSectionFoodEntityBySectionId(id:Long)

    @Query(
        "DELETE  FROM ContainerSectionEntity WHERE idSection =:id"
    )

    suspend fun  deleteContainerSectionEntityBySectionId(id:Long)

}