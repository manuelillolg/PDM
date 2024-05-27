package com.example.fridgeapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SectionDao {
    @Insert
    suspend fun insertSection(section: SectionEntity): Long

    @Query("SELECT * FROM SectionEntity")
    suspend fun  getSections(): List<SectionEntity>

    @Query("DELETE FROM SectionEntity WHERE id = :id")
    suspend fun deleteSection(id :Long)
}