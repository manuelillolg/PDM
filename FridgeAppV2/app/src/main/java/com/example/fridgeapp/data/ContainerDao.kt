package com.example.fridgeapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ContainerDao {
    @Insert
    suspend fun insertContainer(container: ContainerEntity): Long

    @Query("SELECT * FROM ContainerEntity ORDER BY name")
    suspend fun  getContainers(): List<ContainerEntity>

    @Query("SELECT * FROM ContainerEntity WHERE name = :name")
    suspend fun getContainersByName(name:String): List<ContainerEntity>

    @Query("DELETE FROM ContainerEntity WHERE id =:id")
    suspend fun deleteContainerById(id:Long)
}