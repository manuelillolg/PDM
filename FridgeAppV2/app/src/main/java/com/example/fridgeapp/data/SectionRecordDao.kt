package com.example.fridgeapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SectionRecordDao {
    @Insert
    suspend fun insertSectionRecord(sectionRecord: SectionRecordEntity): Long

    @Query("SELECT * FROM SectionRecordEntity ORDER BY name")
    suspend fun  getSectionRecords(): List<SectionRecordEntity>

    @Query("SELECT * FROM SectionRecordEntity WHERE name = :name")
    suspend fun getSectionRecordsByName(name:String):List<SectionRecordEntity>

    @Query("DELETE FROM SectionRecordEntity WHERE id  =:id")
    suspend fun deleteSectionEntity(id:Long)
}