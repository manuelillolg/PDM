package com.example.fridgeapp.model

import android.content.Context


data class ApplicationState(
    val application : Application,
    var context: Context? = null,
    var containerNameSelected:String = "",
    var allFoods :Set<String> = mutableSetOf(),
    var foodSelected : String = "",
    var foodByContainer: List<Pair<Food,Route> > = mutableListOf(),
    var containersWithFoodName: Set<String> = mutableSetOf(),

    //Variables de estado de la página de edicion de alimento
    var foodEdited : Pair<Food,Route>? = null,
    var provisionalFoodNameEdited:String = "",

    //Variables de estado para añadir alimento
    var allCategories: Set<String> = mutableSetOf(),
    var allContainers: Set<String> = mutableSetOf(),
    var sectionsOfContainer: Set<String> = mutableSetOf(),
    var provisionalQuantity: String = "0",
    var isQuantityCorrect: Boolean = true,
    var sectionNameSelected: String = "",
    var categoryNameSelected: String ="",
    var isValidFood: Boolean = true,


)

data class Route(
    var containerIndex: Int = 0,
    var sectionIndex: Int = 0,
    var foodIndex : Int = 0
)
data class Application(
    val containers : MutableList<Container> = mutableListOf(),
    var foodNameList:MutableSet<String> = mutableSetOf(),
    var categoryList:MutableSet<String> = mutableSetOf()
)

data class Container(
    val name: String,
    val sections: List<Section>
)

data class Section(
    val name: String,
    var foods : MutableList<Food>
)

data class Food(
    var name: String,
    val category: String,
    val quantity: Int,
    val location: String ="",
    val code: String =""
)