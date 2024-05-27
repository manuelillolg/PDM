package com.example.fridgeapp

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.room.Room
import com.example.fridgeapp.data.CategoryEntity
import com.example.fridgeapp.data.CategoryRepository
import com.example.fridgeapp.data.ContainerEntity
import com.example.fridgeapp.data.ContainerRepository
import com.example.fridgeapp.data.ContainerSectionEntity
import com.example.fridgeapp.data.ContainerSectionRepository
import com.example.fridgeapp.data.DataBase
import com.example.fridgeapp.data.FoodRecordEntity
import com.example.fridgeapp.data.FoodRecordRepository
import com.example.fridgeapp.data.FoodRepository
import com.example.fridgeapp.data.SectionEntity
import com.example.fridgeapp.data.SectionFoodRepository
import com.example.fridgeapp.data.SectionRecordEntity
import com.example.fridgeapp.data.SectionRecordRepository
import com.example.fridgeapp.data.SectionRepository
import com.example.fridgeapp.model.Application
import com.example.fridgeapp.ui.theme.FridgeAppTheme
import com.example.fridgeapp.utils.readJSONFromAssets
import kotlinx.coroutines.runBlocking
import java.io.File
import java.io.IOException
import java.io.InputStream

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dbFile = File(getDatabasePath("FridgeDB").toString())
        val dbExists = dbFile.exists()
        val db = Room.databaseBuilder(this,DataBase::class.java,"FridgeDB").build()

        val containerDao = db.containerDao
        val containerRepository = ContainerRepository(containerDao)
        val sectionDao = db.sectionDao
        val sectionRepository = SectionRepository(sectionDao)
        val containerSectionDao = db.containerSectionDao
        val containerSectionRepository = ContainerSectionRepository(containerSectionDao)
        val sectionRecordDao = db.sectionRecordDao
        val sectionRecordRepository = SectionRecordRepository(sectionRecordDao)
        val foodRecordDao = db.foodRecordDao
        val foodRecordRepository = FoodRecordRepository(foodRecordDao)
        val foodDao = db.foodDao
        val foodRepository = FoodRepository(foodDao)
        val sectionFoodDao = db.sectionFoodDao
        val sectionFoodRepository = SectionFoodRepository(sectionFoodDao)
        val categoryDao = db.categoryDao
        val categoryRepository = CategoryRepository(categoryDao)


        if(!dbExists){
            var containerId: Long
            var sectionId: Long

            runBlocking {
                //FRIGORIFICO
                containerId = containerRepository.insertContainer(ContainerEntity(name="FRIGORIFICO"))
                sectionId = sectionRepository.insertSection(SectionEntity(name="CAJON FRUTA/VERDURA"))
                containerSectionRepository.insertContainerSection(ContainerSectionEntity(
                    idContainer = containerId,
                    idSection = sectionId
                ))
                sectionId = sectionRepository.insertSection(SectionEntity(name="CAJON CARNE/PESCADO"))
                containerSectionRepository.insertContainerSection(ContainerSectionEntity(
                    idContainer = containerId,
                    idSection = sectionId
                ))
                sectionId = sectionRepository.insertSection(SectionEntity(name="BALDA 1"))
                containerSectionRepository.insertContainerSection(ContainerSectionEntity(
                    idContainer = containerId,
                    idSection = sectionId
                ))
                sectionId = sectionRepository.insertSection(SectionEntity(name="BALDA 2"))
                containerSectionRepository.insertContainerSection(ContainerSectionEntity(
                    idContainer = containerId,
                    idSection = sectionId
                ))
                sectionId = sectionRepository.insertSection(SectionEntity(name="BALDA 3"))
                containerSectionRepository.insertContainerSection(ContainerSectionEntity(
                    idContainer = containerId,
                    idSection = sectionId
                ))

                //CONGELADOR
                containerId = containerRepository.insertContainer(ContainerEntity(name="CONGELADOR"))
                sectionId = sectionRepository.insertSection(SectionEntity(name="CAJON 1"))
                containerSectionRepository.insertContainerSection(ContainerSectionEntity(
                    idContainer = containerId,
                    idSection = sectionId
                ))
                sectionId = sectionRepository.insertSection(SectionEntity(name="CAJON 2"))
                containerSectionRepository.insertContainerSection(ContainerSectionEntity(
                    idContainer = containerId,
                    idSection = sectionId
                ))
                sectionId = sectionRepository.insertSection(SectionEntity(name="CAJON 3"))
                containerSectionRepository.insertContainerSection(ContainerSectionEntity(
                    idContainer = containerId,
                    idSection = sectionId
                ))

                //Guardamos las secciones en el historial de secciones para que puedan ser reutilizadas
                sectionRecordRepository.insertSectionRecord(SectionRecordEntity(name="CAJON 1"))
                sectionRecordRepository.insertSectionRecord(SectionRecordEntity(name="CAJON 2"))
                sectionRecordRepository.insertSectionRecord(SectionRecordEntity(name="CAJON 3"))
                sectionRecordRepository.insertSectionRecord(SectionRecordEntity(name="BALDA 1"))
                sectionRecordRepository.insertSectionRecord(SectionRecordEntity(name="BALDA 2"))
                sectionRecordRepository.insertSectionRecord(SectionRecordEntity(name="BALDA 3"))
                sectionRecordRepository.insertSectionRecord(SectionRecordEntity(name="CAJON CARNE-PESCADO"))
                sectionRecordRepository.insertSectionRecord(SectionRecordEntity(name="CAJON FRUTA-VERDURA"))
                foodRecordRepository.insertFoodRecord(FoodRecordEntity(name="LECHUGA", category = "VERDURA"))
                foodRecordRepository.insertFoodRecord(FoodRecordEntity(name="POLLO", category = "CARNE"))
                categoryRepository.insertCategory(CategoryEntity(name="VERDURA"))
                categoryRepository.insertCategory(CategoryEntity(name="CARNE"))

            }

        }





        enableEdgeToEdge()
        setContent {
            FridgeAppTheme {
                FridgeApp(
                    jsonString =  loadJson(),
                    context = applicationContext,
                    containerRepository = containerRepository,
                    sectionRepository = sectionRepository,
                    containerSectionRepository = containerSectionRepository,
                    sectionRecordRepository = sectionRecordRepository,
                    foodRecordRepository = foodRecordRepository,
                    foodRepository = foodRepository,
                    sectionFoodRepository = sectionFoodRepository,
                    categoryRepository = categoryRepository
                )
            }
        }
    }

    private fun loadJson(): String? {
        val jsonFileString = readJSONFromAssets(context = applicationContext, fileName="data.json")
        return jsonFileString
    }
}



