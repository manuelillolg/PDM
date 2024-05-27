package com.example.fridgeapp.data

class SectionRepository(
    private val sectionDao: SectionDao
) {
    suspend fun getSections():List<SectionEntity>{
        val entities = sectionDao.getSections()
        return entities
    }

    suspend fun insertSection(section: SectionEntity): Long{
        val id = sectionDao.insertSection(section)
        return id
    }

    suspend fun deleteSection(id :Long){
        sectionDao.deleteSection(id)
    }
}