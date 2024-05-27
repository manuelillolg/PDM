package com.example.fridgeapp.model

import android.content.Context
import com.example.fridgeapp.data.CategoryEntity
import com.example.fridgeapp.data.ContainerEntity
import com.example.fridgeapp.data.FoodEntity
import com.example.fridgeapp.data.FoodRecordEntity
import com.example.fridgeapp.data.FoodV2
import com.example.fridgeapp.data.SectionEntity
import com.example.fridgeapp.data.SectionRecordEntity


data class ApplicationState(
    val application : Application,
    var context: Context? = null,
    var containerNameSelected:String = "",
    var allFoods :List<FoodEntity> = mutableListOf(),
    var allFoodsByName :List<FoodV2> = mutableListOf(),
    var allFoodsOfContainer: List<FoodV2> = mutableListOf(),
    var foodSelected : String = "",
    var foodByContainer: List<Pair<Food,Route> > = mutableListOf(),
    var containersWithFoodName: Set<String> = mutableSetOf(),

    //Variables de estado de la página de edicion de alimento
    var foodEdited : Pair<Food,Route>? = null,
    var provisionalFoodNameEdited:String = "",

    //Variables de estado para añadir alimento
    var allCategories: List<CategoryEntity> = mutableListOf(),
    var allContainers: List<ContainerEntity> = mutableListOf(),
    var sectionsOfContainer: List<SectionEntity> = mutableListOf(),
    var allRecordFoods: List<FoodRecordEntity> = mutableListOf(),
    var recordFoodSelected: FoodRecordEntity? = null,
    var provisionalQuantity: String = "1",
    var isQuantityCorrect: Boolean = true,
    var sectionSelected: SectionEntity? = SectionEntity(name=""),
    var categoryNameSelected: String ?="",
    var isValidFood: Boolean = true,
    var containerSelected : ContainerEntity? = ContainerEntity(name=""),
    var foodsToAdd: MutableList<FoodV2> = mutableListOf(),
    var foodV2: FoodV2 = FoodV2(),
    var provisionalComment: String = "",

    //Variables para añadir categorias
    var validCategory : Boolean = true,
    var categorySelected: CategoryEntity? = null,
    var searchingContainer: ContainerEntity? = null,
    var findFoodContainerStep: Int = 0,

    //Variables para administracion de secciones
    var sectionsRecordList: List<SectionRecordEntity> = mutableListOf() ,
    var validSection: Boolean = true,

    //Variables para administración de contenedores
    var validContainer: Boolean = true



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