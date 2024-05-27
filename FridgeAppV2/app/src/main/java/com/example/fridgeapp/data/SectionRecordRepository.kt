package com.example.fridgeapp.data

class SectionRecordRepository(
    private val sectionRecordDao: SectionRecordDao
) {
    suspend fun getSections():List<SectionRecordEntity>{
        val entities = sectionRecordDao.getSectionRecords()
        return entities
    }

    suspend fun insertSectionRecord(sectionRecord: SectionRecordEntity): Long{
        val id = sectionRecordDao.insertSectionRecord(sectionRecord)
        return id
    }
    suspend fun getSectionRecordsByName(name:String):List<SectionRecordEntity>{
        return sectionRecordDao.getSectionRecordsByName(name)
    }

    suspend fun deleteSectionEntity(id:Long){
        sectionRecordDao.deleteSectionEntity(id)
    }
}