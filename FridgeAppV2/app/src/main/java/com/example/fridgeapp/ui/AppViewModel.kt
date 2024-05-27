package com.example.fridgeapp.ui

import android.content.Context
import android.provider.Settings.Global.getString
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.room.util.copy
import com.example.fridgeapp.R
import com.example.fridgeapp.data.CategoryEntity
import com.example.fridgeapp.data.CategoryRepository
import com.example.fridgeapp.data.ContainerEntity
import com.example.fridgeapp.data.ContainerRepository
import com.example.fridgeapp.data.ContainerSectionEntity
import com.example.fridgeapp.data.ContainerSectionRepository
import com.example.fridgeapp.data.FoodEntity
import com.example.fridgeapp.data.FoodRecordEntity
import com.example.fridgeapp.data.FoodRecordRepository
import com.example.fridgeapp.data.FoodRepository
import com.example.fridgeapp.data.FoodV2
import com.example.fridgeapp.data.SectionEntity
import com.example.fridgeapp.data.SectionFoodEntity
import com.example.fridgeapp.data.SectionFoodRepository
import com.example.fridgeapp.data.SectionRecordEntity
import com.example.fridgeapp.data.SectionRecordRepository
import com.example.fridgeapp.data.SectionRepository
import com.example.fridgeapp.model.Application
import com.example.fridgeapp.model.ApplicationState
import com.example.fridgeapp.model.Container
import com.example.fridgeapp.model.Food
import com.example.fridgeapp.model.Route
import com.example.fridgeapp.utils.readJSONFromAssets
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.runBlocking
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream


