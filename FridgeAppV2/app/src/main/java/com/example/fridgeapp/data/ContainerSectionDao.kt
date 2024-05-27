package com.example.fridgeapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ContainerSectionDao {
    @Insert
    suspend fun insertContainerSection(containerSection: ContainerSectionEntity): Long

    @Query("SELECT * FROM ContainerSectionEntity")
    suspend fun  getContainerSections(): List<ContainerSectionEntity>

    @Query("""
        SELECT * FROM SectionEntity 
        INNER JOIN ContainerSectionEntity ON SectionEntity.id = ContainerSectionEntity.idSection
        WHERE idContainer= :containerId 
        ORDER BY name
        """)
    suspend fun  getSectionsByContainerId(containerId: Long): List<SectionEntity>
}