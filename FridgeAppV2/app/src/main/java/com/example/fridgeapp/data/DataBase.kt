package com.example.fridgeapp.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        ContainerEntity::class,
        ContainerSectionEntity::class,
        SectionEntity::class,
        SectionFoodEntity::class,
        FoodEntity::class,
        FoodRecordEntity::class,
        SectionRecordEntity::class,
        CategoryEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class DataBase : RoomDatabase(){
    abstract val containerDao: ContainerDao
    abstract val containerSectionDao: ContainerSectionDao
    abstract val sectionDao: SectionDao
    abstract val sectionFoodDao: SectionFoodDao
    abstract val foodDao: FoodDao
    abstract val foodRecordDao: FoodRecordDao
    abstract val sectionRecordDao: SectionRecordDao
    abstract val categoryDao: CategoryDao
}