class AppViewModel (

): ViewModel() {

    private lateinit var _appState : MutableStateFlow<ApplicationState>
    lateinit var appState: StateFlow<ApplicationState>
    private lateinit var containerRepository : ContainerRepository
    private lateinit var sectionRepository : SectionRepository
    private lateinit var containerSectionRepository : ContainerSectionRepository
    private lateinit var sectionRecordRepository : SectionRecordRepository
    private lateinit var foodRecordRepository : FoodRecordRepository
    private lateinit var foodRepository : FoodRepository
    private lateinit var sectionFoodRepository: SectionFoodRepository
    private lateinit var categoryRepository: CategoryRepository


    fun getAllFoods():Unit{
       var foods : List<FoodEntity>

       runBlocking {
          foods =  foodRepository.getFoodsOnce()
       }

        _appState.update { currentState ->
            currentState.copy(
                allFoods = foods
            )
        }

    }

    fun getFoodByName(name: String){
        var foods : List<FoodEntity>
        var foodsV2: MutableList<FoodV2> = mutableListOf()

        runBlocking {
            foods =  foodRepository.getFoodsByName(name)
        }

        foods.forEach{
            var foodInfo: SectionFoodEntity

            runBlocking {
                foodInfo=sectionFoodRepository.getSectionFoodsByFoodId(it.id!!)[0]
            }

            foodsV2.add(
                FoodV2(
                    food=it,
                    quantity = foodInfo.quantity.toString(),
                    expirationDate = foodInfo.expirationDate,
                    purchaseDate = foodInfo.purchaseDate,
                    comment = foodInfo.comment,
                    location = foodInfo.location
                )
            )
        }
        _appState.update { currentState ->
            currentState.copy(
                allFoodsByName = foodsV2
            )
        }
    }

    fun getAllFoodsV2(){
        var foods : List<FoodRecordEntity>

        runBlocking {
            foods = foodRecordRepository.getFoodRecords()
        }

        _appState.update {
            it.copy(
                allRecordFoods = foods
            )
        }

    }


    fun isValidQuantity(){
       val quantity =  _appState.value.provisionalQuantity
        var valid : Boolean = false
        if(quantity.toIntOrNull() != null && quantity.toInt() > 0){
            valid = true
        }

        _appState.update {
            it.copy(
                isQuantityCorrect = valid
            )
        }
    }

    fun updateQuantity(quantity: String){

        _appState.update {
            it.copy(
                provisionalQuantity = quantity
            )
        }
    }
    fun getContainersWithFoodSelected(){
        var containers = sortedSetOf<String>().toMutableSet()
        val foodName = _appState.value.foodSelected

        for (container in _appState.value.application.containers) {

            for (section in container.sections) {

                for (food in section.foods) {
                    if(food.name == foodName){
                        containers.add(container.name)
                    }

                }
            }
        }

        containers = containers.sorted().toMutableSet();

        _appState.update {currentState ->
            currentState.copy(
                containersWithFoodName = containers,
                containerNameSelected = containers.toList()[0]
            )
        }
    }

    fun deleteFoodById(id:Long){
        runBlocking{
            foodRepository.deleteFoodById(id)
            sectionFoodRepository.deleteSectionFoodByFoodId(id)
        }
    }

    fun deleteFoodRecordById(id:Long){
        runBlocking{
            foodRecordRepository.deleteFoodById(id)
        }
    }

    fun deleteCategoryRecordById(id:Long){
        runBlocking{
            categoryRepository.deleteCategoryEntity(id)
        }
    }

    fun deleteSectionRecordById(id:Long){
        runBlocking{
            sectionRecordRepository.deleteSectionEntity(id)
        }
    }

    fun getFoodOfContainer( ) {
        val foods = mutableListOf<Pair<Food,Route>>()

        _appState.value.application.containers.forEachIndexed { containerIndex, container ->


            if(container.name == _appState.value.containerNameSelected) {

                container.sections.forEachIndexed { sectionIndex, section ->

                    section.foods.forEachIndexed { foodIndex, food ->

                        if(food.name == _appState.value.foodSelected){

                            foods.add(Pair(
                                food,
                                Route(containerIndex=containerIndex,sectionIndex = sectionIndex, foodIndex = foodIndex)))
                        }


                    }
                }
            }
        }

        _appState.update {currentState ->
            currentState.copy(
                foodByContainer = foods
            )
        }
    }

    fun updateFoodSelected(foodName: String){
        _appState.update {currentState ->
            currentState.copy(
                foodSelected = foodName
            )
        }
    }

    fun updateContainerSelected(containerName: String){
        _appState.update {currentState ->
            currentState.copy(
                containerNameSelected = containerName
            )
        }
    }

    fun updateContainerSelectedV2(container: ContainerEntity){
        _appState.update {currentState ->
            currentState.copy(
                containerSelected = container
            )
        }
    }

    //Funciones para la ventana de edición de alimento

    fun updateSelectedFoodEdit(food:FoodV2){
        var newFood = food.copy()
        _appState.update {currentState ->
            currentState.copy(
                foodV2=newFood
            )
        }
    }

    fun saveUpdatedFoodV2(food: FoodV2){
        var sectionFood : SectionFoodEntity

        runBlocking {
            sectionFood = sectionFoodRepository.getSectionFoodsByFoodId(food.food!!.id!!)[0]
        }

        sectionFood.quantity = food.quantity.toInt()
        sectionFood.comment = food.comment
        sectionFood.purchaseDate = food.purchaseDate!!
        sectionFood.expirationDate = food.expirationDate

        runBlocking{ sectionFoodRepository.insertSectionFood(sectionFood) }

    }

    fun saveUpdatedLocationFoodV2(food: FoodV2){
        var sectionFood : SectionFoodEntity

        runBlocking {
            sectionFood = sectionFoodRepository.getSectionFoodsByFoodId(food.food!!.id!!)[0]
        }

        sectionFood.location = "${_appState.value.containerSelected!!.name}/${_appState.value.sectionSelected!!.name}"
        sectionFood.idSection = _appState.value.sectionSelected!!.id!!
        runBlocking{ sectionFoodRepository.insertSectionFood(sectionFood) }

    }
    fun updateProvisionalFoodNameEdit(foodName:String){
        _appState.update {currentState ->
            currentState.copy(
                provisionalFoodNameEdited = foodName
            )
        }

    }

    fun changeFoodName(oldName:String){
        _appState.value.application.containers.forEach { container ->
            container.sections.forEach{section->
                section.foods.forEach{food ->
                    if (food.name == oldName){
                        food.name = _appState.value.provisionalFoodNameEdited
                    }

                }

            }

        }
        _appState.value.application.foodNameList.remove(oldName)
        _appState.value.application.foodNameList.add(_appState.value.provisionalFoodNameEdited)
    }

    //Funciones para añadir nuevo alimento
    fun getAllPossibleFoods(){
        /*var foods =  sortedSetOf<String>().toMutableSet()

        _appState.value.application.foodNameList.forEach{possibleFood ->
            foods.add(possibleFood)
        }

        foods = foods.sorted().toMutableSet()

        _appState.update { currentState ->
            currentState.copy(
                allFoods = foods
            )
        }


        updateFoodSelected(foods.toList()[0])*/ //TODO
    }

    fun saveFood(name: String, category: String){
        runBlocking{
            foodRecordRepository.insertFoodRecord(
                FoodRecordEntity(
                    name = name,
                    category = category
                )
            )
        }
    }

    fun getAllFoodsOfContainer(container:ContainerEntity){
        var sections: List<SectionEntity>
        var sectionsIds : MutableList<Long>  = mutableListOf()
        var foodsByContainer: List<FoodEntity>
        var foods: MutableList<FoodV2> = mutableListOf()

        runBlocking {
            sections = containerSectionRepository.getSectionsByContainerId(container.id!!)
        }

        sections.forEach {
            sectionsIds.add(it.id!!)
        }

        runBlocking {
            foodsByContainer = sectionFoodRepository.getFoodsBySectionId(sectionsIds)
        }

        foodsByContainer.forEach {
            var sectionFood : SectionFoodEntity
            runBlocking{ sectionFood = sectionFoodRepository.getSectionFoodsByFoodId(it.id!!)[0] }
            foods.add(
                FoodV2(
                food = it,
                quantity = sectionFood.quantity.toString(),
                expirationDate = sectionFood.expirationDate,
                purchaseDate = sectionFood.purchaseDate,
                comment = sectionFood.comment,
                location = sectionFood.location
            )
            )
        }

        _appState.update {
            it.copy(
                allFoodsOfContainer = foods
            )
        }


    }

    fun updateSearchingContainer(container: ContainerEntity){
        _appState.update {
            it.copy(
                searchingContainer = container
            )
        }
    }

    fun getAllCategories(){

        var categories : List<CategoryEntity>

        runBlocking {
            categories = categoryRepository.getCategories()
        }

        _appState.update {
            it.copy(
                allCategories = categories
            )
        }
    }

    fun isValidFood(name:String):Boolean{
        var foods : List<FoodRecordEntity>

        runBlocking {
            foods = foodRecordRepository.findFoodByName(name)
        }

        _appState.update {
            it.copy(
                isValidFood = foods.isEmpty()
            )
        }

        return foods.isEmpty()
    }

    fun isValidCategory(name: String): Boolean{
        var categories : List<CategoryEntity>

        runBlocking {
            categories = categoryRepository.findCategoryByName(name)
        }
        Log.d("TAGGGGG",categories.toString())
        Log.d("TAGGGGG",categories.isEmpty().toString())
        _appState.update {
            it.copy(
                validCategory = categories.isEmpty()
            )
        }

        return categories.isEmpty()
    }

    fun updateFindFoodContainerStep(newStep: Int){
        _appState.update {
            it.copy(
                findFoodContainerStep=newStep
            )
        }
    }
    fun getAllSectionRecord(){
        var sectionRecords : List<SectionRecordEntity>
        runBlocking {
            sectionRecords = sectionRecordRepository.getSections()
        }
        _appState.update {
            it.copy(
               sectionsRecordList = sectionRecords
            )
        }

    }

    fun deleteContainer(id:Long){
        var sections: List<SectionEntity>
        runBlocking {
            sections = containerSectionRepository.getSectionsByContainerId(id)
        }

        sections.forEach {
            deleteSection(it.id!!)
        }

        runBlocking { containerRepository.deleteContainerById(id) }
    }

    fun saveSection(name: String){
        runBlocking {
            sectionRecordRepository.insertSectionRecord(SectionRecordEntity(name=name))
        }
    }
    fun isValidSection(name: String):Boolean{
        var sections : List<SectionRecordEntity>

        runBlocking { sections = sectionRecordRepository.getSectionRecordsByName(name)}

        _appState.update {
            it.copy(
                validSection = sections.isEmpty()
            )
        }

        return sections.isEmpty()
    }

    fun isValidContainer(name:String):Boolean{
        var containerList: List<ContainerEntity>
        runBlocking {
            containerList = containerRepository.getContainersByName(name)
        }

        _appState.update {
            it.copy(
                validContainer = containerList.isEmpty()
            )
        }

        return containerList.isEmpty()
    }

    fun saveContainer(name:String){
        var idContainer : Long
        runBlocking {
            idContainer = containerRepository.insertContainer(ContainerEntity(name= name))
        }

        var nameSection : String
        var idSection:Long
        runBlocking{ nameSection = sectionRecordRepository.getSections()[0].name }
        runBlocking { idSection=sectionRepository.insertSection(SectionEntity(name = nameSection)) }
        runBlocking { containerSectionRepository.insertContainerSection(ContainerSectionEntity(idContainer= idContainer, idSection = idSection)) }

    }

    fun updateSectionsOfContainer(section : SectionRecordEntity){
       var idSection : Long
        runBlocking { idSection=sectionRepository.insertSection(SectionEntity(name=section.name)) }
        runBlocking { containerSectionRepository.insertContainerSection(
            ContainerSectionEntity(
                idContainer = _appState.value.containerSelected!!.id!!,
                idSection = idSection
            )
        ) }
    }

    fun deleteSection(id: Long){
        sectionFoodRepository.deleteReferenceToSection(id)
        Log.d("TAGGG",id.toString())
        runBlocking{ sectionRepository.deleteSection(id) }
    }

    fun saveCategory(name: String){
        runBlocking {
            categoryRepository.insertCategory(CategoryEntity(name=name))
        }
    }



    fun updateCategorySelected(category: CategoryEntity){
        _appState.update {
            it.copy(
                categorySelected = category
            )
        }
    }

    fun getAllContainers(){
        var containers: List<ContainerEntity>

        runBlocking {
           containers =  containerRepository.getContainers()
        }


        _appState.update { currentState ->
            currentState.copy(
                allContainers = containers
            )
        }

        if(containers.isNotEmpty())
            updateContainerSelectedV2(containers[0])
    }

    fun updateRecordFoodSelected(food: FoodRecordEntity){
        _appState.update {
            it.copy(
               recordFoodSelected = food
            )
        }
    }

    fun updateFoodV2(food: FoodV2){

        /*var foodV2 : FoodV2 = FoodV2().copy(
            food = food,
            quantity = _appState.value.foodV2.quantity,
            comment = _appState.value.foodV2.comment,
            expirationDate = _appState.value.foodV2.expirationDate,
            purchaseDate = _appState.value.foodV2.purchaseDate
        )*/

        _appState.update {
            it.copy(
                foodV2 = food
            )
        }
    }

    fun updateProvisionalComment(comment: String){
        _appState.update {
            it.copy(
                provisionalComment = comment
            )
        }
    }

    fun addNewProvisionalFood(){
        var foods = _appState.value.foodsToAdd
        foods.add(_appState.value.foodV2)

        _appState.update {
            it.copy(
                foodsToAdd = foods
            )
        }

    }

    fun deleteProvisionalFood(index: Int){
        var foods = _appState.value.foodsToAdd.toMutableList()
        foods.removeAt(index)
        _appState.update {
            it.copy(
                foodsToAdd = foods
            )
        }
    }

    fun saveProvisionalFoodToAddList(){
        var foods = _appState.value.foodsToAdd.toMutableList()
        foods.forEach{
            runBlocking {
                var foodId = foodRepository.insertFood(it.food!!)
                var sectionFoodEntity:SectionFoodEntity = SectionFoodEntity(
                    idSection = _appState.value.sectionSelected!!.id!!,
                    idFood =foodId,
                    comment = it.comment,
                    quantity = it.quantity.toInt(),
                    purchaseDate = it.purchaseDate!!,
                    location = it.location
                )
                sectionFoodRepository.insertSectionFood(sectionFoodEntity)
            }

        }


    }

    fun restartProvisionalFoodToAdd(foods: MutableList<FoodV2>){
        _appState.update {
            it.copy(
                foodsToAdd = foods
            )
        }
    }

    fun getAllSectionsOfContainer(){

        var sections : List<SectionEntity>
        if(_appState.value.containerSelected != null ){
            var containerId = _appState.value.containerSelected!!.id

            runBlocking {
                sections = containerSectionRepository.getSectionsByContainerId(containerId!!)
            }

            _appState.update {
                it.copy(
                    sectionsOfContainer = sections
                )
            }

            updateSectionSelected(sections[0])
        }

    }

    fun updateProvisionalQuantity(quantity :String){
        _appState.update { currentState ->
            currentState.copy(
                provisionalQuantity = quantity
            )
        }
    }

    fun updateCategory(category:String){
        _appState.update { currentState ->
            currentState.copy(
                categoryNameSelected = category
            )
        }
    }

    fun validateQuantity(){
        var isCorrect :Boolean = false
        try {
            if(_appState.value.provisionalQuantity.toInt() > 0){
                isCorrect = true
            }
        }catch(e :Exception){
            Log.d("TAG","Not an integer")
        }

        _appState.update { currentState ->
            currentState.copy(
                isQuantityCorrect = isCorrect
            )
        }
    }
    fun updateSectionSelected(section : SectionEntity){
        _appState.update { currentState ->
            currentState.copy(
                sectionSelected = section
            )
        }
    }

    fun addFood():Boolean{
        /*validateQuantity()

        if(_appState.value.isQuantityCorrect){
            _appState.value.application.containers.forEach{container ->
                if(container.name == _appState.value.containerNameSelected)
                    container.sections.forEach { section->
                        if(section.name == _appState.value.sectionNameSelected) {

                            section.foods.add(
                                Food(
                                    name = _appState.value.foodSelected,
                                    category = _appState.value.categoryNameSelected,
                                    quantity = _appState.value.provisionalQuantity.toInt(),
                                    location = "${_appState.value.containerNameSelected}/${_appState.value.sectionNameSelected}",
                                )
                            )

                        }
                    }
            }

           writeJson()
            return true
        }*/
        return false
    }

    fun addNewFood():Boolean{
        isValidFood()

        if(_appState.value.isValidFood){
            _appState.value.application.foodNameList.add(_appState.value.foodSelected)
            getAllPossibleFoods()
            writeJson()
        }

        return _appState.value.isValidFood
    }

    fun cancelNewFood(){
       // updateFoodSelected(_appState.value.allFoods.toList()[0])
    }

    fun isValidFood(){
        var isValid : Boolean = true

        if(_appState.value.foodSelected.isBlank()){
            isValid = false
        }

        _appState.update { currentState ->
            currentState.copy(
                isValidFood = isValid
            )
        }

    }

    fun initModel(
        jsonString:String,
        context:Context,
        containerRepository : ContainerRepository,
        sectionRepository : SectionRepository,
        containerSectionRepository : ContainerSectionRepository,
        sectionRecordRepository : SectionRecordRepository,
        foodRecordRepository : FoodRecordRepository,
        foodRepository : FoodRepository,
        sectionFoodRepository: SectionFoodRepository,
        categoryRepository: CategoryRepository
        ){

        if(! ::_appState.isInitialized) {
            /*
            //Copiamos el json en almacenamiento interno de la aplicacion:
            try {
                // Verificar si el archivo ya existe en el almacenamiento interno
                val internalFile = File(context.filesDir, "data.json")
                if (!internalFile.exists()) {
                    // Leer el archivo JSON desde la carpeta de activos
                    val inputStream: InputStream = context.assets.open("data.json")
                    val buffer = ByteArray(inputStream.available())
                    inputStream.read(buffer)
                    inputStream.close()

                    // Escribir el archivo en el almacenamiento interno
                    val outputStream: OutputStream = FileOutputStream(internalFile)
                    outputStream.write(buffer)
                    outputStream.close()

                }

                val inputStreamCopy = FileInputStream(internalFile)
                val bufferedReaderCopy = BufferedReader(InputStreamReader(inputStreamCopy))
                val stringBuilder = StringBuilder()
                var line: String?

                // Leer línea por línea y construir el contenido JSON
                while (bufferedReaderCopy.readLine().also { line = it } != null) {
                    stringBuilder.append(line)
                }

                inputStreamCopy.close()


                //Si el modelo existe ya entonces leemos el json y lo cargamos
                val gson = Gson()


                _appState = MutableStateFlow(
                    ApplicationState(
                        gson.fromJson(
                            stringBuilder.toString(),
                            Application::class.java
                        )
                    )
                )

            } catch (e: IOException) {
                e.printStackTrace()
            }

*/
            _appState = MutableStateFlow(
                ApplicationState(application = Application())
            )
            _appState.value.context =context
            appState = _appState.asStateFlow()
            this.containerRepository = containerRepository
            this.sectionRepository = sectionRepository
            this.containerSectionRepository = containerSectionRepository
            this.sectionRecordRepository = sectionRecordRepository
            this.foodRecordRepository = foodRecordRepository
            this.foodRepository = foodRepository
            this.sectionFoodRepository = sectionFoodRepository
            this.categoryRepository = categoryRepository

        }

    }
    
    fun writeJson(){

       /* try {
            // Convertir el objeto a JSON utilizando Gson
            val gson = //Gson()
            val jsonString = gson.toJson(_appState.value.application)

            // Obtener la ruta del archivo en el almacenamiento interno
            val file = File(_appState.value.context!!.filesDir, "data.json")

            // Escribir el JSON en el archivo del almacenamiento interno
            val outputStream: OutputStream = FileOutputStream(file)
            outputStream.write(jsonString.toByteArray())
            outputStream.close()


        } catch (e: Exception) {
            e.printStackTrace()
        }*/
    }

    fun updateModel(){
        getAllFoods()
        getFoodOfContainer()
        getContainersWithFoodSelected()
        updateFoodSelected(_appState.value.foodEdited!!.first.name)
    }
}