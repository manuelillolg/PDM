package com.example.fridgeapp.data

class ContainerRepository(
    private val containerDao: ContainerDao
) {
    suspend fun getContainers():List<ContainerEntity>{
        val entities = containerDao.getContainers()
        return entities
    }

    suspend fun insertContainer(container: ContainerEntity): Long{
        val id = containerDao.insertContainer(container)
        return id
    }

    suspend fun getContainersByName(name:String): List<ContainerEntity>{
        return containerDao.getContainersByName(name)
    }

    suspend fun deleteContainerById(id:Long){
        containerDao.deleteContainerById(id)
    }
}