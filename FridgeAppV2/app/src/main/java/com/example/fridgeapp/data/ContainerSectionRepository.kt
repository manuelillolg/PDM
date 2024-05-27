package com.example.fridgeapp.data

class ContainerSectionRepository(
    private val containerSectionDao: ContainerSectionDao
) {
    suspend fun getContainerSections():List<ContainerSectionEntity>{
        val entities = containerSectionDao.getContainerSections()
        return entities
    }

    suspend fun insertContainerSection(container: ContainerSectionEntity): Long{
        val id = containerSectionDao.insertContainerSection(container)
        return id
    }

    suspend fun getSectionsByContainerId(id:Long): List<SectionEntity>{
        val entities = containerSectionDao.getSectionsByContainerId(id)
        return entities
    }
}