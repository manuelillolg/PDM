package com.example.fridgeapp.ui

import android.content.Context
import android.provider.Settings.Global.getString
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import com.example.fridgeapp.R
import com.example.fridgeapp.model.Application
import com.example.fridgeapp.model.ApplicationState
import com.example.fridgeapp.model.Container
import com.example.fridgeapp.model.Food
import com.example.fridgeapp.model.Route
import com.example.fridgeapp.utils.readJSONFromAssets
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream


class AppViewModel : ViewModel() {

    private lateinit var _appState : MutableStateFlow<ApplicationState>
    lateinit var appState: StateFlow<ApplicationState>


    fun getAllFoods():Unit{
        var foods =  sortedSetOf<String>().toMutableSet()


       for (container in _appState.value.application.containers) {

           for (section in container.sections) {

               for (food in section.foods) {
                   foods.add(food.name)
               }
           }
       }

        foods = foods.sorted().toMutableSet()

        _appState.update { currentState ->
            currentState.copy(
                allFoods = foods
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

    //Funciones para la ventana de edición de alimento

    fun updateSelectedFoodEdit(food:Pair<Food,Route>){
        _appState.update {currentState ->
            currentState.copy(
                foodEdited = food,
                provisionalFoodNameEdited = food.first.name
            )
        }
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
        var foods =  sortedSetOf<String>().toMutableSet()

        _appState.value.application.foodNameList.forEach{possibleFood ->
            foods.add(possibleFood)
        }

        foods = foods.sorted().toMutableSet()

        _appState.update { currentState ->
            currentState.copy(
                allFoods = foods
            )
        }


        updateFoodSelected(foods.toList()[0])
    }

    fun getAllPossibleCategories(){
        var categories =  sortedSetOf<String>().toMutableSet()

        _appState.value.application.categoryList.forEach{category ->
            categories.add(category)
        }

        categories = categories.sorted().toMutableSet()

        _appState.update { currentState ->
            currentState.copy(
                allCategories = categories
            )
        }
        Log.d("taggg",categories.toString())
        updateCategory(categories.toList()[0])
    }

    fun getAllContainers(){
        var containers =  sortedSetOf<String>().toMutableSet()

        _appState.value.application.containers.forEach{container ->
            containers.add(container.name)
        }

        containers = containers.sorted().toMutableSet()

        _appState.update { currentState ->
            currentState.copy(
                allContainers = containers
            )
        }

        updateContainerSelected(containers.toList()[0])
    }


    fun getAllSectionsOfContainer(){
        var sections =  sortedSetOf<String>().toMutableSet()

        _appState.value.application.containers.forEach{container ->
            if(container.name == _appState.value.containerNameSelected){
                container.sections.forEach{section ->
                    sections.add(section.name)
                }
            }
        }

        sections = sections.sorted().toMutableSet()

        _appState.update { currentState ->
            currentState.copy(
                sectionsOfContainer = sections
            )
        }

        updateSectionNameSelected(sections.toList()[0])
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
    fun updateSectionNameSelected(name:String){
        _appState.update { currentState ->
            currentState.copy(
                sectionNameSelected = name
            )
        }
    }

    fun addFood():Boolean{
        validateQuantity()

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
        }
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
        updateFoodSelected(_appState.value.allFoods.toList()[0])
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

    fun initModel(jsonString:String, context:Context){

        if(! ::_appState.isInitialized) {
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


            _appState.value.context =context
            appState = _appState.asStateFlow()

        }

    }
    
    fun writeJson(){

        try {
            // Convertir el objeto a JSON utilizando Gson
            val gson = Gson()
            val jsonString = gson.toJson(_appState.value.application)

            // Obtener la ruta del archivo en el almacenamiento interno
            val file = File(_appState.value.context!!.filesDir, "data.json")

            // Escribir el JSON en el archivo del almacenamiento interno
            val outputStream: OutputStream = FileOutputStream(file)
            outputStream.write(jsonString.toByteArray())
            outputStream.close()


        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun updateModel(){
        getAllFoods()
        getFoodOfContainer()
        getContainersWithFoodSelected()
        updateFoodSelected(_appState.value.foodEdited!!.first.name)
    }
}